package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.ProductDto
import org.example.frikollection_mobile_desktop.ui.ServerImage


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
    type: String?,
    status: String?,
    onBack: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onProductClick: (String) -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val viewModel = remember { HomeViewModel() }
    val state by viewModel.uiState.collectAsState()
    var sortOption by remember { mutableStateOf(SortOption.NameAsc) }

    val baseList = state.allProducts.filter { p ->
        (type == null || type.equals(p.productType?.typeName, ignoreCase = true) || type == "all") &&
                (status == null || status.equals(p.status, ignoreCase = true))
    }

    val finalList = remember(baseList, sortOption) {
        when (sortOption) {
            SortOption.NameAsc -> baseList.sortedBy { it.name }
            SortOption.NameDesc -> baseList.sortedByDescending { it.name }
            SortOption.ReleaseAsc -> baseList.sortedBy { it.release ?: "" }
            SortOption.ReleaseDesc -> baseList.sortedByDescending { it.release ?: "" }
            SortOption.ValueAsc -> baseList.sortedBy { it.value ?: 0.0 }
            SortOption.ValueDesc -> baseList.sortedByDescending { it.value ?: 0.0 }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Frikollection",
                        style = MaterialTheme.typography.h6.copy(fontSize = 20.sp)
                    )
                }

                IconButton(
                    onClick = { /* Acció per anar a SearchScreen */ },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
        ,
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    selected = selectedBottomItem == BottomMenuItem.Home,
                    onClick = { onBottomItemSelected(BottomMenuItem.Home) },
                    label = { Text("Home") },
                    icon = {}
                )
                BottomNavigationItem(
                    selected = selectedBottomItem == BottomMenuItem.Discover,
                    onClick = { onBottomItemSelected(BottomMenuItem.Discover) },
                    label = { Text("Discover") },
                    icon = {}
                )
                BottomNavigationItem(
                    selected = selectedBottomItem == BottomMenuItem.Search,
                    onClick = { onBottomItemSelected(BottomMenuItem.Search) },
                    label = { Text("Search") },
                    icon = {}
                )
                BottomNavigationItem(
                    selected = selectedBottomItem == BottomMenuItem.Lists,
                    onClick = { onBottomItemSelected(BottomMenuItem.Lists) },
                    label = { Text("Lists") },
                    icon = {}
                )
                BottomNavigationItem(
                    selected = selectedBottomItem == BottomMenuItem.Account,
                    onClick = { onBottomItemSelected(BottomMenuItem.Account) },
                    label = { Text("Account") },
                    icon = {}
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { onNavigateToFilterScreen() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text("Filter", color = Color.White)
                }

                DropdownSelector(
                    label = "Sort",
                    options = SortOption.values().map { it.label },
                    selected = sortOption.label,
                    onSelected = { selected ->
                        sortOption = SortOption.values().first { it.label == selected }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildString {
                    if (status != null) append("$status ")
                    append(type ?: "Products")
                    append(" (${finalList.size})")
                },
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(finalList) { product ->
                    ProductCard(product = product, onClick = {
                        onProductClick(product.productId)
                    })
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selected: String?,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: ${selected ?: "All"}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductDto, onClick: () -> Unit) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {

            val imageUrl = "${AppConfig.base_url}${product.smallPicture ?: ""}"

            ServerImage(
                imageUrl = imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(64.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("License: ${product.license ?: "Unknown"}")
                Text("Value: ${product.value?.let { "$it€" } ?: "-"}")
            }
        }
    }
}