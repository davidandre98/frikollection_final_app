package org.example.frikollection_mobile_desktop.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import org.example.frikollection_mobile_desktop.ui.ServerImage

@Composable
fun ProductDetailScreen(
    productId: String,
    onBack: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val viewModel = remember { HomeViewModel() }
    val state by viewModel.uiState.collectAsState()

    val product = state.allProducts.find { it.productId == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Frikollection", fontSize = 20.sp)
                }
                IconButton(onClick = { /* Cerca futura */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        },
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

        if (product != null) {

            // RECORDAR CANVIAR URL QUAN S'EXECUTA EL SERVE
            val bigImageUrl = "${AppConfig.base_url}${product.bigPicture ?: ""}"
            val smallImageUrl = "${AppConfig.base_url}${product.smallPicture ?: ""}"

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imatge gran
                ServerImage(
                    imageUrl = bigImageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )

                // Llicència
                Text(product.license ?: "Unknown License", fontSize = 14.sp)

                // Nom
                Text(product.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                // Valor
                Text(
                    "Trending Value: ${product.value?.let { "$it€" } ?: "-"}",
                    fontSize = 16.sp,
                    color = Color(0xFF1976D2)
                )

                // Botons
                Button(
                    onClick = { /* Afegir a la col·lecció */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ADD TO COLLECTION")
                }

                Button(
                    onClick = { /* Afegir a wishlist */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ADD TO WISHLIST")
                }

                Divider()

                // Títol secció
                Text(
                    "PRODUCT DETAILS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Imatge petita
                ServerImage(
                    imageUrl = smallImageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )

                // Detalls
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("TYPE: ${product.productType?.typeName ?: "-"}")
                    Text("SUBTYPE: ${product.subtype ?: "-"}")
                    Text("RELEASE: ${product.release ?: "-"}")
                    Text("STATUS: ${product.status ?: "-"}")
                    Text("ITEM NUMBER: ${product.itemNumber ?: "-"}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "SEE MORE: ${product.license ?: "-"}",
                        color = Color(0xFF1976D2)
                    )

                    Text(
                        "EXCLUSIVES: ${product.supertype ?: "-"}",
                        color = Color(0xFF1976D2)
                    )
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