package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.BadgePill
import com.example.ui.theme.CloudBg
import com.example.ui.theme.CloudBorder
import com.example.ui.theme.CloudSurface
import com.example.ui.theme.CloudSurfaceHigh
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.RtxViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VkBlue
import com.example.viewmodel.CatalogUiState
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServerRigScreen(
    uiState: CatalogUiState,
    onSelectNode: (String) -> Unit,
    onRunLatencyTest: () -> Unit,
    onSelectResolution: (String) -> Unit,
    onSelectFps: (Int) -> Unit,
    onBitrateChange: (Int) -> Unit,
    onSelectCodec: (String) -> Unit,
    onToggleLowLatency: () -> Unit,
    onSaveCustomStream: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = uiState.streamSettings
    val rig = uiState.rigHardware
    var customUrlInput by remember(settings.customStreamBaseUrl) {
        mutableStateOf(settings.customStreamBaseUrl)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CloudBg)
            .testTag("server_rig_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Сервер и Поток 3D",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Выделенная конфигурация облачного узла и параметры минимальной задержки видеопотока.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Dedicated Hardware Rig Card
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = CloudSurface,
                border = BorderStroke(1.dp, VkBlue.copy(alpha = 0.55f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    VkBlue.copy(alpha = 0.14f),
                                    RtxViolet.copy(alpha = 0.08f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Конфигурация облачного рига",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        BadgePill("ACTIVE RIG", CyberEmerald.copy(alpha = 0.18f), CyberEmerald)
                    }

                    RigSpecRow(
                        icon = Icons.Filled.Memory,
                        label = stringResource(R.string.rig_cpu_title),
                        value = rig.cpuName,
                        subtext = rig.cpuDetails,
                        accent = VkBlue
                    )

                    RigSpecRow(
                        icon = Icons.Filled.GraphicEq,
                        label = stringResource(R.string.rig_gpu_title),
                        value = rig.gpuName,
                        subtext = rig.gpuDetails,
                        accent = CyberEmerald
                    )

                    RigSpecRow(
                        icon = Icons.Filled.Storage,
                        label = stringResource(R.string.rig_ram_title),
                        value = rig.ramDetails,
                        subtext = "DDR4-3600 Dual-Channel • NVMe Gen4 Кэш",
                        accent = RtxViolet
                    )
                }
            }
        }

        // Regional Node & Real Latency Tester
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = CloudSurface,
                border = BorderStroke(1.dp, CloudBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Dns, contentDescription = null, tint = VkBlue)
                        Text("Региональные узлы потока", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    }

                    uiState.serverNodes.forEach { node ->
                        val selected = node.id == settings.selectedNodeId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("node_${node.id}")
                                .clickable { onSelectNode(node.id) },
                            shape = MaterialTheme.shapes.medium,
                            color = if (selected) VkBlue.copy(alpha = 0.16f) else CloudSurfaceHigh,
                            border = BorderStroke(1.dp, if (selected) VkBlue else CloudBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(node.name, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                                    if (selected) {
                                        Icon(
                                            Icons.Filled.CheckCircle,
                                            contentDescription = null,
                                            tint = CyberEmerald,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${node.region} • Нагрузка GPU: ${node.loadPercent}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondary
                                )
                                LinearProgressIndicator(
                                    progress = { node.loadPercent / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = CyberEmerald,
                                    trackColor = CloudBorder
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = uiState.latencyCheck.statusText,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (uiState.latencyCheck.measuredMs != null) CyberEmerald else TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = onRunLatencyTest,
                            enabled = !uiState.latencyCheck.isChecking,
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("ping_test_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VkBlue, contentColor = TextPrimary)
                        ) {
                            if (uiState.latencyCheck.isChecking) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = TextPrimary
                                )
                            } else {
                                Icon(Icons.Filled.NetworkCheck, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Замер пинга", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }

        // Video Stream Quality & Low Latency Parameters
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = CloudSurface,
                border = BorderStroke(1.dp, CloudBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Speed, contentDescription = null, tint = CyberEmerald)
                        Text("Параметры видеопотока", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    }

                    Text("Разрешение трансляции", style = MaterialTheme.typography.titleSmall, color = TextSecondary)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("1080p", "1440p", "4K UHD").forEach { res ->
                            val selected = settings.resolution == res
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectResolution(res) },
                                label = { Text(res) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VkBlue,
                                    selectedLabelColor = TextPrimary,
                                    containerColor = CloudSurfaceHigh,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Text("Частота кадров (FPS)", style = MaterialTheme.typography.titleSmall, color = TextSecondary)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(60, 120, 144).forEach { fps ->
                            val selected = settings.targetFps == fps
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectFps(fps) },
                                label = { Text("$fps FPS") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VkBlue,
                                    selectedLabelColor = TextPrimary,
                                    containerColor = CloudSurfaceHigh,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Text("Видеокодек NVENC", style = MaterialTheme.typography.titleSmall, color = TextSecondary)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("AV1", "H.265 HEVC", "H.264").forEach { codec ->
                            val selected = settings.codec == codec
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectCodec(codec) },
                                label = { Text(codec) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RtxViolet,
                                    selectedLabelColor = TextPrimary,
                                    containerColor = CloudSurfaceHigh,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Битрейт потока", style = MaterialTheme.typography.titleSmall, color = TextSecondary)
                            Text("${settings.bitrateMbps} Мбит/с", style = MaterialTheme.typography.labelLarge, color = CyberEmerald)
                        }
                        Slider(
                            value = settings.bitrateMbps.toFloat(),
                            onValueChange = { onBitrateChange(it.roundToInt()) },
                            valueRange = 15f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = CyberEmerald,
                                activeTrackColor = VkBlue,
                                inactiveTrackColor = CloudBorder
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Filled.Bolt, contentDescription = null, tint = CyberEmerald, modifier = Modifier.size(16.dp))
                                Text("Ультра-низкая задержка", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                            }
                            Text(
                                "NVIDIA Reflex + буферизация 1 кадра",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = settings.lowLatencyMode,
                            onCheckedChange = { onToggleLowLatency() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextPrimary,
                                checkedTrackColor = CyberEmerald
                            )
                        )
                    }
                }
            }
        }

        // Custom Remote Cloud Stream Host (WebRTC / Sunshine / Moonlight Web)
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = CloudSurface,
                border = BorderStroke(1.dp, CloudBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Внешний облачный сервер (WebRTC / Браузер)",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "При желании укажите URL вашего собственного облачного хоста (например, Sunshine Web или WebRTC-шлюза). По умолчанию используется встроенный 3D WebGL движок.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    OutlinedTextField(
                        value = customUrlInput,
                        onValueChange = { customUrlInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://my-cloud-rig.example.com", color = TextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CloudSurfaceHigh,
                            unfocusedContainerColor = CloudSurfaceHigh,
                            focusedBorderColor = VkBlue,
                            unfocusedBorderColor = CloudBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Использовать внешний URL при запуске",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                        Switch(
                            checked = settings.useCustomStreamUrl,
                            onCheckedChange = { enabled ->
                                onSaveCustomStream(customUrlInput, enabled && customUrlInput.isNotBlank())
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextPrimary,
                                checkedTrackColor = VkBlue
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RigSpecRow(
    icon: ImageVector,
    label: String,
    value: String,
    subtext: String,
    accent: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accent.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Text(text = value, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
            Text(text = subtext, style = MaterialTheme.typography.labelMedium, color = CyberEmerald)
        }
    }
}
