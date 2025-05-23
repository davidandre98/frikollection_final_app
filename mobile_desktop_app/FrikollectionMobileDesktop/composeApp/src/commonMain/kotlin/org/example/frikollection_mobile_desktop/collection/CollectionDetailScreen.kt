package org.example.frikollection_mobile_desktop.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.home.SortOption
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.search.SearchBar
import org.example.frikollection_mobile_desktop.ui.cardview.ProductCard
import org.example.frikollection_mobile_desktop.ui.collection.EditCollectionDialog
import org.example.frikollection_mobile_desktop.ui.filter.FilterChip
import org.example.frikollection_mobile_desktop.ui.filter.hasActiveFilters
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.sort.SortBottomSheetDialog

@Composable
fun CollectionDetailScreen(
    viewModel: CollectionViewModel,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onProductClick: (String) -> Unit,
    onManageAdd: () -> Unit,
    onManageRemove: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        if (state.userCollections.isEmpty()) {
            viewModel.loadCollections()
        }
    }

    var showSortDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val collectionName = if (state.isViewingFollowed) {
        state.selectedFollowedCollection?.name ?: return
    } else {
        state.selectedUserCollection?.name ?: return
    }

    val isPrivate = if (state.isViewingFollowed) {
        false
    } else {
        state.selectedUserCollection?.private ?: return
    }

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
            // Header Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D47A1))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(collectionName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isPrivate) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Private",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Private", fontSize = 14.sp, color = Color.White)
                        } else {
                            Text(text = "Public", fontSize = 14.sp, color = Color.White)
                        }
                    }

                    if (!state.isViewingFollowed) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showEditDialog = true },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                        ) {
                            Text("Edit", color = Color.White)
                        }
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Filter & Sort
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
                OutlinedButton(
                    onClick = { showSortDialog = true },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Sort: ${state.sort.label}", color = Color(0xFF0D47A1))
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
                        FilterChip(it) { viewModel.onFilterRemoved(state.filter.copy(productType = null)) }
                    }
                    state.filter.status?.let {
                        FilterChip(it) { viewModel.onFilterRemoved(state.filter.copy(status = null)) }
                    }
                    state.filter.subtype?.let {
                        FilterChip(it) { viewModel.onFilterRemoved(state.filter.copy(subtype = null)) }
                    }
                    state.filter.supertype?.let {
                        FilterChip(it) { viewModel.onFilterRemoved(state.filter.copy(supertype = null)) }
                    }
                    state.filter.license?.let {
                        FilterChip(it) { viewModel.onFilterRemoved(state.filter.copy(license = null)) }
                    }
                    state.filter.tags.forEach { tag ->
                        FilterChip(tag) {
                            val newTags = state.filter.tags - tag
                            viewModel.onFilterRemoved(state.filter.copy(tags = newTags))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product List
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 200.dp),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.filteredProducts) { product ->
                    val isInWishlist = viewModel.isProductInWishlist(product.productId)

                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.productId) },
                        isInWishlist = isInWishlist,
                        onWishlistClick = {
                            viewModel.toggleProductInWishlist(product) { /* actualització reactiva si calgués */ }
                        },
                        onAddClick = {}
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

            if (showEditDialog) {
                EditCollectionDialog(
                    onDismiss = { showEditDialog = false },
                    onAddProducts = {
                        showEditDialog = false
                        onManageAdd()
                    },
                    onRemoveProducts = {
                        showEditDialog = false
                        onManageRemove()
                    }
                )
            }
        }
    }
}