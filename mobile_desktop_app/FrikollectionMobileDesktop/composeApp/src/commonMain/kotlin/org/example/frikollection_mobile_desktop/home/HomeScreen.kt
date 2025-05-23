package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.filter.FilterOptionsCache
import org.example.frikollection_mobile_desktop.ui.cardview.HomeItemCard
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.ui.ServerImage
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.utils.getCardSectionImage
import org.example.frikollection_mobile_desktop.utils.isAndroidPlatform

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToList: (String, String?) -> Unit, // (type, status)
    onSearch: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val carouselHeight = if (isAndroidPlatform()) 180.dp else 360.dp
    val carouselImages by viewModel.carouselImages.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    val productTypes by viewModel.productTypes.collectAsState()

    LaunchedEffect(state.allProducts) {
        if (FilterOptionsCache.productTypes.isEmpty()) {
            FilterOptionsCache.loadFromProducts(state.allProducts)
        }
    }

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = false,
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
        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            val maxContentWidth = if (maxWidth < 801.dp) maxWidth else 800.dp

            Column(
                modifier = Modifier
                    .width(maxContentWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // CAROUSEL amb imatges carregades
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(carouselHeight),
                    contentAlignment = Alignment.Center
                ) {
                    if (carouselImages.isNotEmpty()) {
                        ServerImage(
                            imageUrl = carouselImages[currentIndex],
                            contentDescription = "Carousel image",
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(48.dp)
                                .align(Alignment.CenterStart)
                                .clickable {
                                    currentIndex = if (currentIndex > 0) currentIndex - 1 else carouselImages.lastIndex
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Previous",
                                tint = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(48.dp)
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    currentIndex = if (currentIndex < carouselImages.lastIndex) currentIndex + 1 else 0
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White
                            )
                        }
                    } else {
                        Text("Carregant imatges...", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HomeItemCard(
                    imageUrl = "${AppConfig.base_url}/images/uploads/logo/logo_default.png",
                    title = "All Products",
                    subtitle = "${state.allProducts.size} PRODUCTS",
                    onClick = { onNavigateToList("All Catalog", null) }
                )

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

@Composable
fun ProductTypeSection(
    typeName: String,
    products: List<ProductDto>,
    onNavigateToList: (String, String?) -> Unit
) {
    Text(
        text = typeName.uppercase(),
        style = MaterialTheme.typography.subtitle1.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 25.dp),
        textAlign = TextAlign.Start
    )

    val imageCardViewSize = if (isAndroidPlatform()) 48.dp else 72.dp

    val catalogCount = products.size
    val comingSoonCount = products.count { it.status.equals("Coming Soon", ignoreCase = true) }
    val vaultedCount = products.count { it.status.equals("Vaulted", ignoreCase = true) }
    val availableCount = products.count { it.status.equals("Available", ignoreCase = true) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        HomeItemCard(
            imageUrl = getCardSectionImage(typeName, products, null),
            title = "Catalog",
            subtitle = "$catalogCount PRODUCTS",
            imageSize = imageCardViewSize
        ) { onNavigateToList(typeName, null) }

        Spacer(modifier = Modifier.height(4.dp))

        HomeItemCard(
            imageUrl = getCardSectionImage(typeName, products, "Coming Soon"),
            title = "Coming Soon",
            subtitle = "$comingSoonCount PRODUCTS",
            imageSize = imageCardViewSize
        ) { onNavigateToList(typeName, "Coming Soon") }

        Spacer(modifier = Modifier.height(4.dp))

        HomeItemCard(
            imageUrl = getCardSectionImage(typeName, products, "Vaulted"),
            title = "Vaulted",
            subtitle = "$vaultedCount PRODUCTS",
            imageSize = imageCardViewSize
        ) { onNavigateToList(typeName, "Vaulted") }

        Spacer(modifier = Modifier.height(4.dp))

        HomeItemCard(
            imageUrl = getCardSectionImage(typeName, products, "Available"),
            title = "Available",
            subtitle = "$availableCount PRODUCTS",
            imageSize = imageCardViewSize
        ) { onNavigateToList(typeName, "Available") }
    }
}
