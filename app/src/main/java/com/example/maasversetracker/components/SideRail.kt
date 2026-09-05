package com.example.maasversetracker.components


import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.maasversetracker.screens.Screen

@Composable
fun SideRail(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationRail {
        Screen.entries.forEach { screen ->
            NavigationRailItem(
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) },
                icon = {
                    Icon(screen.icon, contentDescription = screen.title)
                },
                label = { Text(screen.title) }
            )
        }
    }
}