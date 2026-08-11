package com.example.maasversetracker.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

//Clase de las pantallas que tendra la aplicacion
enum class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    Home("home", "Biblioteca", Icons.Default.Home),
    Books("books", "Libros", Icons.Default.MenuBook),
    Characters("characters", "Personajes", Icons.Default.People),
    Notes("notes", "Notas", Icons.Default.EditNote),
    Settings("settings", "Ajustes", Icons.Default.Settings)
}