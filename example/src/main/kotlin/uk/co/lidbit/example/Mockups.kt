package uk.co.lidbit.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.co.lidbit.compose.mockups.annotations.ComposeMockup
import uk.co.lidbit.compose.mockups.annotations.DeviceId
import uk.co.lidbit.compose.mockups.annotations.Orientation

@ComposeMockup(
    devices = [
        DeviceId.IPHONE16_PINK,
        DeviceId.IPHONE16_WHITE,
        DeviceId.IPHONE16_BLACK,
        DeviceId.IPHONE16_TEAL,
        DeviceId.IPHONE16_ULTRAMARINE,
        DeviceId.IPHONE16PLUS_PINK,
        DeviceId.IPHONE16PLUS_WHITE,
        DeviceId.IPHONE16PLUS_BLACK,
        DeviceId.IPHONE16PLUS_TEAL,
        DeviceId.IPHONE16PLUS_ULTRAMARINE,
        DeviceId.IPHONE16PRO_NATURAL_TITANIUM,
        DeviceId.IPHONE16PRO_DESERT_TITANIUM,
        DeviceId.IPHONE16PRO_WHITE_TITANIUM,
        DeviceId.IPHONE16PRO_BLACK_TITANIUM,
        DeviceId.IPHONE16PRO_MAX_NATURAL_TITANIUM,
        DeviceId.IPHONE16PRO_MAX_DESERT_TITANIUM,
        DeviceId.IPHONE16PRO_MAX_WHITE_TITANIUM,
        DeviceId.IPHONE16PRO_MAX_BLACK_TITANIUM,
        DeviceId.ANDROID_COMPACT_BLACK,
        DeviceId.ANDROID_COMPACT_SILVER,
        DeviceId.ANDROID_MEDIUM_BLACK,
        DeviceId.ANDROID_MEDIUM_SILVER,
        DeviceId.ANDROID_EXPANDED_BLACK,
        DeviceId.ANDROID_EXPANDED_SILVER,
        DeviceId.POS_TABLET,
        DeviceId.MACBOOK_PRO_13_SILVER,
        DeviceId.MACBOOK_PRO_13_SPACE_GREY,
        DeviceId.MACBOOK_PRO_16_SILVER,
        DeviceId.MACBOOK_PRO_16_SPACE_GREY,
        DeviceId.IMAC_PRO,
    ],
    orientations = [Orientation.PORTRAIT, Orientation.LANDSCAPE],
)
annotation class AllDevicesMockup

@ComposeMockup(
    devices = [
        DeviceId.MACBOOK_PRO_13_SILVER,
        DeviceId.MACBOOK_PRO_13_SPACE_GREY,
        DeviceId.MACBOOK_PRO_16_SILVER,
        DeviceId.MACBOOK_PRO_16_SPACE_GREY,
    ],
    orientations = [Orientation.LANDSCAPE],
)
annotation class MacbookMockups

@MacbookMockups
@Composable
fun MacbookExample() {
    // Content here
}

@ComposeMockup(
    devices = [DeviceId.ANDROID_COMPACT_BLACK],
    orientations = [Orientation.LANDSCAPE],
)
@Composable
fun NoSafeAreaExample() {
    App()
}

@ComposeMockup(
    devices = [DeviceId.POS_TABLET, DeviceId.MACBOOK_PRO_16_SPACE_GREY, DeviceId.MACBOOK_PRO_13_SILVER],
)
@Composable
fun SafeAreaExample(innerPadding: PaddingValues) {
    App(innerPadding)
}

@AllDevicesMockup
@Composable
fun DevicePresetExample(innerPadding: PaddingValues) {
    App(innerPadding)
}

@ComposeMockup(
    devices = [DeviceId.IPHONE16PLUS_ULTRAMARINE, DeviceId.MACBOOK_PRO_13_SPACE_GREY],
    orientations = [Orientation.PORTRAIT, Orientation.LANDSCAPE],
)
@Composable
fun ExamplePreview(innerPadding: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(innerPadding)
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to Compose Mockups",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
