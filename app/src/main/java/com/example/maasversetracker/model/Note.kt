package com.example.maasversetracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long,
    val title: String,
    val description: String = "",
    val bookId: Int? = null,
    val page: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)