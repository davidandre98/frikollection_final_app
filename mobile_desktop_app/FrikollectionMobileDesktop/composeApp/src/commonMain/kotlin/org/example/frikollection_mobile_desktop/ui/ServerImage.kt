package org.example.frikollection_mobile_desktop.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ServerImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier
)