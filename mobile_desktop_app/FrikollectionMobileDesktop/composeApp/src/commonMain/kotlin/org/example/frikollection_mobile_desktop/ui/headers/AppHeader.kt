package org.example.frikollection_mobile_desktop.ui.headers

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.ui.ServerImage

@Composable
fun AppHeader(
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    showSearch: Boolean = false,
    onSearch: (() -> Unit)? = null
) {
    TopAppBar(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBack && onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // CANVIAR URL AL INICIAR SERVIDOR
            ServerImage(
                imageUrl = "${AppConfig.base_url}/images/uploads/logo/logo_frikollection.png",
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(32.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (showSearch && onSearch != null) {
                IconButton(
                    onClick = { onSearch },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }
        }
    }
}