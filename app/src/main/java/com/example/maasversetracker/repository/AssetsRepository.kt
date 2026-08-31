package com.example.maasversetracker.repository

import android.content.Context
import com.example.maasversetracker.model.Book
import com.example.maasversetracker.model.Character
import kotlinx.serialization.json.Json

//Clase para conseguir los datos de los archivos Json generados
class AssetsRepository(private val context: Context) {

    //Configuracion para leer los archivos
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    //Obtener la lista de el archivo de libros
    fun getBooks(): List<Book> {
        //Se abre el archivo para leer su contenido
        val text = context.assets.open("data/libros.json")
            .bufferedReader()
            .use { it.readText() }

        //Conversion del contenido del archivo en lista de datos
        return json.decodeFromString(text)
    }

    //Obtener la lista de el archivo de personajes
    fun getCharacters(): List<Character> {
        val text = context.assets.open("data/personajes.json")
            .bufferedReader()
            .use { it.readText() }
        return json.decodeFromString(text)
    }
}