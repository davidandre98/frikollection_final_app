package org.example.frikollection_mobile_desktop.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.frikollection_mobile_desktop.BottomMenuItem

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel,
    notificationViewModel: NotificationViewModel,
    onSearch: () -> Unit,
    onHelpItemClick: (String) -> Unit,
    onLogout: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val state by accountViewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.profile == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error loading user profile", color = Color.Red)
            }
        }

        else -> {
            val user = state.profile!!
            AccountScreenContent(
                accountViewModel = accountViewModel,
                notificationViewModel = notificationViewModel,
                user = user,
                onSearch = onSearch,
                onHelpItemClick = onHelpItemClick,
                onLogout = onLogout,
                selectedBottomItem = selectedBottomItem,
                onBottomItemSelected = onBottomItemSelected
            )
        }
    }
}