package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.ui.ServerImage

@Composable
fun HomeItemCard(
    imageUrl: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ServerImage(
            imageUrl = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .size(48.dp)
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