package com.example.maasversetracker.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.maasversetracker.components.BottomBar
import com.example.maasversetracker.ui.theme.MaasverseTrackerTheme
import com.example.maasversetracker.viewmodel.MainViewModel

//Agrupacion de las llamadas de cada una de las pantallas
@Composable
fun MaasverseApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //Variable para controlar el tema
    var isDarkTheme by remember { mutableStateOf(true) }

    MaasverseTrackerTheme(darkTheme = isDarkTheme) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(viewModel = viewModel)
                }
                composable(Screen.Books.route) {
                    BooksScreen(viewModel = viewModel)
                }
                composable(Screen.Characters.route) {
                    CharactersScreen(viewModel = viewModel)
                }
                composable(Screen.Notes.route) {
                    NotesScreen(viewModel = viewModel)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}