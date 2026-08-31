package com.example.maasversetracker.model

import kotlinx.serialization.Serializable

//Data class con los datos guardados de las notas
@Serializable
data class Note(
    val id: Long,
    val title: String,
    val description: String = "",
    val bookId: Int? = null,
    val page: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)