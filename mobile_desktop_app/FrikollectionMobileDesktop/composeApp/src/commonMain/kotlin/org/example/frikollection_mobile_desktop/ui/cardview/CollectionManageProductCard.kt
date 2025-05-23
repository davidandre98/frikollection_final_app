package org.example.frikollection_mobile_desktop.ui.cardview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.api.AppConfig.base_url
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.ui.ServerImage
import org.example.frikollection_mobile_desktop.utils.isAndroidPlatform

@Composable
fun CollectionManageProductCard(
    product: ProductDto,
    isAddMode: Boolean,
    isSelected: Boolean,
    onToggleSelected: (ProductDto) -> Unit,
    onProductClick: (ProductDto) -> Unit
) {
    val imageSize = if (isAndroidPlatform()) 56.dp else 72.dp

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = when {
            !isAddMode && isSelected -> Color(0xFFFFCDD2)
            isAddMode && isSelected -> Color(0xFFC8E6C9)
            else -> Color.White
        },
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onProductClick(product) }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ServerImage(
                imageUrl = "$base_url${product.smallPicture}",
                contentDescription = product.name,
                modifier = Modifier
                    .size(imageSize)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = product.license ?: "License Unknown", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(2.dp))

                Text(text = product.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Value: ${product.value?.let { String.format("%.2f €", it) } ?: "No price data"}",
                    fontSize = 10.sp,
                    color = Color(0xFF1976D2)
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (!isAddMode || isSelected) Color.Red else Color(0xFF1976D2),
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onToggleSelected(product) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!isAddMode || isSelected) Icons.Default.Delete else Icons.Default.Add,
                    contentDescription = if (!isAddMode || isSelected) "Remove" else "Add",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}