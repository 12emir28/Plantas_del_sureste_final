package com.example.plantas.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.plantas.Actividad2
import com.example.plantas.ui.navigation.Screen

data class NavItem(
    val route: String?,
    val icon: ImageVector,
    val label: String,
    val isExternalActivity: Boolean = false
)

@Composable
fun BottomNavBar(navController: NavController) {
    val context = LocalContext.current
    val items = listOf(
        NavItem(Screen.Home.route, Icons.Filled.Home, "Inicio"),
        NavItem(Screen.Catalog.route, Icons.Filled.MenuBook, "Catálogo"),
        NavItem(Screen.Symptoms.route, Icons.Filled.MedicalServices, "Síntomas"),
        NavItem(Screen.Remedies.route, Icons.Filled.Science, "Remedios"),
        NavItem(Screen.Favorites.route, Icons.Filled.Favorite, "Favoritas"),
        // Botón nuevo para el video
        NavItem(null, Icons.Filled.PlayCircle, "Video", isExternalActivity = true)
    )

    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = current == item.route,
                onClick = {
                    if (item.isExternalActivity) {
                        val intent = Intent(context, Actividad2::class.java).apply {
                            if (context !is Activity) {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        }
                        context.startActivity(intent)
                    } else {
                        item.route?.let { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}