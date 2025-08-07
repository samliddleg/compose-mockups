package uk.co.lidbit.compose.mockups.plugin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.graphics.asComposeCanvas
import androidx.compose.ui.scene.CanvasLayersComposeScene
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.BlendMode
import org.jetbrains.skia.Image
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Surface
import uk.co.lidbit.compose.mockups.annotations.Orientation
import java.io.File

@OptIn(InternalComposeUiApi::class)
fun takeScreenshot(
    path: String,
    width: Int,
    height: Int,
    density: Float = 1f,
    content: @Composable () -> Unit,
) {
    val scene =
        CanvasLayersComposeScene(
            size = IntSize(width, height),
            density = Density(density),
        )

    scene.setContent(content)

    val imageSurface = Surface.makeRasterN32Premul(width, height)
    val canvas = imageSurface.canvas.asComposeCanvas()

    scene.render(canvas, System.nanoTime())

    val image = imageSurface.makeImageSnapshot()
    val data = image.encodeToData() ?: error("Failed to encode image")
    File(path).apply {
        parentFile?.mkdirs()
    }.writeBytes(data.bytes)

    scene.close()
}

@OptIn(InternalComposeUiApi::class)
suspend fun takeScreenshot(
    path: String,
    device: Device,
    orientation: Orientation,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    if (!device.allowedOrientations.contains(orientation)) {
        throw RuntimeException("This device ($device) does not support this orientation ($orientation)")
    }
    val shouldRotate = device.defaultOrientation != orientation
    val width = when {
        !shouldRotate -> device.width
        else -> device.height
    }
    val height = when {
        !shouldRotate -> device.height
        else -> device.width
    }
    val contentWidth = when {
        !shouldRotate -> device.frame?.contentWidth ?: device.width
        else -> device.frame?.contentHeight ?: device.height
    }
    val contentHeight = when {
        !shouldRotate -> device.frame?.contentHeight ?: device.height
        else -> device.frame?.contentWidth ?: device.width
    }
    val scene =
        CanvasLayersComposeScene(
            size = IntSize(contentWidth, contentHeight),
            density = Density(device.density),
        )

    scene.setContent {
        val innerPadding = device.frame?.innerPadding ?: Rect(0f, 0f, 0f, 0f)
        val density = device.density
        val paddingValues = if (shouldRotate) {
            PaddingValues(
                top = innerPadding.left.dp / density,
                end = innerPadding.top.dp / density,
                bottom = innerPadding.right.dp / density,
                start = innerPadding.bottom.dp / density,
            )
        } else {
            PaddingValues(
                top = innerPadding.top.dp / density,
                end = innerPadding.right.dp / density,
                bottom = innerPadding.bottom.dp / density,
                start = innerPadding.left.dp / density,
            )
        }
        content(paddingValues)
    }

    val imageSurface = Surface.makeRasterN32Premul(contentWidth, contentHeight)
    val canvas = imageSurface.canvas.asComposeCanvas()

    scene.render(canvas, System.nanoTime())

    val finalSurface = Surface.makeRasterN32Premul(
        width = width,
        height = height,
    )
    val finalCanvas = finalSurface.canvas

    val image = imageSurface.makeImageSnapshot()

    finalCanvas.drawImage(
        image,
        device.frame?.offsetX?.toFloat() ?: 0f,
        device.frame?.offsetY?.toFloat() ?: 0f
    )

    finalCanvas.save()
    if (shouldRotate) {
        finalCanvas.translate(width.toFloat(), 0f)
        finalCanvas.rotate(90f)
    }

    device.frame?.let { frame ->
        frame.maskPath?.let { maskPath ->
            val maskBytes = getImageBytes(maskPath)
//            val maskBytes = Res.readBytes("img/$maskPath.png")
            val maskImage = Image.makeFromEncoded(maskBytes)
            val paint = Paint().apply {
                blendMode = BlendMode.DST_IN
                isAntiAlias = true
            }
            finalCanvas.drawImage(maskImage, 0f, 0f, paint)
        }
        val frameBytes = getImageBytes(frame.framePath)
        val frameImage = Image.makeFromEncoded(frameBytes)
        finalCanvas.drawImage(frameImage, 0f, 0f)
    }

    finalCanvas.restore()

    val finalImage = finalSurface.makeImageSnapshot()
    val data = finalImage.encodeToData() ?: error("Failed to encode image")
    File(path).apply {
        parentFile?.mkdirs()
    }.writeBytes(data.bytes)

    scene.close()
}

private fun getImageBytes(fileName: String): ByteArray =
    ComposeMockupsPlugin::class.java.getResource("/img/$fileName.png")?.readBytes()
        ?: error("Image not found")