package org.example.frikollection_mobile_desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.example.frikollection_mobile_desktop.account.AccountScreen
import org.example.frikollection_mobile_desktop.account.AccountViewModel
import org.example.frikollection_mobile_desktop.account.NotificationViewModel
import org.example.frikollection_mobile_desktop.collection.CollectionDetailScreen
import org.example.frikollection_mobile_desktop.collection.CollectionManageScreen
import org.example.frikollection_mobile_desktop.collection.CollectionScreen
import org.example.frikollection_mobile_desktop.collection.CollectionViewModel
import org.example.frikollection_mobile_desktop.discover.DiscoverScreen
import org.example.frikollection_mobile_desktop.discover.DiscoverViewModel
import org.example.frikollection_mobile_desktop.filter.FilterOptionsCache
import org.example.frikollection_mobile_desktop.home.HomeScreen
import org.example.frikollection_mobile_desktop.home.HomeViewModel
import org.example.frikollection_mobile_desktop.home.ProductDetailScreen
import org.example.frikollection_mobile_desktop.home.ProductListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.frikollection_mobile_desktop.login.LoginScreen
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.models.ProductListState
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.register.RegisterScreen
import org.example.frikollection_mobile_desktop.search.SearchScreen
import org.example.frikollection_mobile_desktop.search.SearchViewModel
import org.example.frikollection_mobile_desktop.state.AppState
import org.example.frikollection_mobile_desktop.state.AuthState
import org.example.frikollection_mobile_desktop.ui.filter.FilterScreen
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.utils.toScreen

enum class BottomMenuItem {
    Home, Discover, Search, Lists, Account
}

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
    object Discover : Screen()
    object Search : Screen()
    object Collection : Screen()
    object Account : Screen()
    object CollectionDetail : Screen()
    data class ProductList(val type: String?, val status: String?, val filter: ProductFilter = ProductFilter()) : Screen()
    data class ProductDetail(val productId: String, val type: String?, val status: String?) : Screen()
    data class Filter(val initialFilter: ProductFilter, val originalType: String?, val originalStatus: String?) : Screen()
    data class CollectionManage(val isAddMode: Boolean) : Screen()
}

@Composable
@Preview
fun App() {
    val authState by AppState.authState.collectAsState()
    val homeViewModel = remember { HomeViewModel() }
    val searchViewModel = remember { SearchViewModel() }
    val collectionViewModel = remember { CollectionViewModel().apply { loadCollections() } }
    val accountViewModel = remember { AccountViewModel() }
    val notificationViewModel = remember { NotificationViewModel() }
    val discoverViewModel = remember { DiscoverViewModel() }

    val currentScreen = remember { mutableStateOf<Screen>(Screen.Login) }
    val productListState = remember { mutableStateOf(ProductListState()) }
    val lastScreenBeforeSearch = remember { mutableStateOf<Screen?>(null) }
    val lastScreenBeforeFilter = remember { mutableStateOf<Screen?>(null) }
    val allProducts = remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    val lastScreenBeforeCollectionDetail = remember { mutableStateOf<Screen?>(null) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            accountViewModel.loadUserProfile()
        }
    }

    MaterialTheme {
        when (authState) {
            is AuthState.Unauthenticated -> {
                when (currentScreen.value) {
                    Screen.Login -> LoginScreen(
                        onLoginSuccess = { currentScreen.value = Screen.Home },
                        onNavigateToRegister = { currentScreen.value = Screen.Register }
                    )
                    Screen.Register -> RegisterScreen(
                        onRegisterSuccess = { currentScreen.value = Screen.Login },
                        onNavigateToLogin = { currentScreen.value = Screen.Login }
                    )
                    else -> currentScreen.value = Screen.Login
                }
            }
            is AuthState.Authenticated -> {
                Box(modifier = Modifier.padding()) {
                    when (val screen = currentScreen.value) {
                        Screen.Home -> HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToList = { type, status ->
                                productListState.value = productListState.value.copy(type = type, status = status)
                                currentScreen.value = Screen.ProductList(type, status)
                            },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            selectedBottomItem = BottomMenuItem.Home,
                            onBottomItemSelected = { bottom ->
                                if (bottom == BottomMenuItem.Search && screen !is Screen.Search) {
                                    lastScreenBeforeSearch.value = screen
                                }
                                currentScreen.value = bottom.toScreen()
                            }
                        )

                        Screen.Discover -> DiscoverScreen(
                            discoverViewModel = discoverViewModel,
                            collectionViewModel = collectionViewModel,
                            onNavigateToCollectionDetail = {
                                lastScreenBeforeCollectionDetail.value = Screen.Discover
                                currentScreen.value = Screen.CollectionDetail
                            },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            selectedBottomItem = BottomMenuItem.Discover,
                            onBottomItemSelected = { currentScreen.value = it.toScreen().also { s ->
                                if (it == BottomMenuItem.Search && screen !is Screen.Search) {
                                    lastScreenBeforeSearch.value = screen
                                }
                            } }
                        )

                        Screen.Search -> SearchScreen(
                            viewModel = searchViewModel,
                            onBack = {
                                searchViewModel.onFilterChange(ProductFilter())
                                searchViewModel.clearQuery()
                                currentScreen.value = lastScreenBeforeSearch.value ?: Screen.Home
                                lastScreenBeforeSearch.value = null
                            },
                            onNavigateToFilterScreen = {
                                lastScreenBeforeFilter.value = screen
                                currentScreen.value = Screen.Filter(
                                    initialFilter = searchViewModel.uiState.value.filter,
                                    originalType = null,
                                    originalStatus = null
                                )
                            },
                            onProductClick = { productId ->
                                currentScreen.value = Screen.ProductDetail(productId, null, null)
                            },
                            selectedBottomItem = BottomMenuItem.Search,
                            onBottomItemSelected = { bottom ->
                                if (screen is Screen.Search && bottom != BottomMenuItem.Search) {
                                    searchViewModel.clearQuery()
                                    searchViewModel.onFilterChange(ProductFilter())
                                }

                                if (bottom == BottomMenuItem.Search && screen !is Screen.Search) {
                                    lastScreenBeforeSearch.value = screen
                                }
                                currentScreen.value = bottom.toScreen()
                            }
                        )

                        Screen.Collection -> CollectionScreen(
                            viewModel = collectionViewModel,
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            selectedBottomItem = BottomMenuItem.Lists,
                            onUserCollectionClick = {
                                collectionViewModel.selectUserCollection(it)
                                currentScreen.value = Screen.CollectionDetail
                            },
                            onFollowedCollectionClick = {
                                collectionViewModel.selectFollowedCollection(it)
                                currentScreen.value = Screen.CollectionDetail
                            },
                            onBottomItemSelected = { bottom ->
                                if (bottom == BottomMenuItem.Search && screen !is Screen.Search) {
                                    lastScreenBeforeSearch.value = screen
                                }
                                currentScreen.value = bottom.toScreen()
                            }
                        )

                        Screen.CollectionDetail -> CollectionDetailScreen(
                            viewModel = collectionViewModel,
                            onBack = {
                                collectionViewModel.clearSelectedCollection()
                                collectionViewModel.onFilterChange(ProductFilter())
                                currentScreen.value = lastScreenBeforeCollectionDetail.value ?: Screen.Collection
                                lastScreenBeforeCollectionDetail.value = null
                            },
                            onNavigateToFilterScreen = {
                                lastScreenBeforeFilter.value = Screen.CollectionDetail
                                currentScreen.value = Screen.Filter(
                                    initialFilter = collectionViewModel.uiState.value.filter,
                                    originalType = null,
                                    originalStatus = null
                                )
                            },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            onProductClick = { productId ->
                                currentScreen.value = Screen.ProductDetail(productId, null, null)
                            },
                            onManageAdd = {
                                collectionViewModel.loadAllProducts {
                                    allProducts.value = collectionViewModel.uiState.value.allProducts
                                    currentScreen.value = Screen.CollectionManage(isAddMode = true)
                                }
                            },
                            onManageRemove = {
                                collectionViewModel.loadAllProducts {
                                    allProducts.value = collectionViewModel.uiState.value.allProducts
                                    currentScreen.value = Screen.CollectionManage(isAddMode = false)
                                }
                            },
                            selectedBottomItem = BottomMenuItem.Lists,
                            onBottomItemSelected = { currentScreen.value = it.toScreen() }
                        )

                        is Screen.CollectionManage -> CollectionManageScreen(
                            viewModel = collectionViewModel,
                            products = allProducts.value,
                            onProductClick = { productId ->
                                currentScreen.value = Screen.ProductDetail(productId, null, null)
                            },
                            isAddMode = screen.isAddMode,
                            onBack = {
                                currentScreen.value = Screen.CollectionDetail
                            },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            onNavigateToFilterScreen = {
                                lastScreenBeforeFilter.value = screen
                                currentScreen.value = Screen.Filter(
                                    initialFilter = collectionViewModel.uiState.value.filter,
                                    originalType = null,
                                    originalStatus = null
                                )
                            },
                            selectedBottomItem = BottomMenuItem.Lists,
                            onBottomItemSelected = { currentScreen.value = it.toScreen() }
                        )

                        Screen.Account -> AccountScreen(
                            accountViewModel = accountViewModel,
                            notificationViewModel = notificationViewModel,
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            onHelpItemClick = { helpItem ->
                                // Obrir finestra amb contingut segons l'ítem clicat
                            },
                            onLogout = {
                                AppState.logout()
                                currentScreen.value = Screen.Login
                            },
                            selectedBottomItem = BottomMenuItem.Account,
                            onBottomItemSelected = { currentScreen.value = it.toScreen() }
                        )

                        is Screen.ProductList -> ProductListScreen(
                            viewModel = homeViewModel,
                            collectionViewModel = collectionViewModel,
                            type = screen.type,
                            status = screen.status,
                            initialFilter = screen.filter,
                            onFilterChange = {
                                productListState.value = productListState.value.copy(filter = it)
                                homeViewModel.onFilterChange(it)
                            },
                            onBack = { currentScreen.value = Screen.Home },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            onNavigateToFilterScreen = {
                                lastScreenBeforeFilter.value = screen
                                currentScreen.value = Screen.Filter(
                                    initialFilter = productListState.value.filter,
                                    originalType = productListState.value.type,
                                    originalStatus = productListState.value.status
                                )
                            },
                            onProductClick = { productId ->
                                currentScreen.value = Screen.ProductDetail(productId, screen.type, screen.status)
                            },
                            selectedBottomItem = BottomMenuItem.Home,
                            onBottomItemSelected = { currentScreen.value = it.toScreen() }
                        )

                        is Screen.ProductDetail -> ProductDetailScreen(
                            productId = screen.productId,
                            homeViewModel = homeViewModel,
                            collectionViewModel = collectionViewModel,
                            searchViewModel = searchViewModel,
                            onBack = {
                                currentScreen.value = Screen.ProductList(screen.type, screen.status)
                            },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            onNavigateToSearchScreen = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            selectedBottomItem = BottomMenuItem.Home,
                            onBottomItemSelected = { currentScreen.value = it.toScreen() }
                        )

                        is Screen.Filter -> FilterScreen(
                            initialFilter = screen.initialFilter,
                            availableProductTypes = FilterOptionsCache.productTypes,
                            availableSubtypes = FilterOptionsCache.subtypes,
                            availableSupertypes = FilterOptionsCache.supertypes,
                            availableStatus = FilterOptionsCache.status,
                            availableLicenses = FilterOptionsCache.licenses,
                            availableTags = FilterOptionsCache.tags,
                            onApply = { newFilter ->
                                when (val last = lastScreenBeforeFilter.value) {
                                    Screen.Search -> {
                                        searchViewModel.onFilterChange(newFilter)
                                        currentScreen.value = Screen.Search
                                    }
                                    Screen.CollectionDetail -> {
                                        collectionViewModel.onFilterChange(newFilter)
                                        currentScreen.value = Screen.CollectionDetail
                                    }
                                    is Screen.CollectionManage -> {
                                        collectionViewModel.onFilterChange(newFilter)
                                        currentScreen.value = Screen.CollectionManage(isAddMode = last.isAddMode)
                                    }
                                    else -> {
                                        productListState.value = productListState.value.copy(filter = newFilter)
                                        currentScreen.value = Screen.ProductList(
                                            type = productListState.value.type,
                                            status = productListState.value.status,
                                            filter = newFilter
                                        )
                                    }
                                }
                            },
                            onReset = {
                                when (val last = lastScreenBeforeFilter.value) {
                                    Screen.Search -> searchViewModel.onFilterChange(ProductFilter())
                                    Screen.CollectionDetail -> collectionViewModel.onFilterChange(ProductFilter())
                                    is Screen.CollectionManage -> {
                                        collectionViewModel.onFilterChange(ProductFilter())
                                        currentScreen.value = Screen.CollectionManage(isAddMode = last.isAddMode)
                                    }
                                    else -> productListState.value = productListState.value.copy(filter = ProductFilter())
                                }
                            },
                            onBack = {
                                currentScreen.value = when (val last = lastScreenBeforeFilter.value) {
                                    Screen.Search -> Screen.Search
                                    Screen.CollectionDetail -> Screen.CollectionDetail
                                    is Screen.CollectionManage -> { Screen.CollectionManage(isAddMode = last.isAddMode) }
                                    else -> Screen.ProductList(
                                        type = productListState.value.type,
                                        status = productListState.value.status,
                                        filter = productListState.value.filter
                                    )
                                }
                            },
                            onSearch = {
                                lastScreenBeforeSearch.value = screen
                                currentScreen.value = Screen.Search
                            },
                            selectedBottomItem = BottomMenuItem.Home,
                            onBottomItemSelected = { currentScreen.value = it.toScreen() }
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}