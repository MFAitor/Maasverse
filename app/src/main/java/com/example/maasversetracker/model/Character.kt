package com.example.maasversetracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val image: String,
    val firstBookId: Int,
    val firstBookTitle: String,
    val books: List<Int>,
    val description: String
)