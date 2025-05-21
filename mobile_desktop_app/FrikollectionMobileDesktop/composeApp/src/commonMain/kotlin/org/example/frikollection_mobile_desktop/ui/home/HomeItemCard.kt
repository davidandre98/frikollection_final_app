package org.example.frikollection_mobile_desktop.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.ui.ServerImage

@Composable
fun HomeItemCard(
    imageUrl: String,
    title: String,
    subtitle: String,
    imageSize: Dp = 48.dp,
    onClick: () -> Unit
) {
    val isDefaultLogo = imageUrl.contains("logo_default.png")
    val finalImageSize = if (isDefaultLogo) imageSize * 1.5f else imageSize

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 8.dp)
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ServerImage(
                imageUrl = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .size(finalImageSize)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2.copy(fontSize = 12.sp)
                )
            }
        }
    }
}