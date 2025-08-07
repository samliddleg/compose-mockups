package uk.co.lidbit.compose.mockups.plugin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import uk.co.lidbit.compose.mockups.annotations.DeviceId
import uk.co.lidbit.compose.mockups.annotations.Orientation

data class MockupEntry(
    val name: String,
    val devices: List<DeviceId>,
    val orientation: Orientation,
    val content: @Composable () -> Unit
)

data class MockupEntryWithPadding(
    val name: String,
    val devices: List<DeviceId>,
    val orientation: Orientation,
    val content: @Composable (innerPadding: PaddingValues) -> Unit
)