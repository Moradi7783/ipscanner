package com.example.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.HorizontalDivider
import android.net.VpnService
import android.net.Uri
import android.content.Intent
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.VpnTunnelService
import com.example.ScannerViewModel
import com.example.ScannedIp
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Sleek Interface Theme Color Tokens (Indigo & Slate)
private val CyberBgDeep = Color(0xFF0F172A) // Slate 900 (Sleek deep slate background)
private val CyberBgCard = Color(0xFF1E293B) // Slate 800 (Sleek active panel background)
private val CyberPrimary = Color(0xFF6366F1) // Indigo 500 (Brand primary highlight)
private val CyberSecondary = Color(0xFF4F46E5) // Indigo 600 (Darker indigo)
private val CyberSuccess = Color(0xFF22C55E) // Green 500 (Glow emerald for alive status)
private val CyberMuted = Color(0xFF94A3B8) // Slate 400 (Sleek text/subtitle)
private val CyberBorder = Color(0x0DFFFFFF) // White 5% (Tailwind border-white/5)

@Composable
fun CdnAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = CyberBgDeep,
            surface = CyberBgCard,
            primary = CyberPrimary,
            secondary = CyberSecondary,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        content = content
    )
}

@Composable
fun MainAppScreen(viewModel: ScannerViewModel) {
    var activeTab by remember { mutableStateOf(0) }
    val isScanning by viewModel.isScanning.collectAsState()

    CdnAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = CyberBgDeep
        ) {
            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier
                            .background(CyberBgCard)
                            .padding(top = 16.dp)
                    ) {
                        // Brand Header matching Sleek Interface Design
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "لک (Lak CDN)",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.End)
                                )
                                Text(
                                    text = "LAK SMART PRO  •  ACTIVE DEEP DIAGNOSTIC",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = CyberPrimary
                                )
                            }
                            // Sleek visual diagnostic pulse light matching Tailwind: bg-indigo-500/20 border border-indigo-500/30
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(CyberPrimary.copy(alpha = 0.2f))
                                    .border(1.dp, CyberPrimary.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(CyberPrimary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Custom Tabs in Persian layout direction - Sleek Capsule Style
                        TabRow(
                            selectedTabIndex = activeTab,
                            containerColor = CyberBgCard.copy(alpha = 0.5f),
                            contentColor = CyberPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, CyberBorder, RoundedCornerShape(20.dp)),
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                                    color = CyberPrimary,
                                    height = 3.dp
                                )
                            },
                            divider = {} // Removed line divider for modern look
                        ) {
                            Tab(
                                selected = activeTab == 0,
                                onClick = { activeTab = 0 },
                                text = {
                                    Text(
                                        text = "اسکنر آی‌پی",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                },
                                selectedContentColor = CyberPrimary,
                                unselectedContentColor = CyberMuted
                            )
                            Tab(
                                selected = activeTab == 1,
                                onClick = { activeTab = 1 },
                                text = {
                                    Text(
                                        text = "تبدیل کانفیگ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                },
                                selectedContentColor = CyberPrimary,
                                unselectedContentColor = CyberMuted
                            )
                            Tab(
                                selected = activeTab == 2,
                                onClick = { activeTab = 2 },
                                text = {
                                    Text(
                                        text = "پروکسی تلگرام",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                },
                                selectedContentColor = CyberPrimary,
                                unselectedContentColor = CyberMuted
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (activeTab) {
                        0 -> ScannerTabScreen(viewModel)
                        1 -> GeneratorTabScreen(viewModel)
                        2 -> TelegramProxyTabScreen(viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScannerTabScreen(viewModel: ScannerViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val scannedIps by viewModel.scannedIps.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val progressCurrent by viewModel.progressCurrent.collectAsState()
    val progressTotal by viewModel.progressTotal.collectAsState()

    val selectedPresets by viewModel.selectedPresets.collectAsState()
    val customCidrs by viewModel.customCidrs.collectAsState()
    val scanPort by viewModel.scanPort.collectAsState()
    val scanTimeout by viewModel.scanTimeout.collectAsState()
    val concurrency by viewModel.concurrency.collectAsState()
    val scanLimit by viewModel.scanLimit.collectAsState()

    val akamaiSmartScan by viewModel.akamaiSmartScan.collectAsState()
    val deepTargetedMode by viewModel.deepTargetedMode.collectAsState()
    val directConnectionStatus by viewModel.directConnectionStatus.collectAsState()
    val directInternetPing by viewModel.directInternetPing.collectAsState()
    val directIpAndIsp by viewModel.directIpAndIsp.collectAsState()
    val isMeasuringDirect by viewModel.isMeasuringDirect.collectAsState()

    val vpnConnected by VpnTunnelService.isConnected.collectAsState(false)
    val vpnConnectedIp by VpnTunnelService.connectedIp.collectAsState("")
    val vpnBytes by VpnTunnelService.bytesTransferred.collectAsState(0L)
    val vpnDuration by VpnTunnelService.durationSeconds.collectAsState(0L)

    val selectedVpnIp by viewModel.selectedVpnIp.collectAsState()
    val selectedVpnPort by viewModel.selectedVpnPort.collectAsState()

    val bestIp = remember(scannedIps) {
        scannedIps.firstOrNull { it.isSuccess }?.ip ?: "104.16.248.249"
    }

    var ipToConnect by remember { mutableStateOf("") }
    var portToConnect by remember { mutableStateOf(443) }

    val vpnPrepareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val startIntent = Intent(context, VpnTunnelService::class.java).apply {
                action = VpnTunnelService.ACTION_CONNECT
                putExtra(VpnTunnelService.EXTRA_IP, ipToConnect)
                putExtra(VpnTunnelService.EXTRA_PORT, portToConnect)
            }
            context.startService(startIntent)
        }
    }

    var showConfigPanel by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Direct Internet Connection Diagnostics Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.checkDirectInternet() },
                            enabled = !isMeasuringDirect
                        ) {
                            if (isMeasuringDirect) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = CyberPrimary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "بروزرسانی وضعیت اینترنت مستقیم",
                                    tint = CyberPrimary
                                )
                            }
                        }
                        
                        Text(
                            text = "وضعیت پایداری اتصال مستقیم به اینترنت",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (directInternetPing != null) CyberSuccess else Color(0xFFEF4444))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (directInternetPing != null) "$directInternetPing ms" else "برقراری ناموفق",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = if (directInternetPing != null) CyberSuccess else Color(0xFFEF4444)
                            )
                        }
                        
                        Text(
                            text = "پینگ مستقیم شبکه‌ی ملی تا DNS:",
                            fontSize = 11.sp,
                            color = CyberMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = directIpAndIsp,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            textAlign = TextAlign.Left
                        )
                        
                        Text(
                            text = "آدرس آی‌پی عمومی مستقیم شما:",
                            fontSize = 11.sp,
                            color = CyberMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberBgDeep)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (directInternetPing != null) {
                                "اتصال مستقیم به اینترنت کشور برقرار است. به راحتی اسکن کنید و آی‌پی سالم دریافت کنید."
                            } else {
                                "اینترنت شما در حالت فیلترینگ بسیار شدید قرار دارد. برای پاسخگوئی بهتر کلید اسکن هدفمند (شرایط سخت) را در تنظیمات بخش زیر روشن کنید."
                            },
                            fontSize = 10.sp,
                            color = CyberMuted,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // VPN Tunneling Dashboard Card (Always visible VPN control console)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, if (vpnConnected) CyberSuccess.copy(alpha = 0.5f) else CyberPrimary.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Active Connection Status Beacon
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (vpnConnected) CyberSuccess else CyberMuted)
                                    .border(2.dp, CyberBgDeep, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (vpnConnected) "تونل فعال (CONNECTED)" else "قطع اتصال (DISCONNECTED)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (vpnConnected) CyberSuccess else CyberMuted
                            )
                        }
                        
                        Text(
                            text = "کنترلر و اتصال مستقیم لک (VPN)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Specs Row: IP and Port
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val activeIpText = if (vpnConnected) {
                            if (vpnConnectedIp.isNotEmpty()) "$vpnConnectedIp:443" else "گیت‌وی فعال لک"
                        } else {
                            if (selectedVpnIp.isNotEmpty()) "$selectedVpnIp:443 (انتخابی)" else "$bestIp:443 (خودکار - بهترین پینگ)"
                        }

                        Text(
                            text = activeIpText,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = if (vpnConnected) CyberSuccess else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "آی‌پی سرور فعال:",
                            fontSize = 11.sp,
                            color = CyberMuted
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Transfer details row
                    if (vpnConnected) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val formattedBytes = remember(vpnBytes) {
                                if (vpnBytes < 1024 * 1024) {
                                    String.format("%.1f KB", vpnBytes.toDouble() / 1024.0)
                                } else {
                                    String.format("%.2f MB", vpnBytes.toDouble() / (1024.0 * 1024.0))
                                }
                            }
                            Text(
                                text = formattedBytes,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CyberPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ترافیک مبادله شده پروکسی:",
                                fontSize = 11.sp,
                                color = CyberMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Duration row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val formattedTime = remember(vpnDuration) {
                                val mins = vpnDuration / 60
                                val secs = vpnDuration % 60
                                String.format("%02d:%02d", mins, secs)
                            }
                            Text(
                                text = formattedTime,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "مدت زمان اتصال مداوم:",
                                fontSize = 11.sp,
                                color = CyberMuted
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val explanationText = if (selectedVpnIp.isNotEmpty()) "اتصال به آی‌پی گزینش شده شما صورت می‌پذیرد." else "در صورت نبود آی‌پی اسکن شده، سرور پیش‌فرض استفاده می‌شود."
                            Text(
                                text = explanationText,
                                fontSize = 10.sp,
                                color = CyberMuted,
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = "راهنمای اتصال هوشمند:",
                                fontSize = 11.sp,
                                color = CyberMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Educational step-by-step and technical context in Persian
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CyberBgDeep.copy(alpha = 0.8f))
                            .border(1.dp, CyberPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "راهنمای حل مشکل اتصال تلگرام و وب‌گردی",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF59E0B) // Sleek Amber/Orange Warning/Info
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "اطلاعات مستقیم",
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "۱. آی‌پی‌های تمیز کلودفلر (CDN IP) خودشان به تنهایی سرور پروکسی عمومی نیستند. بلکه مانند یک دروازه میانی (Bridge) ضد فیلتر عمل می‌کنند که ترافیک کلی شبکه به صورت خام روی آنها کار نمی‌کند.",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "۲. برای رد کردن فیلترینگ تلگرام، اینستاگرام و یوتیوب، ترافیک شما باید با یک کانفیگ پروکسی/فیلترشکن شخصی (از نوع VLESS، VMess یا Trojan لود شده روی وب‌سوکت) رمزگذاری شده و سپس به این آی‌پی‌های تمیز فرستاده شود تا با حداکثر سرعت کار کند.",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = CyberBorder.copy(alpha = 0.4f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "💡 گام ساده برای اتصال پرسرعت تلگرام:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberPrimary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "• ابتدا در بخش \"اسکنر زنده\" آی‌پی‌های تمیز را اسکن و پیدا کنید.\n" +
                                   "• یکی از آی‌پی‌های تمیز یافت شده را کپی کنید.\n" +
                                   "• به بخش \"مبدل کانفیگ\" رفته و لینک اکانت فیلترشکن خود را در کادر بالا بگذارید.\n" +
                                   "• روی دکمه تبدیل کلیک کنید تا این آی‌پی‌های سالم جایگزین شوند.\n" +
                                   "• کانفیگ جدید ساخته شده را کپی کرده و در برنامه‌های فیلترشکن استاندارد مانند v2rayNG یا Nekobox وارد کنید و متصل شوید تا با سرعت شگفت‌انگیز تمام برنامه‌ها از جمله تلگرام باز شوند.",
                            fontSize = 10.sp,
                            color = CyberMuted,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val triggerVpnConnection: (String, Int) -> Unit = { ip, port ->
                        if (isScanning) {
                            Toast.makeText(context, "جهت اتصال بدون اختلال، ابتدا اسکنر زنده را متوقف کنید.", Toast.LENGTH_SHORT).show()
                        } else {
                            ipToConnect = ip
                            portToConnect = port
                            val prepIntent = VpnService.prepare(context)
                            if (prepIntent != null) {
                                vpnPrepareLauncher.launch(prepIntent)
                            } else {
                                val startIntent = Intent(context, VpnTunnelService::class.java).apply {
                                    action = VpnTunnelService.ACTION_CONNECT
                                    putExtra(VpnTunnelService.EXTRA_IP, ip)
                                    putExtra(VpnTunnelService.EXTRA_PORT, port)
                                }
                                context.startService(startIntent)
                            }
                        }
                    }

                    if (vpnConnected) {
                        Button(
                            onClick = {
                                val intent = Intent(context, VpnTunnelService::class.java).apply {
                                    action = VpnTunnelService.ACTION_DISCONNECT
                                }
                                context.startService(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF4444),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop VPN",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "قطع اتصال مستقیم پروکسی لک",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                val targetIp = if (selectedVpnIp.isNotEmpty()) selectedVpnIp else bestIp
                                triggerVpnConnection(targetIp, selectedVpnPort)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start VPN",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "برقراری اتصال مستقیم لک (VPN Connect)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Live Control Bar with Sleek styling
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showConfigPanel = !showConfigPanel }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "تنظیمات اسکن",
                                tint = if (showConfigPanel) CyberPrimary else Color.White
                            )
                        }

                        Text(
                            text = "کنترل اسکنر آی‌پی",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "با کلیک روی شروع، اسکنر ابتدا از میان رنج‌های تعبیه شده تعدادی آی‌پی کلودفلر یا دیگر CDN ها را انتخاب و به صورت خودکار پینگ سالم‌ترین‌ها را پایش می‌کند.",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        textAlign = TextAlign.Right,
                        lineHeight = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Smart Operator Carrier selection
                    val selectedOpId by viewModel.selectedOperatorId.collectAsState()
                    val smartTlsCheck by viewModel.smartTlsCheck.collectAsState()
                    val multiPortScan by viewModel.multiPortScan.collectAsState()

                    Text(
                        text = "دستیار پایش هوشمند با توجه به اپراتور سیم‌کارت شما:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Smooth operators cards row
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        viewModel.operatorPresets.forEach { op ->
                            val isSelected = (selectedOpId == op.id)
                            Card(
                                onClick = { viewModel.selectOperator(op.id) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) CyberPrimary.copy(alpha = 0.12f) else CyberBgDeep
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) CyberPrimary else CyberBorder
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) CyberPrimary else Color.Transparent)
                                            .border(1.dp, if (isSelected) CyberPrimary else CyberMuted, CircleShape)
                                    )

                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                                    ) {
                                        Text(
                                            text = op.name,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) CyberPrimary else Color.White
                                        )
                                        Text(
                                            text = op.description,
                                            fontSize = 9.sp,
                                            color = CyberMuted,
                                            textAlign = TextAlign.Right
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Preset tags selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "بازه های انتخابی برای اسکن:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        viewModel.presetCidrs.forEach { pr ->
                            val isSelected = selectedPresets.contains(pr.name)
                            Box(
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) CyberPrimary.copy(alpha = 0.15f) else CyberBgDeep)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) CyberPrimary else CyberBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.togglePreset(pr.name) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = pr.name,
                                    fontSize = 11.sp,
                                    color = if (isSelected) CyberPrimary else Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Collapsible configuration parameters panel
                    AnimatedVisibility(
                        visible = showConfigPanel,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .background(CyberBgDeep, RoundedCornerShape(16.dp))
                                .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "تنظیمات پیشرفته اسکن",
                                fontSize = 13.sp,
                                color = CyberPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.End)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Custom IP list
                            OutlinedTextField(
                                value = customCidrs,
                                onValueChange = { viewModel.setCustomCidrs(it) },
                                label = { Text("آی‌پی یا رنج‌های اختصاصی (CIDR)") },
                                placeholder = { Text("مثال: 104.18.0.0/16 (با اینتر جدا کنید)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberPrimary,
                                    unfocusedBorderColor = CyberBorder,
                                    focusedLabelColor = CyberPrimary,
                                    unfocusedLabelColor = CyberMuted
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Right,
                                    textDirection = TextDirection.Ltr
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Port field
                            OutlinedTextField(
                                value = scanPort.toString(),
                                onValueChange = {
                                    val num = it.toIntOrNull() ?: 443
                                    viewModel.setScanPort(num)
                                },
                                label = { Text("پورت اتصال (مثال ۱04 - ۴۴۳)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberPrimary,
                                    unfocusedBorderColor = CyberBorder,
                                    focusedLabelColor = CyberPrimary,
                                    unfocusedLabelColor = CyberMuted
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Concurrency slider
                            Text(
                                text = "تعداد همزمانی اسکن (Concurrency): $concurrency",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.End)
                            )
                            Slider(
                                value = concurrency.toFloat(),
                                onValueChange = { viewModel.setConcurrency(it.toInt()) },
                                valueRange = 10f..150f,
                                steps = 14,
                                colors = SliderDefaults.colors(
                                    thumbColor = CyberPrimary,
                                    activeTrackColor = CyberPrimary,
                                    inactiveTrackColor = CyberBorder
                                )
                            )

                            // Timeout Slider
                            Text(
                                text = "حداکثر زمان پاسخگوئی پینگ: $scanTimeout میلی ثانیه",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.End)
                            )
                            Slider(
                                value = scanTimeout.toFloat(),
                                onValueChange = { viewModel.setScanTimeout(it.toInt()) },
                                valueRange = 300f..3000f,
                                colors = SliderDefaults.colors(
                                    thumbColor = CyberPrimary,
                                    activeTrackColor = CyberPrimary,
                                    inactiveTrackColor = CyberBorder
                                )
                            )

                            // Scan limit Slider
                            Text(
                                text = "تعداد کل کاندیداهای تصادفی: $scanLimit آی پی",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.End)
                            )
                            Slider(
                                value = scanLimit.toFloat(),
                                onValueChange = { viewModel.setScanLimit(it.toInt()) },
                                valueRange = 30f..400f,
                                colors = SliderDefaults.colors(
                                    thumbColor = CyberPrimary,
                                    activeTrackColor = CyberPrimary,
                                    inactiveTrackColor = CyberBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            
                            // Akamai Smart Verification Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (akamaiSmartScan) CyberPrimary.copy(alpha = 0.08f) else Color.Transparent)
                                    .clickable { viewModel.setAkamaiSmartScan(!akamaiSmartScan) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp, 24.dp)
                                        .clip(CircleShape)
                                        .background(if (akamaiSmartScan) CyberPrimary else CyberBorder)
                                        .padding(4.dp),
                                    contentAlignment = if (akamaiSmartScan) Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "بررسی هوشمند رنج‌های آکامی",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "تست دسترسی به سرورهای Akamai با تکنیک SNI",
                                        fontSize = 9.sp,
                                        color = CyberMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Smart TLS Check (SSL / Gateway Probe) Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (smartTlsCheck) CyberPrimary.copy(alpha = 0.08f) else Color.Transparent)
                                    .clickable { viewModel.setSmartTlsCheck(!smartTlsCheck) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp, 24.dp)
                                        .clip(CircleShape)
                                        .background(if (smartTlsCheck) CyberPrimary else CyberBorder)
                                        .padding(4.dp),
                                    contentAlignment = if (smartTlsCheck) Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "بررسی هوشمند کلودفلر (SSL TLS Probe)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "تست کامل هندشیک رمزگذاری شده با هدف تضمین کارکرد بی نقص",
                                        fontSize = 9.sp,
                                        color = CyberMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Smart Multi-Port Verification Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (multiPortScan) CyberPrimary.copy(alpha = 0.08f) else Color.Transparent)
                                    .clickable { viewModel.setMultiPortScan(!multiPortScan) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp, 24.dp)
                                        .clip(CircleShape)
                                        .background(if (multiPortScan) CyberPrimary else CyberBorder)
                                        .padding(4.dp),
                                    contentAlignment = if (multiPortScan) Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "پایش و اسکن چند پورت همزمان (Multi-Port)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "بررسی همزمان پورت‌های طلایی (8443، 2053 و غیره) برای کشف بهترین پورت",
                                        fontSize = 9.sp,
                                        color = CyberMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Deep Targeted Mode Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (deepTargetedMode) CyberPrimary.copy(alpha = 0.08f) else Color.Transparent)
                                    .clickable { viewModel.setDeepTargetedMode(!deepTargetedMode) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp, 24.dp)
                                        .clip(CircleShape)
                                        .background(if (deepTargetedMode) CyberSuccess else CyberBorder)
                                        .padding(4.dp),
                                    contentAlignment = if (deepTargetedMode) Alignment.CenterEnd else Alignment.CenterStart
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(CyberSuccess.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "مخصوص شرایط فیلتر",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CyberSuccess
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "حالت اسکن هدفمند (شرایط سخت)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Text(
                                        text = "کاهش همزمانی، افزایش تلاش مجدد و تست چندگانه در فیلترینگ شدید",
                                        fontSize = 9.sp,
                                        color = CyberMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (isScanning) {
                                    viewModel.stopScan()
                                } else {
                                    viewModel.startScan()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("scan_trigger_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isScanning) CyberSecondary else CyberPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isScanning) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = "Trigger Status"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isScanning) "توقف اسکن کنونی" else "شروع اسکن زنده",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Scanning Stats Panel matching Sleek Interface design
        if (isScanning || scannedIps.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, CyberBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Alive Status Badge in green-400/10 with rounded layout
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CyberSuccess.copy(alpha = 0.12f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${scannedIps.count { it.isSuccess }} ALIVE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberSuccess
                                )
                            }

                            // Progress numbers
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "SCANNING PROGRESS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp,
                                    color = CyberMuted,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "$progressCurrent",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.White
                                    )
                                    Text(
                                        text = " / $progressTotal IPs",
                                        fontSize = 13.sp,
                                        color = CyberMuted,
                                        modifier = Modifier.padding(bottom = 3.dp, start = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // High-contrast Sleek Progress bar
                        val progressRatio = if (progressTotal > 0) progressCurrent.toFloat() / progressTotal.toFloat() else 0f
                        Column(modifier = Modifier.fillMaxWidth()) {
                            LinearProgressIndicator(
                                progress = progressRatio,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = CyberPrimary,
                                trackColor = CyberBgDeep
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${(progressRatio * 100).toInt()}% COMPLETED",
                                    fontSize = 10.sp,
                                    color = CyberPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "پیشرفت زنده اسکن",
                                    fontSize = 10.sp,
                                    color = CyberMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Bulk Actions buttons
                        AnimatedVisibility(visible = scannedIps.any { it.isSuccess }) {
                            Column {
                                HorizontalDivider(
                                    color = CyberBorder,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 14.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Send to converter
                                    Button(
                                        onClick = {
                                            viewModel.importHealthyIpsToConverter()
                                            Toast.makeText(
                                                context,
                                                "آی‌پی های سالم به بخش مبدل وارد شدند. برای اعمال، به زبانه دوم بروید.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = CyberPrimary.copy(alpha = 0.12f),
                                            contentColor = CyberPrimary
                                        ),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.weight(1f).height(44.dp),
                                        border = BorderStroke(1.dp, CyberPrimary)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "Send",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("ارسال به مبدل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    // Copy Healthy IPs
                                    Button(
                                        onClick = {
                                            val list = scannedIps.filter { it.isSuccess }.map { it.ip }.joinToString("\n")
                                            clipboardManager.setText(AnnotatedString(list))
                                            Toast.makeText(
                                                context,
                                                "آی‌پی های سالم در حافظه کپی شدند.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = CyberSecondary.copy(alpha = 0.12f),
                                            contentColor = CyberSecondary
                                        ),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.weight(1f).height(44.dp),
                                        border = BorderStroke(1.dp, CyberSecondary)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("کپی سالم‌ها", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // List Header label
        if (scannedIps.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "سرعت و تست",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "تاخیر پینگ (نزولی)",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "آدرس آی‌پی (IP)",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            // Empty state illustration
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(CyberBorder),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Empty Scanner Info",
                            tint = CyberMuted,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "هنوز اسکن انجام نشده است",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "از بالای صفحه رنج‌های دلخواه را گزینش و دکمه شروع اسکن را لمس کنید.",
                        fontSize = 12.sp,
                        color = CyberMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Animated items List
        items(
            items = scannedIps,
            key = { it.ip }
        ) { entity ->
            IpResultItem(
                scannedIp = entity,
                onRunSpeedTest = { viewModel.runSpeedTest(entity.ip) },
                onConnectVpn = { ip, port ->
                    if (isScanning) {
                        Toast.makeText(context, "جهت اتصال بدون اختلال، ابتدا اسکنر زنده را متوقف کنید.", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.setSelectedVpnIp(ip)
                        viewModel.setSelectedVpnPort(port)
                        ipToConnect = ip
                        portToConnect = port
                        val prepIntent = VpnService.prepare(context)
                        if (prepIntent != null) {
                            vpnPrepareLauncher.launch(prepIntent)
                        } else {
                            val startIntent = Intent(context, VpnTunnelService::class.java).apply {
                                action = VpnTunnelService.ACTION_CONNECT
                                putExtra(VpnTunnelService.EXTRA_IP, ip)
                                putExtra(VpnTunnelService.EXTRA_PORT, port)
                            }
                            context.startService(startIntent)
                        }
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun IpResultItem(
    scannedIp: ScannedIp,
    onRunSpeedTest: () -> Unit,
    onConnectVpn: (String, Int) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    
    val opacity = if (scannedIp.isSuccess) 1f else 0.65f
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                if (scannedIp.isSuccess) {
                    isExpanded = !isExpanded
                } else {
                    clipboardManager.setText(AnnotatedString(scannedIp.ip))
                    Toast.makeText(context, "${scannedIp.ip} كپی شد", Toast.LENGTH_SHORT).show()
                }
            }
            .alpha(opacity),
        colors = CardDefaults.cardColors(containerColor = CyberBgCard.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CyberBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speed Test Action or Value
                Box(
                    modifier = Modifier
                        .width(96.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (!scannedIp.isSuccess) {
                        Text(
                            text = "ناموفق",
                            color = Color(0xFFEF4444), // Crimson/Red text for failed
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        if (scannedIp.isSpeedTesting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = CyberPrimary,
                                strokeWidth = 2.dp
                            )
                        } else if (scannedIp.speed != null) {
                            Text(
                                text = String.format("%.1f KB/s", scannedIp.speed),
                                color = CyberSuccess,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        } else {
                            IconButton(
                                onClick = onRunSpeedTest,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "تست سرعت",
                                    tint = CyberPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Latency Text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (scannedIp.isSuccess && scannedIp.latency != null) {
                        Text(
                            text = "${scannedIp.latency} ms",
                            color = if (scannedIp.latency < 250) CyberSuccess else Color(0xFFF59E0B), // Sleek Amber for medium pings
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Text(
                            text = "---",
                            color = CyberMuted,
                            fontSize = 13.sp
                        )
                    }
                }

                // IP & Alive dot status indicator in Farsi layout (IP is on Right)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.width(135.dp)
                ) {
                    Text(
                        text = scannedIp.ip,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (scannedIp.isSuccess) Color.White else CyberMuted,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Status dot
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (scannedIp.isSuccess) CyberSuccess else Color(0xFFEF4444)
                            )
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded && scannedIp.isSuccess) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = CyberBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Connect VPN Button
                        Button(
                            onClick = {
                                onConnectVpn(scannedIp.ip, 443)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "اتصال به عنوان وی‌پی‌ان محلی",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("اتصال مستقیم لک", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Copy button
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(scannedIp.ip))
                                Toast.makeText(context, "${scannedIp.ip} کپی شد", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberBgDeep,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(38.dp),
                            border = BorderStroke(1.dp, CyberBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "کپی",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("کپی آی‌پی", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GeneratorTabScreen(viewModel: ScannerViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val originalConfig by viewModel.converterOriginalConfig.collectAsState()
    val cleanIps by viewModel.converterCleanIps.collectAsState()
    val convertedConfigs by viewModel.convertedConfigs.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Explanatory Intro Card styled sleekly
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "مبدل آی‌پی‌های سالم کلودفلر به سرور",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "این سیستم یک کانفیگ اصلی VLESS، Trojan یا VMess را از شما دریافت و بجای آدرس سرور اصلی، تک تک آی‌پی‌های سالم وارد شده را جایگزین می‌کند. همچنین جهت پایداری اتصال، فیلدهای sni و host بصورت خودکار با دامنه قبلی پر خواهند شد.",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        textAlign = TextAlign.Right,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Original config text inputs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "۱. کانفیگ خام (VLESS / Trojan / VMess)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Template quick insertion buttons
                    Text(
                        text = "بارگذاری هوشمند الگوهای خام:",
                        fontSize = 10.sp,
                        color = CyberPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.setConverterOriginalConfig("vless://8ebfca0a-df62-4822-a982-fec86144bb28@sub.mydomain.com:443?type=ws&security=tls&path=%2F#VLESS-Websocket-TLS")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberBgDeep, contentColor = Color.White),
                            border = BorderStroke(1.dp, CyberBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(34.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("VLESS WS", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.setConverterOriginalConfig("vmess://eyJhZGQiOiJzdWIubXlkb21haW4uY29tIiwiYWlkIjoiMCIsImh0dHAiOiJmYWxzZSIsImlkIjoiOGViZmNhMGEtZGY2Mi00ODIyLWE5ODItZmVjODYxNDRiYjI4IiwiaW5zaWRlUG9ydCI6IjAiLCJuZXQiOiJ3cyIsInBhdGgiOiIvIiwicG9ydCI6IjQ0MyIsInBzIjoiVk1lc3MtV2Vic29ja2V0IiIsInNlY3VyaXR5IjoiYXV0byIsInNuaSI6InN1Yi5teWRvbWFpbi5jb20iLCJ0bHMiOiJ0bHMiLCJ0eXBlIjpub25lLCJ2IjoiMiJ9")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberBgDeep, contentColor = Color.White),
                            border = BorderStroke(1.dp, CyberBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(34.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("VMess WS", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.setConverterOriginalConfig("trojan://password123@sub.mydomain.com:443?type=ws&security=tls&path=%2F#Trojan-Websocket-TLS")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberBgDeep, contentColor = Color.White),
                            border = BorderStroke(1.dp, CyberBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(34.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("Trojan WS", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedTextField(
                        value = originalConfig,
                        onValueChange = { viewModel.setConverterOriginalConfig(it) },
                        placeholder = { Text("لینک vless:// یا trojan:// یا vmess:// خام خود را اینجا جایگذاری کنید") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPrimary,
                            unfocusedBorderColor = CyberBorder,
                            focusedLabelColor = CyberPrimary,
                            unfocusedLabelColor = CyberMuted
                        ),
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            textDirection = TextDirection.Ltr
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "۲. لیست آی‌پی‌ های سالم (هر خط یک آی‌پی)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = cleanIps,
                        onValueChange = { viewModel.setConverterCleanIps(it) },
                        placeholder = { Text("مثال:\n104.16.1.1\n172.67.12.34\n(می‌توانید آی‌پی‌های اسکن شده در تب قبل را ارسال کنید)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPrimary,
                            unfocusedBorderColor = CyberBorder,
                            focusedLabelColor = CyberPrimary,
                            unfocusedLabelColor = CyberMuted
                        ),
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            textDirection = TextDirection.Ltr,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }

        // Output Generated list
        if (convertedConfigs.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val copyBlob = convertedConfigs.joinToString("\n")
                            clipboardManager.setText(AnnotatedString(copyBlob))
                            Toast.makeText(
                                context,
                                "تمامی ${convertedConfigs.size} کانفیگ تولید شده کپی شدند!",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberPrimary,
                            contentColor = CyberBgDeep
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy all")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "کپی همه (${convertedConfigs.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "کانفیگ‌های تولید شده:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary
                    )
                }
            }

            // Search config
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("جستجو در بین تولید شده ها...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberPrimary,
                        unfocusedBorderColor = CyberBorder
                    )
                )
            }

            val filteredList = if (searchQuery.isNotEmpty()) {
                convertedConfigs.filter { it.contains(searchQuery, ignoreCase = true) }
            } else {
                convertedConfigs
            }

            items(filteredList) { generatedUri ->
                GeneratedConfigRow(generatedUri)
            }
        } else {
            // Placeholder Conversion Status
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(CyberBorder),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Waiting for configuration parameters input",
                            tint = CyberMuted,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "منتظر ورود اطلاعات...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "یک کانقیک خام به همراه لیست آی‌پی به فرمهای بالا بدهید تا اتوماتیک خروجی بسازد.",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun GeneratedConfigRow(configUri: String) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val labelName = remember(configUri) {
        val hash = configUri.lastIndexOf('#')
        if (hash != -1) configUri.substring(hash + 1) else "Config File"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = CyberBgCard.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(configUri))
                        Toast.makeText(context, "کپی شد!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy single config",
                        tint = CyberPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = labelName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberBgDeep, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = configUri,
                    fontSize = 10.sp,
                    color = CyberMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Monospace,
                    style = TextStyle(textDirection = TextDirection.Ltr)
                )
            }
        }
    }
}

@Composable
fun BoxBorderBrush(): BorderStroke {
    return BorderStroke(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(CyberBorder, CyberPrimary.copy(alpha = 0.5f), CyberBorder)
        )
    )
}

@Composable
fun TelegramProxyTabScreen(viewModel: ScannerViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // State from ViewModel
    val scannedIps by viewModel.scannedIps.collectAsState()
    val tgProxyPort by viewModel.tgProxyPort.collectAsState()
    val tgProxySecret by viewModel.tgProxySecret.collectAsState()

    val tgSocksPort by viewModel.tgSocksPort.collectAsState()
    val tgSocksUser by viewModel.tgSocksUser.collectAsState()
    val tgSocksPass by viewModel.tgSocksPass.collectAsState()

    // Filter healthy IPs
    val healthyIps = remember(scannedIps) {
        scannedIps.filter { it.isSuccess && it.latency != null }
    }

    // Modern Tab indicator for proxy types: MTProto Node (0) vs SOCKS5 Node (1)
    var proxyTypeTab by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Description Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "مبدل خودکار آی‌پی‌های تمیز به پروکسی تلگرام",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "با اسکن آی‌پی‌های تمیز کلودفلر، می‌توانید آنها را مستقیماً به پروکسی‌های اختصاصی تلگرام (MTProto یا SOCKS5) تبدیل کرده و با یک کلیک تست و فعال کنید. این ترافیک از سرور میانی کلودفلر هدایت می‌شود تا فیلترینگ تلگرام با بالاترین سرعت برداشته شود.",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        textAlign = TextAlign.Right,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Switcher block for proxy styles
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CyberBgCard)
                    .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val weight = 1f
                // SOCKS5 Button
                Button(
                    onClick = { proxyTypeTab = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (proxyTypeTab == 1) CyberPrimary else Color.Transparent,
                        contentColor = if (proxyTypeTab == 1) Color.White else CyberMuted
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(weight).height(38.dp)
                ) {
                    Text("SOCKS5 پروکسی", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // MTProto Button
                Button(
                    onClick = { proxyTypeTab = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (proxyTypeTab == 0) CyberPrimary else Color.Transparent,
                        contentColor = if (proxyTypeTab == 0) Color.White else CyberMuted
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(weight).height(38.dp)
                ) {
                    Text("MTProto پروکسی", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Option fields
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberBgCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CyberBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "تنظیمات آدرس مقصد پروکسی",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (proxyTypeTab == 0) {
                        // MTProto Configurations
                        // Port
                        OutlinedTextField(
                            value = tgProxyPort.toString(),
                            onValueChange = {
                                val parsed = it.toIntOrNull() ?: 443
                                viewModel.setTgProxyPort(parsed)
                            },
                            label = { Text("پورت (Port)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(fontSize = 12.sp, textDirection = TextDirection.Ltr)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Secret
                        OutlinedTextField(
                            value = tgProxySecret,
                            onValueChange = { viewModel.setTgProxySecret(it) },
                            label = { Text("سکرت پروکسی (Secret)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder
                            ),
                            textStyle = TextStyle(fontSize = 11.sp, textDirection = TextDirection.Ltr, fontFamily = FontFamily.Monospace)
                        )
                    } else {
                        // SOCKS5 Configurations
                        // Port
                        OutlinedTextField(
                            value = tgSocksPort.toString(),
                            onValueChange = {
                                val parsed = it.toIntOrNull() ?: 1080
                                viewModel.setTgSocksPort(parsed)
                            },
                            label = { Text("پورت (Port)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(fontSize = 12.sp, textDirection = TextDirection.Ltr)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Username (Optional)
                        OutlinedTextField(
                            value = tgSocksUser,
                            onValueChange = { viewModel.setTgSocksUser(it) },
                            label = { Text("نام کاربری - اختیاری (Username)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder
                            ),
                            textStyle = TextStyle(fontSize = 12.sp, textDirection = TextDirection.Ltr)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Password (Optional)
                        OutlinedTextField(
                            value = tgSocksPass,
                            onValueChange = { viewModel.setTgSocksPass(it) },
                            label = { Text("رمز عبور - اختیاری (Password)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder
                            ),
                            textStyle = TextStyle(fontSize = 12.sp, textDirection = TextDirection.Ltr)
                        )
                    }
                }
            }
        }

        // IP source status & generator
        if (healthyIps.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val allLinks = healthyIps.joinToString("\n") { ipObj ->
                                if (proxyTypeTab == 0) {
                                    "https://t.me/proxy?server=${ipObj.ip}&port=$tgProxyPort&secret=$tgProxySecret"
                                } else {
                                    val creds = if (tgSocksUser.isNotEmpty()) "&user=$tgSocksUser&pass=$tgSocksPass" else ""
                                    "https://t.me/socks?server=${ipObj.ip}&port=$tgSocksPort$creds"
                                }
                            }
                            clipboardManager.setText(AnnotatedString(allLinks))
                            Toast.makeText(context, "تمامی پروکسی‌های تولید شده کپی شدند!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("کپی کل لیست پروکسی", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "لیست پروکسی‌های آماده اتصال (${healthyIps.size} آی‌پی سالم):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary
                    )
                }
            }

            items(healthyIps) { ipObj ->
                val proxyUrl = if (proxyTypeTab == 0) {
                    "tg://proxy?server=${ipObj.ip}&port=$tgProxyPort&secret=$tgProxySecret"
                } else {
                    val creds = if (tgSocksUser.isNotEmpty()) "&user=$tgSocksUser&pass=$tgSocksPass" else ""
                    "tg://socks?server=${ipObj.ip}&port=$tgSocksPort$creds"
                }

                val shareUrl = if (proxyTypeTab == 0) {
                    "https://t.me/proxy?server=${ipObj.ip}&port=$tgProxyPort&secret=$tgProxySecret"
                } else {
                    val creds = if (tgSocksUser.isNotEmpty()) "&user=$tgSocksUser&pass=$tgSocksPass" else ""
                    "https://t.me/socks?server=${ipObj.ip}&port=$tgSocksPort$creds"
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberBgCard.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${ipObj.latency}ms",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberSuccess
                            )

                            Text(
                                text = "سرور: ${ipObj.ip}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                style = TextStyle(textDirection = TextDirection.Ltr)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberBgDeep, RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = shareUrl,
                                fontSize = 9.sp,
                                color = CyberMuted,
                                maxLines = 1,
                                fontFamily = FontFamily.Monospace,
                                style = TextStyle(textDirection = TextDirection.Ltr),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Copy button
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(shareUrl))
                                    Toast.makeText(context, "لینک پروکسی کپی شد", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberBgDeep,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(36.dp),
                                border = BorderStroke(1.dp, CyberBorder)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy proxy",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("کپی لینک", fontSize = 10.sp)
                            }

                            // Connect button
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(proxyUrl))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Try standard share link if deep link fails
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl))
                                            context.startActivity(intent)
                                        } catch (ex: Exception) {
                                            Toast.makeText(context, "برنامه تلگرام روی دستگاه یافت نشد.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1.2f).height(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Open in Telegram",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("اتصال مستقیم", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // Empty state encouraging IP scanning
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(CyberPrimary.copy(alpha = 0.1f))
                            .border(1.dp, CyberPrimary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "هیچ آی‌پی یافت نشد",
                            tint = CyberPrimary,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "آی‌پی سالم اسکن نشده است",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "جهت ساخت اتوماتیک پروکسی، ابتدا باید در تب \"اسکنر آی‌پی\" دکمه شروع اسکن سالم‌ترین آی‌پی‌ها را بزنید تا اتوماتیک اینجا پروکسی‌ها فعال شوند.",
                        fontSize = 11.sp,
                        color = CyberMuted,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "لطفاً از تب بالا اسکنر آی‌پی را انتخاب کنید", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberPrimary.copy(alpha = 0.15f),
                            contentColor = CyberPrimary
                        ),
                        border = BorderStroke(1.dp, CyberPrimary.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("راهنمای گام به گام اسکن", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
