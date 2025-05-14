package org.example.frikollection_mobile_desktop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image as SkiaImage
import java.net.URL
import javax.imageio.ImageIO

@Composable
actual fun ServerImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        imageBitmap = loadImageFromUrl(imageUrl)
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = contentDescription,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.background(Color.LightGray, RoundedCornerShape(4.dp))
        ) {
            Text(
                "No image",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

suspend fun loadImageFromUrl(url: String?): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        try {
            if (url != null) {
                val connection = URL(url).openStream()
                val bufferedImage = ImageIO.read(connection)
                bufferedImage?.let {
                    SkiaImage.makeFromEncoded(it.toByteArray()).asImageBitmap()
                }
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error loading image: ${e.message}")
            null
        }
    }
}

// Extensió per convertir BufferedImage -> ByteArray
fun java.awt.image.BufferedImage.toByteArray(): ByteArray {
    val outputStream = java.io.ByteArrayOutputStream()
    ImageIO.write(this, "png", outputStream)
    return outputStream.toByteArray()
}