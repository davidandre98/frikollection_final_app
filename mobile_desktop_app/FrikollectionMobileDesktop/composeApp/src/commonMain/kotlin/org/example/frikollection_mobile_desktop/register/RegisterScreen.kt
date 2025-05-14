package org.example.frikollection_mobile_desktop.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
expect fun RegisterScreen(
    viewModel: RegisterViewModel = remember { RegisterViewModel() },
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
)