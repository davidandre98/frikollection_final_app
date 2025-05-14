package org.example.frikollection_mobile_desktop

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import org.example.frikollection_mobile_desktop.home.HomeScreen
import org.example.frikollection_mobile_desktop.home.ProductListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.frikollection_mobile_desktop.login.LoginScreen
import org.example.frikollection_mobile_desktop.register.RegisterScreen
import org.example.frikollection_mobile_desktop.state.AppState
import org.example.frikollection_mobile_desktop.state.AuthState

@Composable
@Preview
fun App() {
    val authState by AppState.authState.collectAsState()
    var selectedScreen by remember { mutableStateOf(BottomMenuItem.Home) }
    var showRegister by remember { mutableStateOf(false) }
    var showProductList by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        when (authState) {
            is AuthState.Authenticated -> {
                if (showProductList) {
                    ProductListScreen(
                        type = selectedType,
                        status = selectedStatus,
                        onBack = {
                            showProductList = false
                            selectedType = null
                            selectedStatus = null
                        },
                        onNavigateToFilterScreen = {
                            // Codi per anar a ProductFilterScreen
                        },
                        selectedBottomItem = selectedScreen,
                        onBottomItemSelected = { selectedScreen = it },
                        onProductClick = { /* Codi per anar a ProductDetailScreen */ }
                    )
                } else {
                    BottomNavigationApp(
                        selectedScreen = selectedScreen,
                        onScreenSelected = { selectedScreen = it },
                        onNavigateToList = { type, status ->
                            selectedType = type
                            selectedStatus = status
                            showProductList = true
                        }
                    )
                }
            }

            AuthState.Unauthenticated -> {
                if (showRegister) {
                    RegisterScreen(
                        onNavigateToLogin = {
                            showRegister = false
                        },
                        onRegisterSuccess = {
                            showRegister = false
                        }
                    )
                } else {
                    LoginScreen(
                        onNavigateToRegister = {
                            showRegister = true
                        },
                        onLoginSuccess = {
                            selectedScreen = BottomMenuItem.Home
                        }
                    )
                }
            }
        }
    }
}

enum class BottomMenuItem {
    Home, Discover, Search, Lists, Account
}

@Composable
fun BottomNavigationApp(
    selectedScreen: BottomMenuItem,
    onScreenSelected: (BottomMenuItem) -> Unit,
    onNavigateToList: (String, String?) -> Unit
) {
    when (selectedScreen) {
        BottomMenuItem.Home -> HomeScreen(
            onNavigateToList = onNavigateToList,
            selectedBottomItem = selectedScreen,
            onBottomItemSelected = onScreenSelected
        )
        BottomMenuItem.Discover -> DiscoverScreen()
        BottomMenuItem.Search -> SearchScreen()
        BottomMenuItem.Lists -> ListsScreen()
        BottomMenuItem.Account -> AccountScreen()
    }

    BottomNavigation {
        BottomNavigationItem(
            selected = selectedScreen == BottomMenuItem.Home,
            onClick = { onScreenSelected(BottomMenuItem.Home) },
            label = { Text("Home") },
            icon = { Icons.Outlined.Home }
        )
        BottomNavigationItem(
            selected = selectedScreen == BottomMenuItem.Discover,
            onClick = { onScreenSelected(BottomMenuItem.Discover) },
            label = { Text("Discover") },
            icon = {}
        )
        BottomNavigationItem(
            selected = selectedScreen == BottomMenuItem.Search,
            onClick = { onScreenSelected(BottomMenuItem.Search) },
            label = { Text("Search") },
            icon = { Icons.Outlined.Search }
        )
        BottomNavigationItem(
            selected = selectedScreen == BottomMenuItem.Lists,
            onClick = { onScreenSelected(BottomMenuItem.Lists) },
            label = { Text("Lists") },
            icon = { Icons.Outlined.List }
        )
        BottomNavigationItem(
            selected = selectedScreen == BottomMenuItem.Account,
            onClick = { onScreenSelected(BottomMenuItem.Account) },
            label = { Text("Account") },
            icon = { Icons.Outlined.AccountCircle }
        )
    }
}

@Composable
fun AccountScreen() {
    TODO("Not yet implemented")
}

@Composable
fun ListsScreen() {
    TODO("Not yet implemented")
}

@Composable
fun SearchScreen() {
    TODO("Not yet implemented")
}

@Composable
fun DiscoverScreen() {
    TODO("Not yet implemented")
}
