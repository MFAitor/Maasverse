package com.example.maasversetracker.repository

import android.content.Context
import com.example.maasversetracker.model.Book
import com.example.maasversetracker.model.Character
import kotlinx.serialization.json.Json

class AssetsRepository(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun getBooks(): List<Book> {
        val text = context.assets.open("data/libros.json")
            .bufferedReader()
            .use { it.readText() }
        return json.decodeFromString(text)
    }

    fun getCharacters(): List<Character> {
        val text = context.assets.open("data/personajes.json")
            .bufferedReader()
            .use { it.readText() }
        return json.decodeFromString(text)
    }
}