package org.example.frikollection_mobile_desktop.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CollectionPickerDialog(
    product: ProductDto,
    collections: List<UserCollectionDto>,
    onCollectionSelected: (UserCollectionDto) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.Black, RoundedCornerShape(20.dp))
                .padding(1.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(20.dp))
            ) {
                // HEADER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add to Collection",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // BODY
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF2F2F2))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                        items(collections) { collection ->
                            Card(
                                elevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { onCollectionSelected(collection) }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .background(Color.White)
                                ) {
                                    Text(collection.name, style = MaterialTheme.typography.subtitle1)
                                    Text(
                                        text = "${collection.products.size} products",
                                        style = MaterialTheme.typography.caption,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                // FOOTER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {}
            }
        }
    }
}