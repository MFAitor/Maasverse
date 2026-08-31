package com.example.maasversetracker.model

import kotlinx.serialization.Serializable

//Data class con los datos guardados de los libros
@Serializable
data class Book (
    val id: Int,
    val title: String,
    val isbn: String,
    val originalTitle: String,
    val series: String,
    val type: String,
    val pages: Int,
    val releaseDate: String,
    val cover: String,
    val description: String
)