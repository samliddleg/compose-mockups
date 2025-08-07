package uk.co.lidbit.compose.mockups.plugin

import org.jetbrains.skia.Rect
import uk.co.lidbit.compose.mockups.annotations.DeviceId
import uk.co.lidbit.compose.mockups.annotations.Orientation

private fun makeRect(top: Int = 0, left: Int = 0, right: Int = 0, bottom: Int = 0) = Rect(
    left = left.toFloat(),
    top = top.toFloat(),
    right = right.toFloat(),
    bottom = bottom.toFloat(),
)

open class Device(
    val frame: DeviceFrame?,
    val width: Int,
    val height: Int,
    val density: Float,
    val defaultOrientation: Orientation,
    val allowedOrientations: Set<Orientation>,
) {
    companion object {
        val ALL = DeviceId.values().map(::resolve)
        fun resolve(deviceId: DeviceId): Device = when (deviceId) {
            DeviceId.IPHONE16_PINK -> IPhone16.Pink
            DeviceId.IPHONE16_WHITE -> IPhone16.White
            DeviceId.IPHONE16_BLACK -> IPhone16.Black
            DeviceId.IPHONE16_TEAL -> IPhone16.Teal
            DeviceId.IPHONE16_ULTRAMARINE -> IPhone16.UltraMarine

            DeviceId.IPHONE16PLUS_PINK -> IPhone16Plus.Pink
            DeviceId.IPHONE16PLUS_WHITE -> IPhone16Plus.White
            DeviceId.IPHONE16PLUS_BLACK -> IPhone16Plus.Black
            DeviceId.IPHONE16PLUS_TEAL -> IPhone16Plus.Teal
            DeviceId.IPHONE16PLUS_ULTRAMARINE -> IPhone16Plus.UltraMarine

            DeviceId.IPHONE16PRO_NATURAL_TITANIUM -> IPhone16Pro.NaturalTitanium
            DeviceId.IPHONE16PRO_DESERT_TITANIUM -> IPhone16Pro.DesertTitanium
            DeviceId.IPHONE16PRO_WHITE_TITANIUM -> IPhone16Pro.WhiteTitanium
            DeviceId.IPHONE16PRO_BLACK_TITANIUM -> IPhone16Pro.BlackTitanium

            DeviceId.IPHONE16PRO_MAX_NATURAL_TITANIUM -> IPhone16ProMax.NaturalTitanium
            DeviceId.IPHONE16PRO_MAX_DESERT_TITANIUM -> IPhone16ProMax.DesertTitanium
            DeviceId.IPHONE16PRO_MAX_WHITE_TITANIUM -> IPhone16ProMax.WhiteTitanium
            DeviceId.IPHONE16PRO_MAX_BLACK_TITANIUM -> IPhone16ProMax.BlackTitanium

            DeviceId.ANDROID_COMPACT_BLACK -> AndroidCompact.Black
            DeviceId.ANDROID_COMPACT_SILVER -> AndroidCompact.Silver

            DeviceId.ANDROID_MEDIUM_BLACK -> AndroidMedium.Black
            DeviceId.ANDROID_MEDIUM_SILVER -> AndroidMedium.Silver

            DeviceId.ANDROID_EXPANDED_BLACK -> AndroidExpanded.Black
            DeviceId.ANDROID_EXPANDED_SILVER -> AndroidExpanded.Silver

            DeviceId.POS_TABLET -> PosTablet

            DeviceId.MACBOOK_PRO_13_SILVER -> MacbookPro13.Silver
            DeviceId.MACBOOK_PRO_13_SPACE_GREY -> MacbookPro13.SpaceGrey
            DeviceId.MACBOOK_PRO_16_SILVER -> MacbookPro16.Silver
            DeviceId.MACBOOK_PRO_16_SPACE_GREY -> MacbookPro16.SpaceGrey

            DeviceId.IMAC_PRO -> IMacPro
        }
    }


    sealed class AndroidCompact(color: String) : Device(
        frame = DeviceFrame(
            name = "android_compact_$color",
            maskPath = "android_compact_mask",
            offsetX = 45,
            offsetY = 45,
            contentWidth = 1236,
            contentHeight = 2751,
            innerPadding = makeRect(top = 159),
        ),
        width = 1335,
        height = 2841,
        density = 3f,
        defaultOrientation = Orientation.PORTRAIT,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object Black : AndroidCompact(color = "black")
        object Silver : AndroidCompact(color = "silver")
    }


    sealed class AndroidMedium(color: String) : Device(
        frame = DeviceFrame(
            name = "android_medium_$color",
            maskPath = "android_medium_mask",
            offsetX = 75,
            offsetY = 75,
            contentWidth = 2100,
            contentHeight = 2520,
            innerPadding = makeRect(),
        ),
        width = 2259,
        height = 2670,
        density = 3f,
        defaultOrientation = Orientation.PORTRAIT,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object Black : AndroidMedium(color = "black")
        object Silver : AndroidMedium(color = "silver")
    }

    sealed class AndroidExpanded(color: String) : Device(
        frame = DeviceFrame(
            name = "android_expanded_$color",
            maskPath = "android_expanded_mask",
            offsetX = 135,
            offsetY = 135,
            contentWidth = 3840,
            contentHeight = 2400,
            innerPadding = makeRect(),
        ),
        width = 4110,
        height = 2670,
        density = 3f,
        defaultOrientation = Orientation.LANDSCAPE,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object Black : AndroidExpanded(color = "black")
        object Silver : AndroidExpanded(color = "silver")
    }

    sealed class IPhone16(color: String) : Device(
        frame = DeviceFrame(
            name = "iphone_16_$color",
            maskPath = "iphone_16_mask",
            offsetX = 67,
            offsetY = 59,
            contentWidth = 1179,
            contentHeight = 2556,
            innerPadding = makeRect(top = 164),
        ),
        width = 1313,
        height = 2674,
        density = 3f,
        defaultOrientation = Orientation.PORTRAIT,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object Pink : IPhone16(color = "pink")
        object White : IPhone16(color = "white")
        object Black : IPhone16(color = "black")
        object Teal : IPhone16(color = "teal")
        object UltraMarine : IPhone16(color = "ultramarine")
    }

    sealed class IPhone16Plus(color: String) : Device(
        frame = DeviceFrame(
            name = "iphone_16_plus_$color",
            maskPath = "iphone_16_plus_mask",
            offsetX = 67,
            offsetY = 59,
            contentWidth = 1290,
            contentHeight = 2796,
            innerPadding = makeRect(top = 186),
        ),
        width = 1424,
        height = 2914,
        density = 3f,
        defaultOrientation = Orientation.PORTRAIT,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object Pink : IPhone16Plus(color = "pink")
        object White : IPhone16Plus(color = "white")
        object Black : IPhone16Plus(color = "black")
        object Teal : IPhone16Plus(color = "teal")
        object UltraMarine : IPhone16Plus(color = "ultramarine")
    }

    sealed class IPhone16Pro(color: String) : Device(
        frame = DeviceFrame(
            name = "iphone_16_pro_$color",
            maskPath = "iphone_16_pro_mask",
            offsetX = 52,
            offsetY = 44,
            contentWidth = 1206,
            contentHeight = 2622,
            innerPadding = makeRect(top = 200),
        ),
        width = 1310,
        height = 2710,
        density = 3f,
        defaultOrientation = Orientation.PORTRAIT,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object NaturalTitanium : IPhone16Pro(color = "natural_titanium")
        object DesertTitanium : IPhone16Pro(color = "desert_titanium")
        object WhiteTitanium : IPhone16Pro(color = "white_titanium")
        object BlackTitanium : IPhone16Pro(color = "black_titanium")
    }

    sealed class IPhone16ProMax(color: String) : Device(
        frame = DeviceFrame(
            name = "iphone_16_pro_max_$color",
            maskPath = "iphone_16_pro_max_mask",
            offsetX = 52,
            offsetY = 44,
            contentWidth = 1320,
            contentHeight = 2868,
            innerPadding = makeRect(top = 171),
        ),
        width = 1424,
        height = 2956,
        density = 3f,
        defaultOrientation = Orientation.PORTRAIT,
        allowedOrientations = setOf(Orientation.PORTRAIT, Orientation.LANDSCAPE),
    ) {
        object NaturalTitanium : IPhone16ProMax(color = "natural_titanium")
        object DesertTitanium : IPhone16ProMax(color = "desert_titanium")
        object WhiteTitanium : IPhone16ProMax(color = "white_titanium")
        object BlackTitanium : IPhone16ProMax(color = "black_titanium")
    }

    object PosTablet : Device(
        frame = DeviceFrame(
            name = "pos_tablet",
            offsetX = 21,
            offsetY = 21,
            contentWidth = 581,
            contentHeight = 363,
            innerPadding = makeRect(),
        ),
        width = 623,
        height = 585,
        density = 1f,
        defaultOrientation = Orientation.LANDSCAPE,
        allowedOrientations = setOf(Orientation.LANDSCAPE),
    )

    sealed class MacbookPro13(color: String) : Device(
        frame = DeviceFrame(
            name = "macbook_pro_13_$color",
            maskPath = null,
            offsetX = 395,
            offsetY = 142,
            contentWidth = 2562,
            contentHeight = 1602,
            innerPadding = makeRect(),
        ),
        width = 3352,
        height = 1974,
        density = 2f,
        defaultOrientation = Orientation.LANDSCAPE,
        allowedOrientations = setOf(Orientation.LANDSCAPE),
    ) {
        object Silver : MacbookPro13(color = "silver")
        object SpaceGrey : MacbookPro13(color = "space_grey")
    }

    sealed class MacbookPro16(color: String) : Device(
        frame = DeviceFrame(
            name = "macbook_pro_16_$color",
            maskPath = null,
            offsetX = 420,
            offsetY = 97,
            contentWidth = 3074,
            contentHeight = 1922,
            innerPadding = makeRect(),
        ),
        width = 3914,
        height = 2241,
        density = 2f,
        defaultOrientation = Orientation.LANDSCAPE,
        allowedOrientations = setOf(Orientation.LANDSCAPE),
    ) {
        object Silver : MacbookPro16(color = "silver")
        object SpaceGrey : MacbookPro16(color = "space_grey")
    }

    object IMacPro : Device(
        frame = DeviceFrame(
            name = "imac_pro",
            offsetX = 227,
            offsetY = 240,
            contentWidth = 5122,
            contentHeight = 2882,
            innerPadding = makeRect(),
            maskPath = null,
        ),
        width = 5576,
        height = 4610,
        density = 2f,
        defaultOrientation = Orientation.LANDSCAPE,
        allowedOrientations = setOf(Orientation.LANDSCAPE),
    )

}