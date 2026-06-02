package com.example.plantas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantas.PlantasViewModel
import com.example.plantas.ui.components.BottomNavBar
import com.example.plantas.ui.screens.*

sealed class Screen(val route: String) {
    object Home         : Screen("home")
    object Catalog      : Screen("catalog")
    object Symptoms     : Screen("symptoms")
    object Remedies     : Screen("remedies")
    object Favorites    : Screen("favorites")
    object Detail       : Screen("detail")
    object RemedyDetail : Screen("remedy_detail")
    object SymptomDetail: Screen("symptom_detail")
}

val screensWithoutNavBar = listOf(
    Screen.Detail.route, Screen.RemedyDetail.route, Screen.SymptomDetail.route)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: PlantasViewModel = viewModel()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold(bottomBar = {
        if (currentRoute !in screensWithoutNavBar) BottomNavBar(navController)
    }) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Home.route)         { HomeScreen(navController, viewModel) }
            composable(Screen.Catalog.route)      { CatalogScreen(navController, viewModel) }
            composable(Screen.Symptoms.route)     { SymptomsScreen(navController, viewModel) }
            composable(Screen.Remedies.route)     { RemediesScreen(navController, viewModel) }
            composable(Screen.Favorites.route)    { FavoritesScreen(navController, viewModel) }
            composable(Screen.Detail.route)       { PlantDetailScreen(navController, viewModel) }
            composable(Screen.RemedyDetail.route) { RemedyDetailScreen(navController, viewModel) }
            composable(Screen.SymptomDetail.route){ SymptomDetailScreen(navController, viewModel) }
        }
    }
}
