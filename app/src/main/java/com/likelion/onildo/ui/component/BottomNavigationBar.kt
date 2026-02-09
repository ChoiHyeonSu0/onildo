package com.likelion.onildo.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.likelion.onildo.ui.navigation.AppDestinations

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.background
        )

        NavigationBarItem(
            selected = currentRoute == AppDestinations.STATS,
            onClick = { onTabSelected(AppDestinations.STATS) },
            icon = { Icon(Icons.Default.Home, contentDescription = "통계") },
            label = { Text("통계") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == AppDestinations.HOME,
            onClick = { onTabSelected(AppDestinations.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "홈") },
            label = { Text("홈") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == AppDestinations.ARCHIVE,
            onClick = { onTabSelected(AppDestinations.ARCHIVE) },
            icon = { Icon(Icons.Default.Home, contentDescription = "회고") },
            label = { Text("회고") },
            colors = itemColors
        )
    }
}
