package com.example.maasversetracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maasversetracker.model.Book
import com.example.maasversetracker.model.Character
import com.example.maasversetracker.repository.AssetsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.maasversetracker.model.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AssetsRepository(application)
    private val prefs = application.getSharedPreferences("maas_prefs", Context.MODE_PRIVATE)

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters.asStateFlow()

    private val _ratings = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val ratings: StateFlow<Map<Int, Int>> = _ratings.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //Aquellas funciones que lanzamos con el inicio de la app
    init {
        loadData()
        loadRatings()
        loadNotes()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _books.value = repository.getBooks()
                _characters.value = repository.getCharacters()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadRatings() {
        val all = prefs.all
        val map = mutableMapOf<Int, Int>()
        all.forEach { (key, value) ->
            if (key.startsWith("rating_") && value is Int) {
                val bookId = key.removePrefix("rating_").toIntOrNull()
                if (bookId != null) map[bookId] = value
            }
        }
        _ratings.value = map
    }

    fun setRating(bookId: Int, rating: Int) {
        prefs.edit().putInt("rating_$bookId", rating).apply()
        _ratings.value = _ratings.value.toMutableMap().apply {
            put(bookId, rating)
        }
    }

    fun clearRating(bookId: Int) {
        prefs.edit().remove("rating_$bookId").apply()
        _ratings.value = _ratings.value.toMutableMap().apply {
            remove(bookId)
        }
    }

    fun getRating(bookId: Int): Int {
        return _ratings.value[bookId] ?: 0
    }

    //Funcion que carga las notas
    private fun loadNotes() {
        val jsonString = prefs.getString("notes_json", null) ?: return
        try {
            _notes.value = Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Funcion para guardar las notas
    private fun saveNotes() {
        val jsonString = Json.encodeToString(_notes.value)
        prefs.edit().putString("notes_json", jsonString).apply()
    }

    //Funcion con la que se añaden nuevas notas
    fun addNote(title: String, description: String, bookId: Int?, page: Int?) {
        val note = Note(
            id = System.currentTimeMillis(),
            title = title,
            description = description,
            bookId = bookId,
            page = page
        )
        _notes.value = _notes.value + note
        saveNotes()
    }

    //Funcion para borrar notas
    fun deleteNote(noteId: Long) {
        _notes.value = _notes.value.filter { it.id != noteId }
        saveNotes()
    }

    //Funcion para modificar notas
    fun updateNote(note: Note) {
        _notes.value = _notes.value.map {
            if (it.id == note.id) note else it
        }
        saveNotes()
    }

    //Funcion de reseteo de la app
    fun resetAllData() {
        prefs.edit().clear().apply()
        _ratings.value = emptyMap()
        _notes.value = emptyList()
    }
}