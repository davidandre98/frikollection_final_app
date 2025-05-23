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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.ui.cardview.CollectionCard
import org.example.frikollection_mobile_desktop.ui.collection.CollectionFilterDropdown
import org.example.frikollection_mobile_desktop.ui.collection.CollectionOptionsDialog
import org.example.frikollection_mobile_desktop.ui.collection.CreateCollectionDialog
import org.example.frikollection_mobile_desktop.ui.collection.UpdateCollectionDialog
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader


@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel,
    onSearch: () -> Unit,
    onUserCollectionClick: (UserCollectionDto) -> Unit,
    onFollowedCollectionClick: (FollowedCollectionDto) -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        if (state.userCollections.isEmpty()) {
            viewModel.loadCollections()
        }
    }

    var showCreateDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showDeletedSuccessDialog by remember { mutableStateOf(false) }
    var showCollectionOptionsDialog by remember { mutableStateOf(false) }

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
                    "Public Collections" -> state.userCollections.filterNot { it.private }
                        .sortedWith(compareBy {
                            when (it.name.lowercase()) {
                                "my wishlist" -> 0
                                "my collection" -> 1
                                else -> 2
                            }
                        })
                    "Private Collections" -> state.userCollections.filter { it.private }
                        .sortedWith(compareBy {
                            when (it.name.lowercase()) {
                                "my wishlist" -> 0
                                "my collection" -> 1
                                else -> 2
                            }
                        })
                    "Followed Collections" -> state.followedCollections
                    "My Collections" -> state.userCollections.sortedWith(compareBy {
                        when (it.name.lowercase()) {
                            "my wishlist" -> 0
                            "my collection" -> 1
                            else -> 2
                        }
                    })
                    else -> {
                        val sortedUserCollections = state.userCollections.sortedWith(compareBy {
                            when (it.name.lowercase()) {
                                "my wishlist" -> 0
                                "my collection" -> 1
                                else -> 2
                            }
                        })
                        sortedUserCollections + state.followedCollections
                    }
                }

                // CREATE NEW Collection
                item {
                    val imageUrl = "${AppConfig.base_url}/images/uploads/logo/logo_new_collection.png"
                    CollectionCard(
                        title = "CREATE NEW",
                        subtitle = "Collection",
                        productCount = null,
                        imageUrl = imageUrl,
                        isPrivate = false,
                        onClick = { showCreateDialog = true },
                        onOptionsClick = {}
                    )
                }

                items(collections.size) { index ->
                    when (val collection = collections[index]) {
                        is UserCollectionDto -> {
                            val stats = state.statsMap[collection.collectionId]

                            val imageUrl = when (collection.name.lowercase()) {
                                "my wishlist" -> "${AppConfig.base_url}/images/uploads/logo/logo_wishlist.png"
                                "my collection" -> "${AppConfig.base_url}/images/uploads/logo/logo_default.png"
                                else -> {
                                    collection.products.firstOrNull()?.smallPicture?.let {
                                        "${AppConfig.base_url}$it"
                                    } ?: "${AppConfig.base_url}/images/uploads/logo/logo_pet.png"
                                }
                            }

                            CollectionCard(
                                title = collection.name,
                                subtitle = null,
                                productCount = stats?.totalProducts ?: collection.products.size,
                                imageUrl = imageUrl,
                                isPrivate = collection.private,
                                onClick = { onUserCollectionClick(collection) },
                                onOptionsClick = {
                                    viewModel.selectUserCollection(collection)
                                    showCollectionOptionsDialog = true
                                }
                            )
                        }

                        is FollowedCollectionDto -> {
                            val stats = state.statsMap[collection.collectionId]

                            val matchedUserCollection = state.userCollections.find { it.collectionId == collection.collectionId }
                            val imageUrl = matchedUserCollection?.products?.firstOrNull()?.smallPicture?.let {
                                "${AppConfig.base_url}$it"
                            } ?: "${AppConfig.base_url}/images/uploads/logo/logo_pet.png"

                            CollectionCard(
                                title = collection.name,
                                subtitle = "By: ${collection.ownerNickname}",
                                productCount = stats?.totalProducts,
                                imageUrl = imageUrl,
                                isPrivate = false,
                                onClick = { onFollowedCollectionClick(collection) },
                                onOptionsClick = {}
                            )
                        }
                    }
                }
            }

            if (showCreateDialog) {
                CreateCollectionDialog(
                    onDismiss = { showCreateDialog = false },
                    onCreate = { name, isPrivate ->
                        viewModel.createCollection(name, isPrivate)
                        showCreateDialog = false
                    }
                )
            }

            state.selectedUserCollection?.let { selected ->
                if (showUpdateDialog) {
                    UpdateCollectionDialog(
                        collection = selected,
                        onDismiss = {
                            showUpdateDialog = false
                            viewModel.clearSelectedCollection()
                        },
                        onUpdate = { name, isPrivate ->
                            viewModel.updateCollection(selected.collectionId, name, isPrivate)
                            showUpdateDialog = false
                            viewModel.clearSelectedCollection()
                        }
                    )
                }

                if (showCollectionOptionsDialog) {
                    CollectionOptionsDialog(
                        name = selected.name,
                        onDismiss = { showCollectionOptionsDialog = false },
                        onEdit = {
                            showCollectionOptionsDialog = false
                            showUpdateDialog = true
                        },
                        onRemove = {
                            showCollectionOptionsDialog = false
                            showDeleteConfirmDialog = true
                        }
                    )
                }

                if (showDeleteConfirmDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmDialog = false },
                        title = { Text("Delete Collection") },
                        text = { Text("Are you sure you want to delete this collection?") },
                        confirmButton = {
                            Button(onClick = {
                                viewModel.deleteCollection(selected.collectionId)
                                showDeleteConfirmDialog = false
                                showDeletedSuccessDialog = true
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteConfirmDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }

            if (showDeletedSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showDeletedSuccessDialog = false },
                    title = { Text("Collection Deleted") },
                    text = { Text("The collection has been successfully deleted.") },
                    confirmButton = {
                        Button(onClick = { showDeletedSuccessDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}