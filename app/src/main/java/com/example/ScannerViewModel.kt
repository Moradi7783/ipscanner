package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.ssl.SNIHostName
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

data class ScannedIp(
    val ip: String,
    val latency: Long?,
    val speed: Double?, // KB/s
    val isSpeedTesting: Boolean = false,
    val isSuccess: Boolean = false
)

data class PresetCidr(
    val name: String,
    val desc: String,
    val cidrs: List<String>
)

class ScannerViewModel : ViewModel() {

    // Presets for CDN ranges popular in general usage
    val presetCidrs = listOf(
        PresetCidr(
            "Cloudflare General",
            "بازه های عمومی اصلی کلودفلر (بسیار محبوب)",
            listOf("172.64.0.0/13", "104.16.0.0/12", "162.158.0.0/15")
        ),
        PresetCidr(
            "Cloudflare Select",
            "بازه های منتخب و بهینه کلودفلر",
            listOf("108.162.192.0/18", "141.101.64.0/18", "188.114.96.0/20", "190.93.240.0/20")
        ),
        PresetCidr(
            "Akamai Smart CDN",
            "رنج‌های اختصاصی و هوشمند آکامی (Akamai)",
            listOf("23.32.0.0/11", "104.64.0.0/10", "184.24.0.0/13", "184.84.0.0/14", "23.200.0.0/13", "95.100.0.0/15")
        ),
        PresetCidr(
            "Amazon Cloudfront",
            "بازه های شبکه آمازون CDN",
            listOf("13.32.0.0/15", "54.239.128.0/18")
        ),
        PresetCidr(
            "Gcore CDN",
            "بازه های شبکه جی کور CDN",
            listOf("92.223.70.0/24", "92.223.64.0/22")
        )
    )

    // Configuration states
    private val _selectedPresets = MutableStateFlow(setOf("Cloudflare General"))
    val selectedPresets = _selectedPresets.asStateFlow()

    private val _customCidrs = MutableStateFlow("")
    val customCidrs = _customCidrs.asStateFlow()

    private val _scanPort = MutableStateFlow(443)
    val scanPort = _scanPort.asStateFlow()

    private val _scanTimeout = MutableStateFlow(1200) // ms
    val scanTimeout = _scanTimeout.asStateFlow()

    private val _concurrency = MutableStateFlow(40)
    val concurrency = _concurrency.asStateFlow()

    private val _scanLimit = MutableStateFlow(100) // limit for random IP scan selection
    val scanLimit = _scanLimit.asStateFlow()

    private val _akamaiSmartScan = MutableStateFlow(true)
    val akamaiSmartScan = _akamaiSmartScan.asStateFlow()

    private val _deepTargetedMode = MutableStateFlow(false)
    val deepTargetedMode = _deepTargetedMode.asStateFlow()

    private val _selectedVpnIp = MutableStateFlow<String>("")
    val selectedVpnIp = _selectedVpnIp.asStateFlow()

    private val _selectedVpnPort = MutableStateFlow<Int>(443)
    val selectedVpnPort = _selectedVpnPort.asStateFlow()

    fun setSelectedVpnIp(ip: String) {
        _selectedVpnIp.update { ip }
    }

    fun setSelectedVpnPort(port: Int) {
        _selectedVpnPort.update { port }
    }

    private val _directConnectionStatus = MutableStateFlow<String>("نامشخص")
    val directConnectionStatus = _directConnectionStatus.asStateFlow()

    private val _directInternetPing = MutableStateFlow<Long?>(null)
    val directInternetPing = _directInternetPing.asStateFlow()

    private val _directIpAndIsp = MutableStateFlow<String>("در حال دریافت...")
    val directIpAndIsp = _directIpAndIsp.asStateFlow()

    private val _isMeasuringDirect = MutableStateFlow(false)
    val isMeasuringDirect = _isMeasuringDirect.asStateFlow()

    init {
        checkDirectInternet()
    }

    fun setAkamaiSmartScan(value: Boolean) {
        _akamaiSmartScan.value = value
    }

    fun setDeepTargetedMode(value: Boolean) {
        _deepTargetedMode.value = value
    }

    fun checkDirectInternet() {
        viewModelScope.launch {
            _isMeasuringDirect.value = true
            _directConnectionStatus.value = "در حال تست..."
            _directIpAndIsp.value = "در حال دریافت آدرس..."
            
            val latency = checkTcpPing("1.1.1.1", 53, 2000)
            if (latency != null) {
                _directConnectionStatus.value = "متصل (بدون پروکسی)"
                _directInternetPing.value = latency
                val publicIp = fetchPublicIp()
                _directIpAndIsp.value = if (publicIp != null) "آی‌پی: $publicIp" else "آی‌پی مستقیم برقرار"
            } else {
                _directConnectionStatus.value = "قطع یا فیلتر شدید"
                _directInternetPing.value = null
                _directIpAndIsp.value = "عدم امکان اتصال مستقیم به اینترنت جهانی"
            }
            _isMeasuringDirect.value = false
        }
    }

    private suspend fun fetchPublicIp(): String? = withContext(Dispatchers.IO) {
        var socket: Socket? = null
        try {
            socket = Socket()
            socket.connect(InetSocketAddress("104.16.248.249", 80), 2000) // CF direct IP
            val writer = socket.outputStream.bufferedWriter()
            val reader = socket.inputStream.bufferedReader()
            
            writer.write("GET /cdn-cgi/trace HTTP/1.1\r\nHost: 1.1.1.1\r\nConnection: close\r\n\r\n")
            writer.flush()
            
            var line: String?
            var ipLine: String? = null
            while (reader.readLine().also { line = it } != null) {
                if (line?.startsWith("ip=") == true) {
                    ipLine = line?.substringAfter("ip=")
                }
            }
            ipLine
        } catch (e: Exception) {
            null
        } finally {
            try { socket?.close() } catch (_: Exception) {}
        }
    }

    // Scanner execution states
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _progressCurrent = MutableStateFlow(0)
    val progressCurrent = _progressCurrent.asStateFlow()

    private val _progressTotal = MutableStateFlow(0)
    val progressTotal = _progressTotal.asStateFlow()

    private val _scannedIps = MutableStateFlow<List<ScannedIp>>(emptyList())
    val scannedIps = _scannedIps.asStateFlow()

    // Converter Screen states
    private val _converterOriginalConfig = MutableStateFlow("")
    val converterOriginalConfig = _converterOriginalConfig.asStateFlow()

    private val _converterCleanIps = MutableStateFlow("")
    val converterCleanIps = _converterCleanIps.asStateFlow()

    private val _convertedConfigs = MutableStateFlow<List<String>>(emptyList())
    val convertedConfigs = _convertedConfigs.asStateFlow()

    private var scanJob: Job? = null

    fun togglePreset(name: String) {
        _selectedPresets.update {
            if (it.contains(name)) {
                if (it.size > 1) it - name else it
            } else {
                it + name
            }
        }
    }

    fun setCustomCidrs(value: String) {
        _customCidrs.value = value
    }

    fun setScanPort(value: Int) {
        _scanPort.value = value
    }

    fun setScanTimeout(value: Int) {
        _scanTimeout.value = value
    }

    fun setConcurrency(value: Int) {
        _concurrency.value = value
    }

    fun setScanLimit(value: Int) {
        _scanLimit.value = value
    }

    fun setConverterOriginalConfig(value: String) {
        _converterOriginalConfig.value = value
        triggerConversion()
    }

    fun setConverterCleanIps(value: String) {
        _converterCleanIps.value = value
        triggerConversion()
    }

    fun triggerConversion() {
        val original = _converterOriginalConfig.value
        val ips = _converterCleanIps.value
            .split(Regex("[\n,;]+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        
        if (original.isNotEmpty() && ips.isNotEmpty()) {
            _convertedConfigs.value = ConfigParser.parseAndConvert(original, ips)
        } else {
            _convertedConfigs.value = emptyList()
        }
    }

    fun importHealthyIpsToConverter() {
        val healthyIpsStr = _scannedIps.value
            .filter { it.isSuccess && it.latency != null }
            .sortedBy { it.latency }
            .map { it.ip }
            .joinToString("\n")
        _converterCleanIps.value = healthyIpsStr
        triggerConversion()
    }

    fun stopScan() {
        scanJob?.cancel()
        _isScanning.value = false
    }

    fun startScan() {
        if (_isScanning.value) return
        _isScanning.value = true
        _scannedIps.value = emptyList()
        _progressCurrent.value = 0

        scanJob = viewModelScope.launch {
            try {
                // Collect CIDRs
                val allCidrs = mutableListOf<String>()
                
                // From chosen presets
                val activePresets = _selectedPresets.value
                for (p in presetCidrs) {
                    if (activePresets.contains(p.name)) {
                        allCidrs.addAll(p.cidrs)
                    }
                }

                // From custom CIDRs field
                if (_customCidrs.value.isNotEmpty()) {
                    _customCidrs.value.split(Regex("[\n,;]+"))
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .forEach { allCidrs.add(it) }
                }

                if (allCidrs.isEmpty()) {
                    _isScanning.value = false
                    return@launch
                }

                // Scatter random IPs from these CIDRs
                val limit = _scanLimit.value
                val ipsToScan = mutableListOf<String>()
                val ipsPerCidr = maxOf(5, limit / allCidrs.size)
                
                for (cidr in allCidrs) {
                    if (cidr.contains("/")) {
                        val genIps = ConfigParser.getRandomIpsFromCidr(cidr, ipsPerCidr)
                        ipsToScan.addAll(genIps)
                    } else if (ConfigParser.isIpAddress(cidr)) {
                        ipsToScan.add(cidr)
                    }
                }

                val finalScanList = ipsToScan.shuffled().take(limit)
                if (finalScanList.isEmpty()) {
                    _isScanning.value = false
                    return@launch
                }

                _progressTotal.value = finalScanList.size

                val rawConcurrency = _concurrency.value
                val effectiveConcurrency = if (_deepTargetedMode.value) maxOf(10, rawConcurrency / 2) else rawConcurrency
                val sem = Semaphore(effectiveConcurrency)
                val port = _scanPort.value
                val timeout = _scanTimeout.value

                val jobs = finalScanList.map { ip ->
                    async(Dispatchers.IO) {
                        sem.withPermit {
                            val latency = checkSmartPing(ip, port, timeout, _deepTargetedMode.value, _akamaiSmartScan.value)
                            withContext(Dispatchers.Main) {
                                _scannedIps.update { list ->
                                    val item = ScannedIp(
                                        ip = ip,
                                        latency = latency,
                                        speed = null,
                                        isSuccess = latency != null
                                    )
                                    (list + item).sortedWith(compareBy<ScannedIp> { !it.isSuccess }
                                        .thenBy { it.latency ?: Long.MAX_VALUE })
                                }
                                _progressCurrent.value += 1
                            }
                        }
                    }
                }

                jobs.awaitAll()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isScanning.value = false
            }
        }
    }

    private suspend fun checkSmartPing(ip: String, port: Int, timeout: Int, deepMode: Boolean, akamaiSmart: Boolean): Long? {
        val attempts = if (deepMode) 2 else 1
        var bestLatency: Long? = null
        val effectiveTimeout = if (deepMode) (timeout * 1.5).toInt() else timeout
        
        for (i in 1..attempts) {
            val lat = checkTcpPing(ip, port, effectiveTimeout)
            if (lat != null) {
                bestLatency = if (bestLatency == null) lat else minOf(bestLatency, lat)
                
                // If it is Akamai and smart verification is on, verify if active Akamai SSL responds
                if (akamaiSmart && isAkamaiIp(ip)) {
                    val akamaiOk = verifyAkamaiHost(ip, port, effectiveTimeout)
                    if (!akamaiOk) {
                        return null
                    }
                }
                
                if (!deepMode) break // Standard mode needs only one successful ping
            }
        }
        return bestLatency
    }

    private fun isAkamaiIp(ip: String): Boolean {
        return ip.startsWith("23.") || ip.startsWith("184.") || ip.startsWith("95.100.") || 
               (ip.startsWith("104.") && ip.split(".")[1].toIntOrNull()?.let { it in 64..127 } ?: false)
    }

    private suspend fun verifyAkamaiHost(ip: String, port: Int, timeout: Int): Boolean = withContext(Dispatchers.IO) {
        var sslSocket: Socket? = null
        try {
            val factory = SSLSocketFactory.getDefault()
            sslSocket = factory.createSocket()
            sslSocket.connect(InetSocketAddress(ip, port), timeout)
            sslSocket.soTimeout = timeout
            
            if (sslSocket is SSLSocket) {
                val sslParams = sslSocket.sslParameters
                sslParams.serverNames = listOf(SNIHostName("www.akamai.com"))
                sslSocket.sslParameters = sslParams
                sslSocket.startHandshake()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        } finally {
            try { sslSocket?.close() } catch (_: Exception) {}
        }
    }

    private suspend fun checkTcpPing(ip: String, port: Int, timeout: Int): Long? = withContext(Dispatchers.IO) {
        val start = System.currentTimeMillis()
        var socket: Socket? = null
        try {
            socket = Socket()
            socket.connect(InetSocketAddress(ip, port), timeout)
            val duration = System.currentTimeMillis() - start
            duration
        } catch (e: Exception) {
            null
        } finally {
            try {
                socket?.close()
            } catch (_: Exception) {}
        }
    }

    fun runSpeedTest(targetIp: String) {
        viewModelScope.launch {
            // Mark as testing speed
            _scannedIps.update { list ->
                list.map { if (it.ip == targetIp) it.copy(isSpeedTesting = true) else it }
            }

            val speedValue = measureSpeed(targetIp)

            _scannedIps.update { list ->
                list.map { if (it.ip == targetIp) it.copy(isSpeedTesting = false, speed = speedValue) else it }
            }
        }
    }

    private suspend fun measureSpeed(ip: String): Double? = withContext(Dispatchers.IO) {
        var sslSocket: Socket? = null
        try {
            val factory = SSLSocketFactory.getDefault()
            sslSocket = factory.createSocket()
            sslSocket.connect(InetSocketAddress(ip, 443), 2500)
            sslSocket.soTimeout = 4000

            if (sslSocket is SSLSocket) {
                val sslParams = sslSocket.sslParameters
                sslParams.serverNames = listOf(SNIHostName("speed.cloudflare.com"))
                sslSocket.sslParameters = sslParams
                sslSocket.startHandshake()
            }

            val writer = sslSocket.outputStream.bufferedWriter()
            val reader = sslSocket.inputStream

            // Read 50KB payload
            val request = "GET /__down?bytes=51200 HTTP/1.1\r\n" +
                          "Host: speed.cloudflare.com\r\n" +
                          "User-Agent: Mozilla/5.0 CDNScanner\r\n" +
                          "Connection: close\r\n\r\n"

            writer.write(request)
            writer.flush()

            val headerBuffer = StringBuilder()
            var char: Int
            while (true) {
                char = reader.read()
                if (char == -1) break
                headerBuffer.append(char.toChar())
                if (headerBuffer.endsWith("\r\n\r\n")) {
                    break
                }
            }

            val bodyStartTime = System.currentTimeMillis()
            val buffer = ByteArray(2048)
            var totalBytesRead = 0

            while (true) {
                val read = reader.read(buffer)
                if (read == -1) break
                totalBytesRead += read
                if (System.currentTimeMillis() - bodyStartTime > 4000) {
                    break
                }
            }

            val duration = System.currentTimeMillis() - bodyStartTime
            if (duration > 0 && totalBytesRead > 500) {
                // Return scale in KB/s
                val speedKbs = (totalBytesRead.toDouble() / 1024.0) / (duration.toDouble() / 1000.0)
                speedKbs
            } else {
                null
            }
        } catch (e: Exception) {
            null
        } finally {
            try {
                sslSocket?.close()
            } catch (_: Exception) {}
        }
    }
}
