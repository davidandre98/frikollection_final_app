package org.example.frikollection_mobile_desktop.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.ui.cardview.CollectionCard
import org.example.frikollection_mobile_desktop.ui.collection.CollectionFilterDropdown
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader


@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = remember { CollectionViewModel() },
    onSearch: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf("My Collections") }
    val filterOptions = listOf(
        "All Collections",
        "My Collections",
        "Public Collections",
        "Private Collections",
        "Followed Collections"
    )

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = false,
                onBack = {},
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
            CollectionFilterDropdown(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                filterOptions = filterOptions
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val collections: List<Any> = when (selectedFilter) {
                    "Public collections" -> state.userCollections.filterNot { it.private }
                    "Private collections" -> state.userCollections.filter { it.private }
                    "Followed collections" -> state.followedCollections
                    "My collections" -> state.userCollections
                    else -> state.userCollections + state.followedCollections
                }

                // CREATE NEW CollectionCard
                item {
                    CollectionCard(
                        title = "CREATE NEW",
                        subtitle = "Collection",
                        productCount = null,
                        imageRes = null,
                        isPrivate = false,
                        onClick = { /* TODO: Action */ }
                    )
                }

                items(collections.size) { index ->
                    when (val collection = collections[index]) {
                        is UserCollectionDto -> {
                            val stats = state.statsMap[collection.collectionId]
                            CollectionCard(
                                title = collection.name,
                                subtitle = "Owner",
                                productCount = stats?.totalProducts ?: collection.products.size,
                                imageRes = null,
                                isPrivate = collection.private,
                                onClick = { /* TODO */ }
                            )
                        }
                        is FollowedCollectionDto -> {
                            val stats = state.statsMap[collection.collectionId]
                            CollectionCard(
                                title = collection.name,
                                subtitle = "By: ${collection.ownerNickname}",
                                productCount = stats?.totalProducts,
                                imageRes = null,
                                isPrivate = false,
                                onClick = { /* TODO */ }
                            )
                        }
                    }
                }
            }
        }
    }
}