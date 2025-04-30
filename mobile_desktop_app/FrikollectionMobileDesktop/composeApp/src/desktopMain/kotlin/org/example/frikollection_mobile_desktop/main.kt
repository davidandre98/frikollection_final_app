package org.example.frikollection_mobile_desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FrikollectionMobileDesktop",
    ) {
        App()
    }
}