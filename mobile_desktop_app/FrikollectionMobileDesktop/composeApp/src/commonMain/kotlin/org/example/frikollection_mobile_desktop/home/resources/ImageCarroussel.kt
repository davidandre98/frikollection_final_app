package org.example.frikollection_mobile_desktop.home.resources

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.frikollection_mobile_desktop.ui.ServerImage

@Composable
fun ImageCarousel(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
) {
    var currentIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrls.isNotEmpty()) {
            // Imatge principal
            ServerImage(
                imageUrl = imageUrls[currentIndex],
                contentDescription = "Carroussel image",
                modifier = imageModifier
            )

            // Fletxa esquerra
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Previous",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(8.dp)
                    .clickable {
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else imageUrls.lastIndex
                    },
                tint = MaterialTheme.colors.onSurface
            )

            // Fletxa dreta
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Next",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(8.dp)
                    .clickable {
                        currentIndex = if (currentIndex < imageUrls.lastIndex) currentIndex + 1 else 0
                    },
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}