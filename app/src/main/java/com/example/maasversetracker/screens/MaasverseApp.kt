package com.example.maasversetracker.screens

import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.maasversetracker.components.BottomBar
import com.example.maasversetracker.ui.theme.MaasverseTrackerTheme
import com.example.maasversetracker.viewmodel.MainViewModel

//Agrupacion de las llamadas de cada una de las pantallas
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MaasverseApp(activity: Activity, viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //Variables necesarias para ajustar a la pantalla del dispositivo
    val widthClass = calculateWindowSizeClass(activity).widthSizeClass
    val isWide = widthClass != WindowWidthSizeClass.Compact

    //Variable para controlar el tema
    var isDarkTheme by remember { mutableStateOf(true) }

    //Variable para cambiar de una pestaña a otra
    val onNavigate: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    MaasverseTrackerTheme(darkTheme = isDarkTheme) {
        Scaffold(
            bottomBar = {
                if (!isWide) {
                    BottomBar(
                        currentRoute = currentRoute,
                        onNavigate = onNavigate
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWide) {
                    BottomBar(
                        currentRoute = currentRoute,
                        onNavigate = onNavigate
                    )
                }

                AppNavHost(
                    navController = navController,
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
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
                onToggleTheme = onToggleTheme
            )
        }
    }
}