package org.example.frikollection_mobile_desktop.ui.cardview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.ui.ServerImage

@Composable
fun ProductCard(
    product: ProductDto,
    onClick: () -> Unit,
    isInWishlist: Boolean,
    onWishlistClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val isShortName = product.name.length <= 18

    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUrl = "${AppConfig.base_url}${product.smallPicture ?: ""}"

            ServerImage(
                imageUrl = imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.license ?: "License Unknown",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Value: ${product.value?.let { String.format("%.2f €", it) } ?: "No price data"}",
                    fontSize = 10.sp,
                    color = Color(0xFF1976D2)
                )
            }

            if (isShortName) {
                Spacer(modifier = Modifier.height(28.dp))
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE0E0E0))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isInWishlist) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Add to wishlist",
                    tint = if (isInWishlist) Color.Red else Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onWishlistClick() }
                )

                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add to collection",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onAddClick() }
                )
            }
        }
    }
}