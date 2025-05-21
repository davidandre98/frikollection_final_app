package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.ui.cardview.ProductCard
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.filter.FilterChip
import org.example.frikollection_mobile_desktop.ui.filter.hasActiveFilters
import org.example.frikollection_mobile_desktop.ui.sort.SortBottomSheetDialog


enum class SortOption(val label: String) {
    NameAsc("Name: A - Z"),
    NameDesc("Name: Z - A"),
    ReleaseAsc("Oldest First"),
    ReleaseDesc("Newest First"),
    ValueAsc("Value: Low to High"),
    ValueDesc("Value: High to Low")
}

@Composable
fun ProductListScreen(
    viewModel: HomeViewModel,
    type: String?,
    status: String?,
    initialFilter: ProductFilter,
    onFilterChange: (ProductFilter) -> Unit,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onProductClick: (String) -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showSortDialog by remember { mutableStateOf(false) }

    LaunchedEffect(type) {
        viewModel.setSelectedType(type)
    }
    LaunchedEffect(status) {
        viewModel.setSelectedStatus(status)
    }
    LaunchedEffect(initialFilter) {
        viewModel.onFilterChange(initialFilter)
    }

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = true,
                onBack = { onBack() },
                showSearch = true,
                onSearch = { onSearch() }
            )
        },
        bottomBar = {
            AppFooter(
                selectedBottomItem = selectedBottomItem,
                onBottomItemSelected = onBottomItemSelected
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onNavigateToFilterScreen() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Filter", color = Color.White, fontSize = 18.sp)
                    }

                    Box {
                        OutlinedButton(
                            onClick = { showSortDialog = true },
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Sort: ${state.sort.label}", color = Color(0xFF0D47A1))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (hasActiveFilters(state.filter)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 36.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.filter.productType?.let {
                            FilterChip(it) {
                                onFilterChange(state.filter.copy(productType = null))
                            }
                        }
                        state.filter.status?.let {
                            FilterChip(it) {
                                onFilterChange(state.filter.copy(status = null))
                            }
                        }
                        state.filter.subtype?.let {
                            FilterChip(it) {
                                onFilterChange(state.filter.copy(subtype = null))
                            }
                        }
                        state.filter.supertype?.let {
                            FilterChip(it) {
                                onFilterChange(state.filter.copy(supertype = null))
                            }
                        }
                        state.filter.license?.let {
                            FilterChip(it) {
                                onFilterChange(state.filter.copy(license = null))
                            }
                        }
                        state.filter.tags.forEach { tag ->
                            FilterChip(tag) {
                                val newFilter = state.filter.copy(tags = state.filter.tags - tag)
                                onFilterChange(newFilter)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Text(
                        text = "${state.filteredProducts.size} PRODUCTS",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    val subtitle = buildString {
                        if (!status.isNullOrBlank()) append("$status ")
                        if (!type.isNullOrBlank() && type != "All Catalog") {
                            append("$type Catalog")
                        }
                        else append("$type")
                    }

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.h6,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.productId) }
                        )
                    }
                }

                if (showSortDialog) {
                    SortBottomSheetDialog(
                        currentSort = state.sort,
                        onDismiss = { showSortDialog = false },
                        onSortSelected = {
                            viewModel.onSortChanged(it)
                            showSortDialog = false
                        }
                    )
                }
            }
        }
    }
}