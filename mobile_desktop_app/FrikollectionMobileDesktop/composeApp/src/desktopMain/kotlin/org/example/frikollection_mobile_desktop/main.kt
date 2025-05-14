package org.example.frikollection_mobile_desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.frikollection_mobile_desktop.home.HomeScreen
import org.example.frikollection_mobile_desktop.home.ProductDetailScreen
import org.example.frikollection_mobile_desktop.home.ProductListScreen
import org.example.frikollection_mobile_desktop.login.LoginScreen
import org.example.frikollection_mobile_desktop.register.RegisterScreen

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Main : Screen()
    data class ProductList(val type: String?, val status: String?) : Screen()
    data class ProductDetail(val productId: String, val type: String?, val status: String?) : Screen()
}

fun main() = application {
    val windowState = rememberWindowState(
        width = 500.dp,
        height = 700.dp,
        position = WindowPosition.Aligned(Alignment.Center)
    )

    val currentScreenState = remember { mutableStateOf<Screen>(Screen.Login) }
    val currentScreen = currentScreenState.value

    // Ajustar mides i centrar finestra al canviar de pantalles
    LaunchedEffect(currentScreen) {
        when (currentScreen) {
            Screen.Login -> {
                windowState.size = DpSize(500.dp, 700.dp)
                windowState.position = WindowPosition.Aligned(Alignment.Center)
            }
            Screen.Register -> {
                windowState.size = DpSize(640.dp, 1020.dp)
                windowState.position = WindowPosition.Aligned(Alignment.Center)
            }
            is Screen.Main, is Screen.ProductList, is Screen.ProductDetail -> {
                if (windowState.size != DpSize(720.dp, 1080.dp)) {
                    windowState.size = DpSize(720.dp, 1080.dp)
                    windowState.position = WindowPosition.Aligned(Alignment.Center)
                }
            }
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Desktop App"
    ) {
        when (currentScreen) {
            Screen.Login -> LoginScreen(
                onLoginSuccess = {
                    currentScreenState.value = Screen.Main
                },
                onNavigateToRegister = {
                    currentScreenState.value = Screen.Register
                }
            )

            Screen.Register -> RegisterScreen(
                onNavigateToLogin = {
                    currentScreenState.value = Screen.Login
                },
                onRegisterSuccess = {
                    currentScreenState.value = Screen.Login
                }
            )

            Screen.Main -> HomeScreen(
                onNavigateToList = { type, status ->
                    currentScreenState.value = Screen.ProductList(type, status)
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = {
                    currentScreenState.value = Screen.Main
                }
            )

            is Screen.ProductList -> ProductListScreen(
                type = currentScreen.type,
                status = currentScreen.status,
                onBack = {
                    currentScreenState.value = Screen.Main
                },
                onNavigateToFilterScreen = {
                    // Acció per obrir pantalla de filtres
                },
                onProductClick = { productId ->
                    currentScreenState.value = Screen.ProductDetail(
                        productId = productId,
                        type = currentScreen.type,
                        status = currentScreen.status
                    )
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = { selected ->
                    // De moment, si seleccionen una altra pestanya, tornem a HomeScreen
                    currentScreenState.value = Screen.Main
                }
            )

            is Screen.ProductDetail -> ProductDetailScreen(
                productId = currentScreen.productId,
                onBack = {
                    currentScreenState.value = Screen.ProductList(
                        type = currentScreen.type,
                        status = currentScreen.status
                    )
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = {
                    currentScreenState.value = Screen.Main
                }
            )
        }
    }
}