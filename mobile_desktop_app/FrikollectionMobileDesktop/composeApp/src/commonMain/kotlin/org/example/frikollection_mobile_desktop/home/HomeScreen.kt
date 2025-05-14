package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = remember { HomeViewModel() },
    onNavigateToList: (String, String?) -> Unit, // (type, status)
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val carouselImages by viewModel.carouselImages.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    val productTypes by viewModel.productTypes.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                showBack = false,
                showSearch = true,
                onSearch = { /* Acció per anar a SearchScreen */ }
            )
        },
        bottomBar = {
            AppFooter(
                selectedBottomItem = selectedBottomItem,
                onBottomItemSelected = onBottomItemSelected
            )
        }
    ) { paddingValues ->

        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            val maxContentWidth = if (maxWidth < 601.dp) maxWidth else 600.dp

            Column(
                modifier = Modifier
                    .width(maxContentWidth)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // CARROUSEL amb imatges carregades
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (carouselImages.isNotEmpty()) {
                        ServerImage(
                            imageUrl = carouselImages[currentIndex],
                            contentDescription = "Carroussel image",
                            modifier = Modifier.fillMaxSize()
                        )

                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(8.dp)
                                .clickable {
                                    currentIndex = if (currentIndex > 0) currentIndex - 1 else carouselImages.lastIndex
                                }
                        )

                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .clickable {
                                    currentIndex = if (currentIndex < carouselImages.lastIndex) currentIndex + 1 else 0
                                }
                        )
                    } else {
                        Text("Carregant imatges...", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                HomeItemCard(
                    imageUrl = "${AppConfig.base_url}/images/uploads/logo/logo_all_products.png",
                    title = "All Products",
                    subtitle = "${state.allProducts.size} PRODUCTS",
                    onClick = { onNavigateToList("all", null) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                productTypes.forEach { type ->
                    ProductTypeSection(
                        typeName = type,
                        products = state.allProducts.filter { it.productType?.typeName.equals(type, ignoreCase = true) },
                        onNavigateToList = onNavigateToList
                    )
                }
            }
        }
    }
}

fun getSectionImage(type: String, products: List<ProductDto>): String {
    val firstImage = products.firstOrNull()?.smallPicture
    val baseUrl = AppConfig.base_url

    return when (type.lowercase()) {
        "tcg" -> "${baseUrl}/images/uploads/logo/logo_tcg.png"
        "funko", "figure" -> {
            if (!firstImage.isNullOrBlank()) "$baseUrl$firstImage"
            else "$baseUrl/images/uploads/logo/logo_${type.lowercase()}.png"
        }
        else -> "$baseUrl/images/uploads/logo/logo_default.png"
    }
}

@Composable
fun ProductTypeSection(
    typeName: String,
    products: List<ProductDto>,
    onNavigateToList: (String, String?) -> Unit
) {
    Text(
        text = typeName.uppercase(),
        style = MaterialTheme.typography.subtitle1.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = 4.dp)
    )

    val imageUrl = getSectionImage(typeName, products)

    val catalogCount = products.size
    val comingSoonCount = products.count { it.status.equals("Coming Soon", ignoreCase = true) }
    val vaultedCount = products.count { it.status.equals("Vaulted", ignoreCase = true) }
    val availableCount = products.count { it.status.equals("Available", ignoreCase = true) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        HomeItemCard(
            imageUrl = imageUrl,
            title = "Catalog",
            subtitle = "$catalogCount PRODUCTS"
        ) { onNavigateToList(typeName, null) }

        HomeItemCard(
            imageUrl = imageUrl,
            title = "Coming Soon",
            subtitle = "$comingSoonCount PRODUCTS"
        ) { onNavigateToList(typeName, "Coming Soon") }

        HomeItemCard(
            imageUrl = imageUrl,
            title = "Vaulted",
            subtitle = "$vaultedCount PRODUCTS"
        ) { onNavigateToList(typeName, "Vaulted") }

        HomeItemCard(
            imageUrl = imageUrl,
            title = "Available",
            subtitle = "$availableCount PRODUCTS"
        ) { onNavigateToList(typeName, "Available") }
    }
}
