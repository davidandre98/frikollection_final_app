package org.example.frikollection_mobile_desktop.ui.footers

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.utils.isAndroidPlatform

@Composable
fun AppFooter(
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    val footerHeight = if (isAndroidPlatform()) 56.dp else 64.dp
    val iconSize = if (isAndroidPlatform()) 24.dp else 36.dp
    val textSize = if (isAndroidPlatform()) 12.sp else 16.sp

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.height(footerHeight),
        elevation = 8.dp
    ) {
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Home,
            onClick = { onBottomItemSelected(BottomMenuItem.Home) },
            label = { Text("Home", fontSize = textSize) },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(iconSize)
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray

        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Discover,
            onClick = { onBottomItemSelected(BottomMenuItem.Discover) },
            label = { Text("Discover", fontSize = textSize) },
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Discover",
                    modifier = Modifier.size(iconSize)
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Search,
            onClick = { onBottomItemSelected(BottomMenuItem.Search) },
            label = { Text("Search", fontSize = textSize) },
            icon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(iconSize)
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Lists,
            onClick = { onBottomItemSelected(BottomMenuItem.Lists) },
            label = { Text("Lists", fontSize = textSize) },
            icon = {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Lists",
                    modifier = Modifier.size(iconSize)
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray

        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Account,
            onClick = { onBottomItemSelected(BottomMenuItem.Account) },
            label = { Text("Account", fontSize = textSize) },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Account",
                    modifier = Modifier.size(iconSize)
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray
        )
    }
}