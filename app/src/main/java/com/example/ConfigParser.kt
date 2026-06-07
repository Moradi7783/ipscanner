package com.example

import android.util.Base64
import org.json.JSONObject

object ConfigParser {

    fun isIpAddress(str: String): Boolean {
        val clean = str.trim()
        val ipRegex = Regex("""^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$""")
        return ipRegex.matches(clean)
    }

    fun getRandomIpsFromCidr(cidr: String, count: Int): List<String> {
        try {
            val parts = cidr.trim().split("/")
            if (parts.size != 2) return emptyList()
            val ipParts = parts[0].split(".")
            if (ipParts.size != 4) return emptyList()
            val prefixLen = parts[1].toInt()

            var ipInt = 0
            for (i in 0..3) {
                ipInt = (ipInt shl 8) or (ipParts[i].toInt() and 0xFF)
            }

            val mask = if (prefixLen == 0) 0 else (0xFFFFFFFF.toLong() shl (32 - prefixLen)).toInt()
            val network = ipInt and mask
            val hostCount = (1L shl (32 - prefixLen)) - 2

            if (hostCount <= 0) return listOf(parts[0])

            val ips = mutableSetOf<String>()
            val actualCount = minOf(count, hostCount.toInt())

            val random = java.util.Random()
            var attempts = 0
            while (ips.size < actualCount && attempts < actualCount * 10) {
                attempts++
                val offset = (random.nextDouble() * hostCount).toLong() + 1
                val targetIpInt = network or (offset.toInt() and mask.inv())

                val octet1 = (targetIpInt ushr 24) and 0xFF
                val octet2 = (targetIpInt ushr 16) and 0xFF
                val octet3 = (targetIpInt ushr 8) and 0xFF
                val octet4 = targetIpInt and 0xFF
                ips.add("$octet1.$octet2.$octet3.$octet4")
            }
            return ips.toList()
        } catch (e: Exception) {
            return emptyList()
        }
    }

    fun parseAndConvert(originalConfig: String, cleanIps: List<String>): List<String> {
        val trimmed = originalConfig.trim()
        if (trimmed.isEmpty() || cleanIps.isEmpty()) return emptyList()

        return when {
            trimmed.startsWith("vless://") -> convertVlessOrTrojan("vless://", trimmed, cleanIps)
            trimmed.startsWith("trojan://") -> convertVlessOrTrojan("trojan://", trimmed, cleanIps)
            trimmed.startsWith("vmess://") -> convertVmess(trimmed, cleanIps)
            else -> emptyList()
        }
    }

    private fun convertVlessOrTrojan(prefix: String, link: String, cleanIps: List<String>): List<String> {
        return try {
            val inside = link.substring(prefix.length)

            // Split fragment
            val hashIndex = inside.indexOf('#')
            val (restMain, originalFragment) = if (hashIndex != -1) {
                Pair(inside.substring(0, hashIndex), inside.substring(hashIndex + 1))
            } else {
                Pair(inside, "")
            }

            // Split query params
            val queryIndex = restMain.indexOf('?')
            val (mainAndPort, originalQuery) = if (queryIndex != -1) {
                Pair(restMain.substring(0, queryIndex), restMain.substring(queryIndex + 1))
            } else {
                Pair(restMain, "")
            }

            // Split user and host
            val atIndex = mainAndPort.lastIndexOf('@')
            if (atIndex == -1) return emptyList()

            val credentials = mainAndPort.substring(0, atIndex)
            val hostAndPort = mainAndPort.substring(atIndex + 1)

            val colonIndex = hostAndPort.lastIndexOf(':')
            if (colonIndex == -1) return emptyList()

            val originalHost = hostAndPort.substring(0, colonIndex)
            val port = hostAndPort.substring(colonIndex + 1)

            val isOriginalHostDomain = !isIpAddress(originalHost)

            // Parse query parameters
            val params = mutableMapOf<String, String>()
            if (originalQuery.isNotEmpty()) {
                originalQuery.split("&").forEach { pair ->
                    val parts = pair.split("=", limit = 2)
                    if (parts.size == 2) {
                        params[parts[0]] = parts[1]
                    } else if (parts.size == 1) {
                        params[parts[0]] = ""
                    }
                }
            }

            // If original host is a domain, set sni and host in parameters if they are not already set
            if (isOriginalHostDomain) {
                if (!params.containsKey("sni")) {
                    params["sni"] = originalHost
                }
                if (!params.containsKey("host")) {
                    params["host"] = originalHost
                }
            }

            cleanIps.mapIndexed { index, cleanIp ->
                val queryStr = params.map { "${it.key}=${it.value}" }.joinToString("&")
                val fragment = if (originalFragment.isNotEmpty()) {
                    "$originalFragment-CleanScanned-${index + 1}"
                } else {
                    "CleanScanned-${index + 1}"
                }
                "$prefix$credentials@$cleanIp:$port?$queryStr#$fragment"
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun convertVmess(link: String, cleanIps: List<String>): List<String> {
        return try {
            val base64 = link.substring("vmess://".length).trim()
            val jsonStr = String(Base64.decode(base64, Base64.DEFAULT), Charsets.UTF_8)
            val obj = JSONObject(jsonStr)

            val originalAdd = obj.optString("add", "")
            val originalHost = obj.optString("host", "")
            val originalSni = obj.optString("sni", "")
            val originalPs = obj.optString("ps", "VMess-Config")

            val fallbackSni = if (originalSni.isEmpty()) originalAdd else originalSni
            val fallbackHost = if (originalHost.isEmpty()) originalAdd else originalHost

            cleanIps.mapIndexed { index, cleanIp ->
                val newObj = JSONObject(jsonStr)
                newObj.put("add", cleanIp)

                if (fallbackSni.isNotEmpty() && !isIpAddress(fallbackSni)) {
                    newObj.put("sni", fallbackSni)
                }
                if (fallbackHost.isNotEmpty() && !isIpAddress(fallbackHost)) {
                    newObj.put("host", fallbackHost)
                }

                newObj.put("ps", "$originalPs-CleanScanned-${index + 1}")

                val newJson = newObj.toString()
                val newBase64 = Base64.encodeToString(newJson.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
                "vmess://$newBase64"
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
