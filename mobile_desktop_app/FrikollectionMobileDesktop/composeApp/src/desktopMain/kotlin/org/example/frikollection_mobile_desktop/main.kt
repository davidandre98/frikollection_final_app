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
import org.example.frikollection_mobile_desktop.login.LoginScreen
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.models.ProductListState
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.register.RegisterScreen
import org.example.frikollection_mobile_desktop.search.SearchScreen
import org.example.frikollection_mobile_desktop.search.SearchViewModel
import org.example.frikollection_mobile_desktop.state.AppState
import org.example.frikollection_mobile_desktop.ui.filter.FilterScreen
import org.example.frikollection_mobile_desktop.utils.toScreen


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
    val lastScreenBeforeDetail = remember { mutableStateOf<Screen?>(null) }
    val collectionViewModel = remember { CollectionViewModel().apply { loadCollections() } }
    val allProducts = remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    val accountViewModel = remember { AccountViewModel() }
    val notificationViewModel = remember { NotificationViewModel() }
    val discoverViewModel = remember { DiscoverViewModel() }
    val lastScreenBeforeCollectionDetail = remember { mutableStateOf<Screen?>(null) }

    // Ajustar mides i centrar finestra al canviar de pantalles
    LaunchedEffect(currentScreen) {
        when (currentScreen) {
            Screen.Login -> windowState.size = DpSize(500.dp, 700.dp)
            Screen.Register -> windowState.size = DpSize(640.dp, 1020.dp)
            else -> windowState.size = DpSize(720.dp, 1080.dp)
        }
        windowState.position = WindowPosition.Aligned(Alignment.Center)

        if (currentScreen == Screen.Account) {
            accountViewModel.loadUserProfile()
        }
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
                viewModel = homeViewModel,
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
                discoverViewModel = discoverViewModel,
                collectionViewModel = collectionViewModel,
                onNavigateToCollectionDetail = {
                    lastScreenBeforeCollectionDetail.value = Screen.Discover
                    currentScreenState.value = Screen.CollectionDetail
                },
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
                    searchViewModel.clearQuery()
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
                    lastScreenBeforeDetail.value = currentScreen
                    currentScreenState.value = Screen.ProductDetail(productId = productId, type = null, status = null)
                },
                selectedBottomItem = BottomMenuItem.Search,
                onBottomItemSelected = { bottomItem ->
                    if (currentScreen is Screen.Search && bottomItem != BottomMenuItem.Search) {
                        searchViewModel.clearQuery()
                        searchViewModel.onFilterChange(ProductFilter())
                    }

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
                viewModel = collectionViewModel,
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                selectedBottomItem = BottomMenuItem.Lists,
                onUserCollectionClick = { userCollection ->
                    collectionViewModel.selectUserCollection(userCollection)
                    currentScreenState.value = Screen.CollectionDetail
                },
                onFollowedCollectionClick = { followedCollection ->
                    collectionViewModel.selectFollowedCollection(followedCollection)
                    currentScreenState.value = Screen.CollectionDetail
                },
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

            Screen.CollectionDetail -> CollectionDetailScreen(
                viewModel = collectionViewModel,
                onBack = {
                    collectionViewModel.clearSelectedCollection()
                    collectionViewModel.onFilterChange(ProductFilter())
                    currentScreenState.value = lastScreenBeforeCollectionDetail.value ?: Screen.Collection
                    lastScreenBeforeCollectionDetail.value = null
                },
                onNavigateToFilterScreen = {
                    lastScreenBeforeFilter.value = Screen.CollectionDetail
                    currentScreenState.value = Screen.Filter(
                        initialFilter = collectionViewModel.uiState.value.filter,
                        originalType = null,
                        originalStatus = null
                    )
                },
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                onProductClick = { productId ->
                    lastScreenBeforeDetail.value = currentScreen
                    currentScreenState.value = Screen.ProductDetail(productId = productId, type = null, status = null)
                },
                onManageAdd = {
                    collectionViewModel.loadAllProducts {
                        allProducts.value = collectionViewModel.uiState.value.allProducts
                        currentScreenState.value = Screen.CollectionManage(isAddMode = true)
                    }
                },
                onManageRemove = {
                    collectionViewModel.loadAllProducts {
                        allProducts.value = collectionViewModel.uiState.value.allProducts
                        currentScreenState.value = Screen.CollectionManage(isAddMode = false)
                    }
                },
                selectedBottomItem = BottomMenuItem.Lists,
                onBottomItemSelected = { bottomItem ->
                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)
                    collectionViewModel.onFilterChange(ProductFilter())
                    currentScreenState.value = when (bottomItem) {
                        BottomMenuItem.Home -> Screen.Home
                        BottomMenuItem.Discover -> Screen.Discover
                        BottomMenuItem.Search -> Screen.Search
                        BottomMenuItem.Lists -> Screen.Collection
                        BottomMenuItem.Account -> Screen.Account
                    }
                }
            )

            is Screen.CollectionManage -> CollectionManageScreen(
                viewModel = collectionViewModel,
                products = allProducts.value,
                onProductClick = { productId ->
                    lastScreenBeforeDetail.value = currentScreen
                    currentScreenState.value = Screen.ProductDetail(productId = productId, type = null, status = null)
                },
                isAddMode = currentScreen.isAddMode,
                onBack = {
                    currentScreenState.value = Screen.CollectionDetail
                },
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                onNavigateToFilterScreen = {
                    lastScreenBeforeFilter.value = currentScreen
                    currentScreenState.value = Screen.Filter(
                        initialFilter = collectionViewModel.uiState.value.filter,
                        originalType = null,
                        originalStatus = null
                    )
                },
                selectedBottomItem = BottomMenuItem.Lists,
                onBottomItemSelected = { bottomItem ->
                    resetFiltersIfNeeded(currentScreen, productListState, searchViewModel)
                    collectionViewModel.onFilterChange(ProductFilter())
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
                accountViewModel = accountViewModel,
                notificationViewModel = notificationViewModel,
                onSearch = {
                    lastScreenBeforeSearch.value = currentScreen
                    currentScreenState.value = Screen.Search
                },
                onHelpItemClick = { helpItem ->
                    // Obrir finestra amb contingut segons l'ítem clicat
                },
                onLogout = {
                    AppState.logout()
                    currentScreenState.value = Screen.Login
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
                collectionViewModel = collectionViewModel,
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
                    lastScreenBeforeDetail.value = currentScreen
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
                collectionViewModel = collectionViewModel,
                searchViewModel = searchViewModel,
                onBack = {
                    currentScreenState.value = when (val last = lastScreenBeforeDetail.value) {
                        is Screen.ProductList -> last
                        is Screen.Search -> Screen.Search
                        is Screen.CollectionDetail -> Screen.CollectionDetail
                        is Screen.CollectionManage -> last
                        else -> Screen.Home
                    }
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
                    when (val last = lastScreenBeforeFilter.value) {
                        Screen.Search -> {
                            searchViewModel.onFilterChange(newFilter)
                            currentScreenState.value = Screen.Search
                        }
                        Screen.CollectionDetail -> {
                            collectionViewModel.onFilterChange(newFilter)
                            currentScreenState.value = Screen.CollectionDetail
                        }
                        is Screen.CollectionManage -> {
                            collectionViewModel.onFilterChange(newFilter)
                            currentScreenState.value = Screen.CollectionManage(isAddMode = last.isAddMode)
                        }
                        else -> {
                            productListState.value = productListState.value.copy(filter = newFilter)
                            currentScreenState.value = Screen.ProductList(
                                type = productListState.value.type,
                                status = productListState.value.status,
                                filter = productListState.value.filter
                            )
                        }
                    }
                },
                onReset = {
                    when (val last = lastScreenBeforeFilter.value) {
                        Screen.Search -> {
                            searchViewModel.onFilterChange(ProductFilter())
                        }
                        Screen.CollectionDetail -> {
                            collectionViewModel.onFilterChange(ProductFilter())
                        }
                        is Screen.CollectionManage -> {
                            collectionViewModel.onFilterChange(ProductFilter())
                            currentScreenState.value = Screen.CollectionManage(isAddMode = last.isAddMode)
                        }
                        else -> {
                            productListState.value = productListState.value.copy(filter = ProductFilter())
                        }
                    }
                },
                onBack = {
                    currentScreenState.value = when (val last = lastScreenBeforeFilter.value) {
                        Screen.Search -> Screen.Search
                        Screen.CollectionDetail -> Screen.CollectionDetail
                        is Screen.CollectionManage -> Screen.CollectionManage(isAddMode = last.isAddMode)
                        else -> Screen.ProductList(
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