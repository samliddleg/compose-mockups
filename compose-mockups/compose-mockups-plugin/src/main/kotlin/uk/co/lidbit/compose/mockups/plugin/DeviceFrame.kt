package uk.co.lidbit.compose.mockups.plugin

import org.jetbrains.skia.Rect

data class DeviceFrame(
    val name: String,
    val offsetX: Int,
    val offsetY: Int,
    val contentWidth: Int,
    val contentHeight: Int,
    val innerPadding: Rect,
    val framePath: String = "${name}_frame",
    val maskPath: String? = "${name}_mask",
)