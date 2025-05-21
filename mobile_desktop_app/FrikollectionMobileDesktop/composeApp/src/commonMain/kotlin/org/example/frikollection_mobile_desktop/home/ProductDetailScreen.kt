package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.search.SearchViewModel
import org.example.frikollection_mobile_desktop.ui.ServerImage
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader

@Composable
fun ProductDetailScreen(
    productId: String,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    onNavigateToSearchScreen: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by homeViewModel.uiState.collectAsState()
    val product = state.allProducts.find { it.productId == productId }

    var showDetails by remember { mutableStateOf(false) }

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

        if (product != null) {

            val bigImageUrl = "${AppConfig.base_url}${product.bigPicture ?: ""}"
            val smallImageUrl = "${AppConfig.base_url}${product.smallPicture ?: ""}"

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ServerImage(
                    imageUrl = bigImageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(512.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(product.license ?: "Unknown License", fontSize = 18.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(product.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Trending Value: ${product.value?.let { "%.2f€".format(it) } ?: "No price data"}",
                        fontSize = 14.sp,
                        color = Color(0xFF1976D2)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* TODO: Add to collection */ },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add to collection",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ADD TO COLLECTION", color = Color.White)
                }

                Button(
                    onClick = { /* TODO: Add to wishlist */ },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to wishlist",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ADD TO WISHLIST", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { showDetails = !showDetails }
                        .background(Color.Black),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "PRODUCT DETAILS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Icon(
                            imageVector = if (showDetails) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowRight,
                            contentDescription = "Toggle Details",
                            tint = Color.White
                        )
                    }
                }

                if (showDetails) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ServerImage(
                                imageUrl = smallImageUrl,
                                contentDescription = product.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(224.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row {
                                Text("TYPE: ", fontWeight = FontWeight.Bold)
                                Text(product.productType?.typeName ?: "-")
                            }
                            Row {
                                Text("SUBTYPE: ", fontWeight = FontWeight.Bold)
                                Text(product.subtype ?: "-")
                            }
                            Row {
                                Text("RELEASE: ", fontWeight = FontWeight.Bold)
                                Text(product.release ?: "-")
                            }
                            Row {
                                Text("STATUS: ", fontWeight = FontWeight.Bold)
                                Text(product.status ?: "-")
                            }
                            Row {
                                Text("ITEM NUMBER: ", fontWeight = FontWeight.Bold)
                                Text(product.itemNumber.toString() ?: "-")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text("SEE MORE: ", fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(
                                    text = product.license ?: "",
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.clickable {
                                        searchViewModel.onFilterChange(ProductFilter(license = product.license))
                                        onNavigateToSearchScreen()
                                    }
                                )
                            }
                            Row {
                                Text("TAGS: ", fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(
                                    text = product.supertype ?: "",
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.clickable {
                                        searchViewModel.onFilterChange(ProductFilter(tags = listOfNotNull(product.supertype)))
                                        onNavigateToSearchScreen()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Product not found", color = Color.Gray)
            }
        }
    }
}