package org.example.frikollection_mobile_desktop.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.frikollection_mobile_desktop.account.NotificationViewModel
import kotlinx.coroutines.launch

@Composable
fun NotificationDialog(
    viewModel: NotificationViewModel,
    onClose: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onClose) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.6f)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D47A1), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Notifications", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // NOTIFICATION LIST
            LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                items(notifications) { notification ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color(0xFFF5F5F5),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = notification.collectionName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${notification.followerNickname} follow this collection",
                                    fontSize = 12.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = notification.createdAt.takeLast(8).dropLast(3),
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                                IconButton(onClick = {
                                    scope.launch { viewModel.deleteNotification(notification.notificationId) }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Notification",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // DELETE ALL
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { scope.launch { viewModel.deleteAllNotifications() } },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Delete All", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // FOOTER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D47A1), RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}