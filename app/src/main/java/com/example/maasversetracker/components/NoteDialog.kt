package com.example.maasversetracker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.maasversetracker.model.Book
import com.example.maasversetracker.model.Note

//Ventana emergente para la creacion de notas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDialog(
    books: List<Book>,
    existingNote: Note? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, bookId: Int?, page: Int?) -> Unit
) {
    //Variables que guardan todos los datos de las notas
    var title by rememberSaveable { mutableStateOf(existingNote?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(existingNote?.description ?: "") }
    var selectedBookId by rememberSaveable { mutableStateOf(existingNote?.bookId) }
    var pageText by rememberSaveable { mutableStateOf(existingNote?.page?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    //Variable que controla si el selector de libros esta abierto
    val selectedBookTitle = books.find { it.id == selectedBookId }?.title ?: "— Ninguno —"

    //Ventana para introducir los datos de la nota
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existingNote == null) "Nueva nota" else "Editar nota") },
        text = {
            Column {
                //Campo para titulo
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                //Campo para descripcion de la nota
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Selector de libro
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {

                    //Campo de muestra de libro seleccionado
                    OutlinedTextField(
                        value = selectedBookTitle,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Libro") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    //Lista desplegable de todos los libros
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        //Posibilidad de asociar la nota sin seleccionar ninguna libro en concreto
                        DropdownMenuItem(
                            text = { Text("— Ninguno —") },
                            onClick = {
                                selectedBookId = null
                                expanded = false
                            }
                        )

                        //Se recorren los libros para cargarlos en la lista
                        books.forEach { book ->
                            DropdownMenuItem(
                                text = { Text(book.title) },
                                onClick = {
                                    selectedBookId = book.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                //Campo para introducir la pagina
                OutlinedTextField(
                    value = pageText,
                    //Solo es posible escribir numeros en este campo
                    onValueChange = { pageText = it.filter { c -> c.isDigit() } },
                    label = { Text("Página (opcional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        //Guardar la nota
        confirmButton = {
            Button(
                onClick = {
                    //Se cromprueba que por lo menos la nota tenga titulo
                    if (title.isNotBlank()) {
                        onSave(
                            title.trim(),
                            description.trim(),
                            selectedBookId,
                            pageText.toIntOrNull()
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Guardar")
            }
        },

        //Opcion de cerrar la creacion de la nota sin completar los datos
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}