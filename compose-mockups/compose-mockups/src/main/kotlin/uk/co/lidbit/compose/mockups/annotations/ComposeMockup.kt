package uk.co.lidbit.compose.mockups.annotations

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ComposeMockup(
    val devices: Array<DeviceId> = [],
    val orientations: Array<Orientation> = [Orientation.PORTRAIT, Orientation.LANDSCAPE],
)

/**
 * If BOTH is used then the devices will do all supported orientations.
 */
enum class Orientation {
    PORTRAIT,
    LANDSCAPE,
}

enum class DeviceId {
    IPHONE16_PINK,
    IPHONE16_WHITE,
    IPHONE16_BLACK,
    IPHONE16_TEAL,
    IPHONE16_ULTRAMARINE,

    IPHONE16PLUS_PINK,
    IPHONE16PLUS_WHITE,
    IPHONE16PLUS_BLACK,
    IPHONE16PLUS_TEAL,
    IPHONE16PLUS_ULTRAMARINE,

    IPHONE16PRO_NATURAL_TITANIUM,
    IPHONE16PRO_DESERT_TITANIUM,
    IPHONE16PRO_WHITE_TITANIUM,
    IPHONE16PRO_BLACK_TITANIUM,

    IPHONE16PRO_MAX_NATURAL_TITANIUM,
    IPHONE16PRO_MAX_DESERT_TITANIUM,
    IPHONE16PRO_MAX_WHITE_TITANIUM,
    IPHONE16PRO_MAX_BLACK_TITANIUM,

    ANDROID_COMPACT_BLACK,
    ANDROID_COMPACT_SILVER,

    ANDROID_MEDIUM_BLACK,
    ANDROID_MEDIUM_SILVER,

    ANDROID_EXPANDED_BLACK,
    ANDROID_EXPANDED_SILVER,

    POS_TABLET,

    MACBOOK_PRO_13_SILVER,
    MACBOOK_PRO_13_SPACE_GREY,
    MACBOOK_PRO_16_SILVER,
    MACBOOK_PRO_16_SPACE_GREY,

    IMAC_PRO,
}

data class MockupInfo(
    val devices: Set<DeviceId>,
    val orientations: Set<Orientation>,
)