package org.example.frikollection_mobile_desktop.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.ui.cardview.CollectionManageProductCard
import org.example.frikollection_mobile_desktop.ui.filter.FilterChip
import org.example.frikollection_mobile_desktop.ui.filter.hasActiveFilters
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.sort.SortBottomSheetDialog

@Composable
fun CollectionManageScreen(
    viewModel: CollectionViewModel,
    products: List<ProductDto>,
    onProductClick: (String) -> Unit,
    isAddMode: Boolean,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showSortDialog by remember { mutableStateOf(false) }
    val collection = state.selectedUserCollection ?: return

    // Controla estat de selecció local
    val selectedProducts = if (isAddMode) state.productsToAdd else state.productsToRemove

    // Llista de productes a mostrar
    val displayedProducts = if (isAddMode) {
        products.filterNot { p ->
            collection.products.any { it.productId == p.productId }
        }
    } else {
        collection.products
    }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = true,
                onBack = onBack,
                showSearch = true,
                onSearch = onSearch
            )
        },
        bottomBar = {
            AppFooter(
                selectedBottomItem = selectedBottomItem,
                onBottomItemSelected = onBottomItemSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D47A1))
                    .padding(16.dp)
            ) {
                Text(
                    text = if (isAddMode) "ADD ITEMS TO COLLECTION" else "REMOVE ITEMS FROM COLLECTION",
                    color = Color.White,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = collection.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.height(12.dp))

            if (hasActiveFilters(state.filter)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.filter.productType?.let {
                        FilterChip(text = it) {
                            viewModel.onFilterRemoved(state.filter.copy(productType = null))
                        }
                    }
                    state.filter.status?.let {
                        FilterChip(text = it) {
                            viewModel.onFilterRemoved(state.filter.copy(status = null))
                        }
                    }
                    state.filter.subtype?.let {
                        FilterChip(text = it) {
                            viewModel.onFilterRemoved(state.filter.copy(subtype = null))
                        }
                    }
                    state.filter.supertype?.let {
                        FilterChip(text = it) {
                            viewModel.onFilterRemoved(state.filter.copy(supertype = null))
                        }
                    }
                    state.filter.license?.let {
                        FilterChip(text = it) {
                            viewModel.onFilterRemoved(state.filter.copy(license = null))
                        }
                    }
                    state.filter.tags.forEach { tag ->
                        FilterChip(text = tag) {
                            viewModel.onFilterRemoved(state.filter.copy(tags = state.filter.tags - tag))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayedProducts) { product ->
                        CollectionManageProductCard(
                            product = product,
                            isAddMode = isAddMode,
                            isSelected = selectedProducts.contains(product),
                            onToggleSelected = {
                                if (isAddMode) viewModel.toggleProductToAdd(it)
                                else viewModel.toggleProductToRemove(it)
                            },
                            onProductClick = { clickedProduct ->
                                onProductClick(clickedProduct.productId)
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        val collectionId = collection.collectionId
                        if (isAddMode) {
                            val ids = state.productsToAdd.map { it.productId }
                            viewModel.addProductsToCollection(collectionId, ids) { added, _ ->
                                viewModel.clearSelectedProducts()
                                successMessage = "S'han afegit ${added.size} productes a la col·lecció."
                                showSuccessDialog = true
                                viewModel.refreshSelectedCollection()
                            }
                        } else {
                            val ids = state.productsToRemove.map { it.productId }
                            var remaining = ids.size
                            ids.forEach { id ->
                                viewModel.removeProductFromCollection(collectionId, id) {
                                    remaining--
                                    if (remaining == 0) {
                                        viewModel.clearSelectedProducts()
                                        successMessage = "S'han eliminat ${ids.size} productes de la col·lecció."
                                        showSuccessDialog = true
                                        viewModel.refreshSelectedCollection()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0D47A1))
                ) {
                    Text("Done", fontSize = 16.sp, color = Color.White)
                }
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

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false; onBack() },
                title = { Text("Operació completada") },
                text = { Text(successMessage) },
                confirmButton = {
                    Button(onClick = {
                        showSuccessDialog = false
                        onBack()
                    }) {
                        Text("D'acord")
                    }
                }
            )
        }
    }
}