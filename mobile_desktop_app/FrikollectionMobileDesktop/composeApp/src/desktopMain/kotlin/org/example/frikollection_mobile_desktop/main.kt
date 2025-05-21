package org.example.frikollection_mobile_desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.frikollection_mobile_desktop.account.AccountScreen
import org.example.frikollection_mobile_desktop.collection.CollectionScreen
import org.example.frikollection_mobile_desktop.collection.CollectionViewModel
import org.example.frikollection_mobile_desktop.discover.DiscoverScreen
import org.example.frikollection_mobile_desktop.filter.FilterOptionsCache
import org.example.frikollection_mobile_desktop.home.HomeScreen
import org.example.frikollection_mobile_desktop.home.HomeViewModel
import org.example.frikollection_mobile_desktop.home.ProductDetailScreen
import org.example.frikollection_mobile_desktop.home.ProductListScreen
import org.example.frikollection_mobile_desktop.login.LoginScreen
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.models.ProductListState
import org.example.frikollection_mobile_desktop.register.RegisterScreen
import org.example.frikollection_mobile_desktop.search.SearchScreen
import org.example.frikollection_mobile_desktop.search.SearchViewModel
import org.example.frikollection_mobile_desktop.ui.filter.FilterScreen

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
    object Discover : Screen()
    object Search : Screen()
    object Collection : Screen()
    object Account : Screen()
    data class ProductList(val type: String?, val status: String?, val filter: ProductFilter = ProductFilter()) : Screen()
    data class ProductDetail(val productId: String, val type: String?, val status: String?) : Screen()
    data class Filter(val initialFilter: ProductFilter, val originalType: String?, val originalStatus: String?) : Screen()
}

fun main() = application {
    val windowState = rememberWindowState(
        width = 500.dp,
        height = 700.dp,
        position = WindowPosition.Aligned(Alignment.Center)
    )

    val currentScreenState = remember { mutableStateOf<Screen>(Screen.Login) }
    val currentScreen = currentScreenState.value
    val homeViewModel = remember { HomeViewModel() }
    val productListState = remember { mutableStateOf(ProductListState()) }
    val searchViewModel = remember { SearchViewModel() }
    val lastScreenBeforeSearch = remember { mutableStateOf<Screen?>(null) }
    val lastScreenBeforeFilter = remember { mutableStateOf<Screen?>(null) }

    // Ajustar mides i centrar finestra al canviar de pantalles
    LaunchedEffect(currentScreen) {
        when (currentScreen) {
            Screen.Login -> windowState.size = DpSize(500.dp, 700.dp)
            Screen.Register -> windowState.size = DpSize(640.dp, 1020.dp)
            else -> windowState.size = DpSize(720.dp, 1080.dp)
        }
        windowState.position = WindowPosition.Aligned(Alignment.Center)
    }

    fun resetFiltersIfNeeded(
        current: Screen,
        productListState: MutableState<ProductListState>,
        searchViewModel: SearchViewModel
    ) {
        when (current) {
            is Screen.ProductList -> productListState.value = productListState.value.copy(filter = ProductFilter())
            Screen.Search -> searchViewModel.onFilterChange(ProductFilter())
            else -> Unit
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Desktop App"
    ) {
        when (currentScreen) {
            Screen.Login -> LoginScreen(
                onLoginSuccess = { currentScreenState.value = Screen.Home },
                onNavigateToRegister = { currentScreenState.value = Screen.Register }
            )

            Screen.Register -> RegisterScreen(
                onNavigateToLogin = { currentScreenState.value = Screen.Login },
                onRegisterSuccess = { currentScreenState.value = Screen.Login }
            )

            Screen.Home -> HomeScreen(
                onNavigateToList = { type, status ->
                    productListState.value = productListState.value.copy(type = type, status = status)
                    currentScreenState.value = Screen.ProductList(
                        type = productListState.value.type,
                        status = productListState.value.status,
                        filter = productListState.value.filter
                    )
                },
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            Screen.Discover -> DiscoverScreen(
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Discover,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            Screen.Search -> SearchScreen(
                viewModel = searchViewModel,
                onBack = {
                    searchViewModel.onFilterChange(ProductFilter())
                    currentScreenState.value = lastScreenBeforeSearch.value ?: Screen.Home
                    lastScreenBeforeSearch.value = null
                },
                onNavigateToFilterScreen = {
                    lastScreenBeforeFilter.value = currentScreen
                    currentScreenState.value = Screen.Filter(
                        initialFilter = searchViewModel.uiState.value.filter,
                        originalType = null,
                        originalStatus = null
                    )
                },
                onProductClick = { productId ->
                    currentScreenState.value = Screen.ProductDetail(
                        productId = productId,
                        type = null,
                        status = null
                    )
                },
                selectedBottomItem = BottomMenuItem.Search,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            Screen.Collection -> CollectionScreen(
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Lists,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            Screen.Account -> AccountScreen(
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Account,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            is Screen.ProductList -> ProductListScreen(
                viewModel = homeViewModel,
                type = productListState.value.type,
                status = productListState.value.status,
                initialFilter = currentScreen.filter,
                onFilterChange = { newFilter ->
                    productListState.value = productListState.value.copy(filter = newFilter)
                    homeViewModel.onFilterChange(newFilter)
                    currentScreenState.value = Screen.ProductList(
                        type = productListState.value.type,
                        status = productListState.value.status,
                        filter = newFilter
                    )

                },
                onBack = {
                    currentScreenState.value = Screen.Home
                    productListState.value = productListState.value.copy(filter = ProductFilter())
                },
                onSearch = {
                    productListState.value = productListState.value.copy(filter = ProductFilter())
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                onNavigateToFilterScreen = {
                    lastScreenBeforeFilter.value = currentScreen
                    currentScreenState.value = Screen.Filter(
                        initialFilter = productListState.value.filter,
                        originalType = productListState.value.type,
                        originalStatus = productListState.value.status
                    )
                },
                onProductClick = { productId ->
                    currentScreenState.value = Screen.ProductDetail(
                        productId = productId,
                        type = productListState.value.type,
                        status = productListState.value.status
                    )
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            is Screen.ProductDetail -> ProductDetailScreen(
                productId = currentScreen.productId,
                homeViewModel = homeViewModel,
                searchViewModel = searchViewModel,
                onBack = {
                    currentScreenState.value = Screen.ProductList(
                        type = productListState.value.type,
                        status = productListState.value.status,
                        filter = productListState.value.filter
                    )
                },
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                onNavigateToSearchScreen = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            is Screen.Filter -> FilterScreen(
                initialFilter = currentScreen.initialFilter,
                availableProductTypes = FilterOptionsCache.productTypes,
                availableSubtypes = FilterOptionsCache.subtypes,
                availableSupertypes = FilterOptionsCache.supertypes,
                availableStatus = FilterOptionsCache.status,
                availableLicenses = FilterOptionsCache.licenses,
                availableTags = FilterOptionsCache.tags,
                onApply = { newFilter ->
                    if (lastScreenBeforeFilter.value == Screen.Search) {
                        searchViewModel.onFilterChange(newFilter)
                        currentScreenState.value = Screen.Search
                    } else {
                        productListState.value = productListState.value.copy(filter = newFilter)
                        currentScreenState.value = Screen.ProductList(
                            type = productListState.value.type,
                            status = productListState.value.status,
                            filter = productListState.value.filter
                        )
                    }
                },
                onReset = {
                    if (lastScreenBeforeFilter.value == Screen.Search) {
                        searchViewModel.onFilterChange(ProductFilter())
                    } else {
                        productListState.value = productListState.value.copy(filter = ProductFilter())
                    }
                },
                onBack = {
                    if (lastScreenBeforeFilter.value == Screen.Search) {
                        currentScreenState.value = Screen.Search
                    } else {
                        currentScreenState.value = Screen.ProductList(
                            type = productListState.value.type,
                            status = productListState.value.status,
                            filter = productListState.value.filter
                        )
                    }
                },
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Home,
                onBottomItemSelected = { bottomItem ->
                    if (bottomItem == BottomMenuItem.Search && currentScreen !is Screen.Search) {
                        lastScreenBeforeSearch.value = currentScreen
                    }

                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)

                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )
        }
    }
}