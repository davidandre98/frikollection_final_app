package org.example.frikollection_mobile_desktop.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto

@Composable
fun UpdateCollectionDialog(
    collection: UserCollectionDto,
    onDismiss: () -> Unit,
    onUpdate: (updatedName: String, isPrivate: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(collection.name) }
    var isPrivate by remember { mutableStateOf(collection.private) }
    var showNameError by remember { mutableStateOf(false) }

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
                        text = "Update Collection",
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
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (showNameError && it.isNotBlank()) {
                                showNameError = false
                            }
                        },
                        label = { Text("Collection Name") },
                        isError = showNameError,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (showNameError) {
                        Text(
                            text = "Please enter a collection name.",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Visibility:", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        Spacer(modifier = Modifier.width(24.dp))

                        RadioButton(selected = !isPrivate, onClick = { isPrivate = false })
                        Text("Public", modifier = Modifier.padding(end = 16.dp))

                        RadioButton(selected = isPrivate, onClick = { isPrivate = true })
                        Text("Private")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val createdDate = collection.creationDate
                        Text("Created on: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(createdDate)
                    }
                }

                // FOOTER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .padding(vertical = 8.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", fontSize = 14.sp, color = Color(0xFF0D47A1))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                if (name.isBlank()) {
                                    showNameError = true
                                } else {
                                    onUpdate(name.trim(), isPrivate)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Update Collection",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}