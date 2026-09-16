package com.example.maasversetracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.maasversetracker.data.characterImageRequest
import com.example.maasversetracker.model.Character
import com.example.maasversetracker.viewmodel.MainViewModel
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults

//Ventana con la lista de personajes
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CharactersScreen(viewModel: MainViewModel) {

    //Variables para obtener los personajes
    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    //Guardar el texto de la barra de busqueda
    var searchQuery by remember { mutableStateOf("") }

    //Personajes pendientes de confirmar como vistos
    var pendingCharacter by remember { mutableStateOf<Character?>(null) }

    //Personajes ya revelados
    val revealedIds by viewModel.revealedCharacters.collectAsState()

    //Personaje cuyo estado descripción se está mostrando
    var showDetail by remember { mutableStateOf<Character?>(null) }

    //Variables para filtrar los personajes por sagas
    val books by viewModel.books.collectAsState()

    val sagaOptions = listOf("Todas", "ACOTAR", "Trono de Cristal", "Ciudad Medialuna")
    val selectedSaga by viewModel.selectedSaga.collectAsState()
    var sagaMenuExpanded by remember { mutableStateOf(false) }

    // Filtrado por nombre y libro en el que aparecen
    val filteredCharacters = characters
        .filter { character ->
            val matchesName = searchQuery.isBlank() ||
                    character.name.contains(searchQuery, ignoreCase = true)

            val matchesSaga = if (selectedSaga == "Todas") {
                true
            } else {
                val seriesBookIds = books
                    .filter { it.series == selectedSaga }
                    .map { it.id }
                    .toSet()

                character.firstBookId in seriesBookIds ||
                        character.books.any { it in seriesBookIds }
            }

            matchesName && matchesSaga
        }
        .sortedBy { it.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Personajes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Pulsa para revelar (spoiler)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Barra de búsqueda por nombre
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar personaje...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Barra para seleccionar la saga a la que pertenecen los personajes
        ExposedDropdownMenuBox(
            expanded = sagaMenuExpanded,
            onExpandedChange = { sagaMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedSaga,
                onValueChange = {},
                readOnly = true,
                label = { Text("Saga") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sagaMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = sagaMenuExpanded,
                onDismissRequest = { sagaMenuExpanded = false }
            ) {
                sagaOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.setSelectedSaga(option)
                            sagaMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Mostrar mensjae mientras se cargan personajes
        if (isLoading) {
            Text("Cargando personajes...")

        //Mensaje para caso no personaje no existente
        } else if (filteredCharacters.isEmpty()) {
            Text(
                text = "No se encontraron personajes",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        } else {
            //Personajes filtrados
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredCharacters) { character ->
                    //Se comprueba si el personaja ha sido revelado
                    val isRevealed = character.id in revealedIds

                    CharacterItem(
                        character = character,
                        isRevealed = isRevealed,
                        onClick = {
                            if (isRevealed) {
                                showDetail = character
                            } else {
                                pendingCharacter = character
                            }
                        }
                    )
                }
            }
        }
    }

    //Diálogo de aviso de spoiler
    pendingCharacter?.let { character ->
        AlertDialog(
            onDismissRequest = { pendingCharacter = null },
            title = { Text("¿Estás preparado para el spoiler?") },
            text = {
                Text("Vas a revelar la información de:\n\n${character.name}")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.revealCharacter(character.id)
                    pendingCharacter = null
                    showDetail = character
                }) {
                    Text("Sí, revelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingCharacter = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    //Diálogo de detalle (cuando ya está revelado)
    showDetail?.let { character ->
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { showDetail = null },
            title = { Text(character.name) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Imagen del personaje
                    AsyncImage(
                        model = characterImageRequest(context, character.image),
                        contentDescription = character.name,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = character.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Primera aparición: ${character.firstBookTitle}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            //Botón para cerrar la descripcion del personaje
            confirmButton = {
                TextButton(onClick = { showDetail = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

//Elemento para representar cada personaje
@Composable
private fun CharacterItem(
    character: Character,
    isRevealed: Boolean,
    onClick: () -> Unit
) {

    //Se obtiene el context para poder mostrar la imagen del personaje
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {if (isRevealed) {
            AsyncImage(
                model = characterImageRequest(context, character.image),
                contentDescription = character.name,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Card(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isRevealed) character.name.take(1) else character.id.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = character.firstBookTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //Para indicar el estado del personaje (revelado u oculto)
            Text(
                text = if (isRevealed) "Revelado" else "🔒",
                style = MaterialTheme.typography.labelSmall,
                color = if (isRevealed) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}