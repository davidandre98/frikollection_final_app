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
import org.example.frikollection_mobile_desktop.collection.CollectionScreen
import org.example.frikollection_mobile_desktop.collection.CollectionViewModel
import org.example.frikollection_mobile_desktop.discover.DiscoverScreen
import org.example.frikollection_mobile_desktop.filter.FilterOptionsCache
import org.example.frikollection_mobile_desktop.home.HomeScreen
import org.example.frikollection_mobile_desktop.home.HomeViewModel
import org.example.frikollection_mobile_desktop.home.ProductDetailScreen
import org.example.frikollection_mobile_desktop.home.ProductListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.frikollection_mobile_desktop.login.LoginScreen
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.register.RegisterScreen
import org.example.frikollection_mobile_desktop.search.SearchScreen
import org.example.frikollection_mobile_desktop.search.SearchViewModel
import org.example.frikollection_mobile_desktop.state.AppState
import org.example.frikollection_mobile_desktop.state.AuthState
import org.example.frikollection_mobile_desktop.ui.filter.FilterScreen
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter

@Composable
@Preview
fun App() {
    val authState by AppState.authState.collectAsState()
    var selectedScreen by remember { mutableStateOf(BottomMenuItem.Home) }
    var showRegister by remember { mutableStateOf(false) }

    val homeViewModel = remember { HomeViewModel() }

    // Navegació a ProductListScreen quan un cardview de HomeScreen és clicat
    var showProductList by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    // Navegació a ProductDetailScreen quan un cardview de ProductListScreen és clicat
    var showProductDetail by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }

    // Navegació a FilterScreen
    var currentFilter by remember { mutableStateOf(ProductFilter()) }
    var showFilterScreen by remember { mutableStateOf(false) }
    var previousScreenBeforeFilter by remember { mutableStateOf<Any?>(null) }

    // Navegació a SearchScreen
    val searchViewModel = remember { SearchViewModel() }
    var previousScreenBeforeSearch by remember { mutableStateOf<Any?>(null) }

    // Navegació a CollectionScreen

    fun resetFiltersIfNeeded(
        current: BottomMenuItem,
        onResetProductFilter: () -> Unit,
        onResetSearchFilter: () -> Unit
    ) {
        when (current) {
            BottomMenuItem.Search -> onResetSearchFilter()
            else -> onResetProductFilter()
        }
    }

    MaterialTheme {
        when (authState) {
            is AuthState.Authenticated -> {
                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    bottomBar = {
                        AppFooter(
                            selectedBottomItem = selectedScreen,
                            onBottomItemSelected = {
                                showProductList = false
                                selectedType = null
                                selectedStatus = null
                                selectedScreen = it
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        when {
                            selectedScreen == BottomMenuItem.Home -> {
                                HomeScreen(
                                    onNavigateToList = { type, status ->
                                        selectedType = type
                                        selectedStatus = status
                                        showProductList = true
                                    },
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }

                            selectedScreen == BottomMenuItem.Discover -> {
                                DiscoverScreen(
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }
                            selectedScreen == BottomMenuItem.Search -> {
                                SearchScreen(
                                    viewModel = searchViewModel,
                                    onBack = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        previousScreenBeforeSearch = null
                                    },
                                    onNavigateToFilterScreen = {
                                        previousScreenBeforeFilter = selectedScreen
                                        showFilterScreen = true
                                    },
                                    onProductClick = { productId ->
                                        selectedProductId = productId
                                        showProductDetail = true
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }
                            selectedScreen == BottomMenuItem.Lists -> {
                                CollectionScreen(
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }
                            selectedScreen == BottomMenuItem.Account -> {
                                AccountScreen(
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }

                            showProductList -> {
                                ProductListScreen(
                                    viewModel = homeViewModel,
                                    type = selectedType,
                                    status = selectedStatus,
                                    initialFilter = currentFilter,
                                    onFilterChange = { newFilter ->
                                        currentFilter = newFilter
                                        homeViewModel.onFilterChange(currentFilter)
                                    },
                                    onBack = {
                                        showProductList = false
                                        selectedType = null
                                        selectedStatus = null
                                        currentFilter = ProductFilter()
                                    },
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    onNavigateToFilterScreen = {
                                        previousScreenBeforeFilter = selectedScreen
                                        showFilterScreen = true
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    },
                                    onProductClick = { productId ->
                                        selectedProductId = productId
                                        showProductDetail = true
                                    }
                                )
                            }

                            showFilterScreen -> {
                                FilterScreen(
                                    initialFilter = when (selectedScreen) {
                                        BottomMenuItem.Search -> searchViewModel.uiState.value.filter
                                        else -> currentFilter
                                    },
                                    availableProductTypes = FilterOptionsCache.productTypes,
                                    availableSubtypes = FilterOptionsCache.subtypes,
                                    availableSupertypes = FilterOptionsCache.supertypes,
                                    availableStatus = FilterOptionsCache.status,
                                    availableLicenses = FilterOptionsCache.licenses,
                                    availableTags = FilterOptionsCache.tags,
                                    onApply = { newFilter ->
                                        showFilterScreen = false

                                        when (previousScreenBeforeFilter) {
                                            BottomMenuItem.Search -> {
                                                searchViewModel.onFilterChange(newFilter)
                                            }
                                            else -> {
                                                currentFilter = newFilter
                                            }
                                        }
                                    },
                                    onReset = {
                                        when (previousScreenBeforeFilter) {
                                            BottomMenuItem.Search -> {
                                                searchViewModel.onFilterChange(ProductFilter())
                                            }
                                            else -> {
                                                currentFilter = ProductFilter()
                                            }
                                        }
                                    },
                                    onBack = {
                                        showFilterScreen = false
                                    },
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }

                            showProductDetail && selectedProductId != null -> {
                                ProductDetailScreen(
                                    productId = selectedProductId!!,
                                    homeViewModel = homeViewModel,
                                    searchViewModel = searchViewModel,
                                    onBack = {
                                        showProductDetail = false
                                        selectedProductId = null
                                    },
                                    onSearch = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    onNavigateToSearchScreen = {
                                        previousScreenBeforeSearch = selectedScreen
                                        selectedScreen = BottomMenuItem.Search
                                    },
                                    selectedBottomItem = selectedScreen,
                                    onBottomItemSelected = {
                                        resetFiltersIfNeeded(
                                            current = selectedScreen,
                                            onResetProductFilter = { currentFilter = ProductFilter() },
                                            onResetSearchFilter = { searchViewModel.onFilterChange(ProductFilter()) }
                                        )
                                        selectedScreen = it
                                    }
                                )
                            }
                        }
                    }
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