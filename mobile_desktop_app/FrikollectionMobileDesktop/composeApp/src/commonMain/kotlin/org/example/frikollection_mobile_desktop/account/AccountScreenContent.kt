package org.example.frikollection_mobile_desktop.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.api.AppConfig.base_url
import org.example.frikollection_mobile_desktop.models.user.UserProfileDto
import org.example.frikollection_mobile_desktop.ui.ServerImage
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader
import org.example.frikollection_mobile_desktop.ui.user.EditAccountInfoDialog
import org.example.frikollection_mobile_desktop.ui.user.NotificationDialog
import org.example.frikollection_mobile_desktop.ui.user.ShowEditAvatarDialog

@Composable
fun AccountScreenContent(
    accountViewModel: AccountViewModel,
    notificationViewModel: NotificationViewModel,
    user: UserProfileDto,
    onSearch: () -> Unit,
    onHelpItemClick: (String) -> Unit,
    onLogout: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val avatarUrl = "$base_url${user.avatar ?: ""}"
    var showEditAvatarDialog by remember { mutableStateOf(false) }

    // elements update account info
    var showEditInfoDialog by remember { mutableStateOf(false) }

    // elements update biografia
    var isEditing by remember { mutableStateOf(false) }
    var bioText by remember { mutableStateOf(user.biography ?: "") }

    // elements notificacions
    var showNotificationsDialog by remember { mutableStateOf(false) }
    val unreadCount by notificationViewModel.unreadCount.collectAsState()
    val recentNotifications by notificationViewModel.notifications.collectAsState()

    Scaffold(
        backgroundColor = Color(0x66CCCCCC),
        topBar = {
            AppHeader(
                showBack = false,
                showSearch = true,
                onSearch = onSearch
            )
        },
        bottomBar = {
            AppFooter(
                selectedBottomItem = selectedBottomItem,
                onBottomItemSelected = onBottomItemSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("My Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                    .padding(top = 24.dp, bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ServerImage(
                        imageUrl = avatarUrl,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showEditAvatarDialog = true }) {
                        Text("Edit", color = Color(0xFF1976D2))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Account Info", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        TextButton(onClick = { showEditInfoDialog = true }) {
                            Text("EDIT", fontSize = 10.sp, color = Color(0xFF0D47A1))
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        InfoField(label = "First Name", value = user.firstName ?: "-")
                        InfoField(label = "Last Name", value = user.lastName ?: "-")
                        InfoField(label = "User Name", value = user.username)
                        InfoField(label = "Email", value = user.email ?: user.username)
                        InfoField(label = "Password", value = "******")
                        InfoField(label = "Birthday", value = user.birthday ?: "-")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Biography", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        TextButton(onClick = {
                            if (isEditing) {
                                accountViewModel.updateBiography(bioText)
                            } else {
                                bioText = user.biography ?: ""
                            }
                            isEditing = !isEditing
                        }) {
                            Text(if (isEditing) "SAVE" else "EDIT", fontSize = 10.sp, color = Color(0xFF0D47A1))
                        }
                    }

                    OutlinedTextField(
                        value = bioText.ifBlank { "No biography added" },
                        onValueChange = { if (isEditing && it.length <= 200) bioText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        readOnly = !isEditing,
                        enabled = true,
                        maxLines = 5,
                        singleLine = false
                    )

                    if (isEditing) {
                        Text(
                            text = "",
                            fontSize = 10.sp,
                            color = Color(0xFFF5F5F5),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 20.dp, top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notifications", fontSize = 18.sp)
                        TextButton(onClick = { showNotificationsDialog = true }) {
                            Text("VIEW ALL", fontSize = 10.sp, color = Color(0xFF0D47A1))
                        }
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (unreadCount > 0) {
                            recentNotifications.take(3).forEach { notification ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = 2.dp
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .background(Color(0xFFE0E0E0))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(Color.LightGray)
                                        ) {}
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(notification.collectionName ?: "-", fontWeight = FontWeight.Bold)
                                            Text(notification.message)
                                        }
                                        Text(
                                            text = notification.createdAt.substringAfter("T").take(5),
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("No new notifications to read.", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Help",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val helpItems = listOf(
                            "Contact Us",
                            "FAQ",
                            "Privacy Policy & Terms of Use"
                        )

                        helpItems.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onHelpItemClick(item) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item)
                                Text(">", fontWeight = FontWeight.Bold)
                            }

                            if (index < helpItems.lastIndex) {
                                Divider()
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Last login: ${user.lastLogin}", fontSize = 12.sp, color = Color.Gray)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.wrapContentWidth().padding(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text("LOG OUT", color = Color.White)
                }
            }
        }

        if (showEditAvatarDialog) {
            ShowEditAvatarDialog(
                onDismiss = { showEditAvatarDialog = false },
                onAvatarSelected = { bytes, fileName ->
                    accountViewModel.updateAvatar(bytes, fileName)
                    showEditAvatarDialog = false
                }
            )
        }

        if (showEditInfoDialog) {
            EditAccountInfoDialog(
                viewModel = accountViewModel,
                initial = user,
                onDismiss = { showEditInfoDialog = false },
                onSave = { dto -> accountViewModel.updateUserInfo(dto) }
            )
        }

        if (showNotificationsDialog) {
            NotificationDialog(
                viewModel = notificationViewModel,
                onClose = { showNotificationsDialog = false }
            )
        }
    }
}

@Composable
fun InfoField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 12.sp, color = Color.Black)
    }
}