package org.example.frikollection_mobile_desktop.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.models.ProductFilter

@Composable
fun FilterChip(text: String?, onRemove: () -> Unit) {
    if (!text.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(color = Color.Black, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.White,
                modifier = Modifier
                    .size(12.dp)
                    .clickable { onRemove() }
            )
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.body2,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

fun hasActiveFilters(filter: ProductFilter): Boolean {
    return !filter.productType.isNullOrBlank() ||
            !filter.subtype.isNullOrBlank() ||
            !filter.supertype.isNullOrBlank() ||
            !filter.status.isNullOrBlank() ||
            !filter.license.isNullOrBlank() ||
            filter.tags.isNotEmpty()
}