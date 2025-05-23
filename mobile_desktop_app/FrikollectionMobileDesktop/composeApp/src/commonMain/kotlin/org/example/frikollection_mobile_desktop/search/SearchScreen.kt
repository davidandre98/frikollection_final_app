package org.example.frikollection_mobile_desktop.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.collection.CollectionViewModel
import org.example.frikollection_mobile_desktop.ui.cardview.ProductCard
import org.example.frikollection_mobile_desktop.ui.filter.FilterChip
import org.example.frikollection_mobile_desktop.ui.filter.hasActiveFilters
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = remember { SearchViewModel() },
    onBack: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onProductClick: (String) -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()


    val collectionViewModel = remember { CollectionViewModel() }
    val collectionState by collectionViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (collectionState.userCollections.isEmpty()) {
            collectionViewModel.loadCollections()
        }
    }

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = true,
                onBack = { onBack() },
                showSearch = false
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

            SearchBar(
                query = state.query,
                onQueryChange = { viewModel.onQueryChange(it) },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Button(
                    onClick = { onNavigateToFilterScreen() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Filter", color = Color.White, fontSize = 18.sp)
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

            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Text(
                    text = "${state.filteredProducts.size} PRODUCTS",
                    fontSize = 14.sp,
                    color = Color.DarkGray
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
                    val isInWishlist = collectionViewModel.isProductInWishlist(product.productId)

                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.productId) },
                        isInWishlist = isInWishlist,
                        onWishlistClick = {
                            collectionViewModel.toggleProductInWishlist(product) { /* actualització reactiva si calgués */ }
                        },
                        onAddClick = {}
                    )
                }
            }

            state.error?.let { errorMsg ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color.Black,
                    modifier = Modifier
                        .clickable { onQueryChange("") }
                )
            }
        },
        placeholder = {
            Text(
                text = "Search in the catalog...",
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.padding(start = 2.dp)
            )
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}