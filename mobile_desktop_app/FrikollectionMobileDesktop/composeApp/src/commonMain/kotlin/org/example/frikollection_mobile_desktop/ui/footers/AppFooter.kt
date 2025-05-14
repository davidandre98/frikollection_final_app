package org.example.frikollection_mobile_desktop.ui.footers

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import org.example.frikollection_mobile_desktop.BottomMenuItem

@Composable
fun AppFooter(
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    BottomNavigation {
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Home,
            onClick = { onBottomItemSelected(BottomMenuItem.Home) },
            label = { Text("Home") },
            icon = {}
        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Discover,
            onClick = { onBottomItemSelected(BottomMenuItem.Discover) },
            label = { Text("Discover") },
            icon = {}
        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Search,
            onClick = { onBottomItemSelected(BottomMenuItem.Search) },
            label = { Text("Search") },
            icon = {}
        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Lists,
            onClick = { onBottomItemSelected(BottomMenuItem.Lists) },
            label = { Text("Lists") },
            icon = {}
        )
        BottomNavigationItem(
            selected = selectedBottomItem == BottomMenuItem.Account,
            onClick = { onBottomItemSelected(BottomMenuItem.Account) },
            label = { Text("Account") },
            icon = {}
        )
    }
}