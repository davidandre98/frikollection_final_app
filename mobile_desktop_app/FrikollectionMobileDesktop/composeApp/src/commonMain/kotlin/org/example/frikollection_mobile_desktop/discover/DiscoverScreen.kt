package org.example.frikollection_mobile_desktop.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.account.InfoField
import org.example.frikollection_mobile_desktop.api.AppConfig.base_url
import org.example.frikollection_mobile_desktop.collection.CollectionViewModel
import org.example.frikollection_mobile_desktop.ui.ServerImage
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.user.EditAccountInfoDialog
import org.example.frikollection_mobile_desktop.ui.user.NotificationDialog
import org.example.frikollection_mobile_desktop.ui.user.ShowEditAvatarDialog

@Composable
fun DiscoverScreen(
    discoverViewModel: DiscoverViewModel,
    collectionViewModel: CollectionViewModel,
    onNavigateToCollectionDetail: () -> Unit,
    onSearch: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by discoverViewModel.uiState.collectAsState()

    var showCollectionsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        discoverViewModel.loadDiscoverData()
    }

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = false,
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // COLLECTIONS SECTION
            SectionWithHeader(title = "Collections", onViewAll = { showCollectionsDialog = true }) {
                state.otherCollections.forEach { collection ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(collection.name, fontWeight = FontWeight.Bold)
                            Text("By: ${collection.user?.username ?: "Unknown"}", fontSize = 12.sp)
                            Text("${collection.products?.size ?: 0} products", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // USERS SECTION
            SectionWithHeader(title = "Users", onViewAll = { /* TODO */ }) {
                state.otherUsers.forEach { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ServerImage(
                                imageUrl = "$base_url${user.avatar}",
                                contentDescription = user.nickname,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(user.nickname, fontWeight = FontWeight.Bold)
                                Text(user.firstName + " " + user.lastName, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            if (showCollectionsDialog) {
                Dialog(onDismissRequest = { showCollectionsDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("All Collections", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                            val scrollState = rememberScrollState()
                            Column(modifier = Modifier.verticalScroll(scrollState)) {
                                state.otherCollections.forEach { collection ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                collectionViewModel.selectPublicCollection(collection)
                                                showCollectionsDialog = false
                                                onNavigateToCollectionDetail()
                                            },
                                        elevation = 4.dp
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(collection.name, fontWeight = FontWeight.Bold)
                                            Text("By: ${collection.user?.username}", fontSize = 12.sp)
                                            Text("${collection.products?.size ?: 0} products", fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionWithHeader(title: String, onViewAll: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onViewAll) {
                Text("View all", color = Color(0xFF1976D2))
            }
        }
        content()
    }
}