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

@Composable
fun CharactersScreen(viewModel: MainViewModel) {
    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var revealedIds by remember { mutableStateOf(setOf<Int>()) }
    var pendingCharacter by remember { mutableStateOf<Character?>(null) }
    var showDetail by remember { mutableStateOf<Character?>(null) }

    // Filtrado por nombre
    val filteredCharacters = characters
        .filter {
            searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true)
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

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar personaje...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Text("Cargando personajes...")
        } else if (filteredCharacters.isEmpty()) {
            Text(
                text = "No se encontraron personajes",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredCharacters) { character ->
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
    // Diálogo de aviso de spoiler
    pendingCharacter?.let { character ->
        AlertDialog(
            onDismissRequest = { pendingCharacter = null },
            title = { Text("¿Estás preparado para el spoiler?") },
            text = {
                Text("Vas a revelar la información de:\n\n${character.name}")
            },
            confirmButton = {
                TextButton(onClick = {
                    revealedIds = revealedIds + character.id
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

    // Diálogo de detalle (cuando ya está revelado)
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
                    // Imagen del personaje
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
            confirmButton = {
                TextButton(onClick = { showDetail = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun CharacterItem(
    character: Character,
    isRevealed: Boolean,
    onClick: () -> Unit
) {

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

            Text(
                text = if (isRevealed) "Revelado" else "🔒",
                style = MaterialTheme.typography.labelSmall,
                color = if (isRevealed) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}