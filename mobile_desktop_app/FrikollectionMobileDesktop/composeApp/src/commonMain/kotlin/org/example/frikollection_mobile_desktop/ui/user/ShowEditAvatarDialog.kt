package org.example.frikollection_mobile_desktop.ui.user

import androidx.compose.runtime.Composable
import org.example.frikollection_mobile_desktop.account.AccountViewModel

@Composable
expect fun ShowEditAvatarDialog(
    onDismiss: () -> Unit,
    onAvatarSelected: (fileBytes: ByteArray, fileName: String) -> Unit
)