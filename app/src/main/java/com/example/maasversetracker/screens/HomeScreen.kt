package com.example.maasversetracker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maasversetracker.model.Book
import com.example.maasversetracker.viewmodel.MainViewModel
import com.example.maasversetracker.R
import com.example.maasversetracker.data.getCoverResource


@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val ratings by viewModel.ratings.collectAsState()
    val readBookIds = ratings.keys

    //Variable para ir sumando notas
    val notes by viewModel.notes.collectAsState()

    val seriesOrder = listOf("ACOTAR", "Trono de Cristal", "Ciudad Medialuna")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Maas Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tu biblioteca de Sarah J. Maas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Contadores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                value = readBookIds.size.toString(),
                label = "Leídos",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = (books.size - readBookIds.size).toString(),
                label = "Pendientes",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = notes.size.toString(),
                label = "Notas",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando biblioteca...")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(28.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                seriesOrder.forEach { series ->
                    val seriesBooks = books.filter { it.series == series }
                    if (seriesBooks.isNotEmpty()) {
                        item {
                            SeriesShelf(
                                seriesName = series,
                                books = seriesBooks,
                                readBookIds = readBookIds
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SeriesShelf(
    seriesName: String,
    books: List<Book>,
    readBookIds: Set<Int>
) {
    val backgroundRes = when (seriesName) {
        "ACOTAR" -> R.drawable.fondo_flores
        "Trono de Cristal" -> R.drawable.fondo_fuego
        "Ciudad Medialuna" -> R.drawable.fondo_noche
        else -> null
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = seriesName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${books.count { it.id in readBookIds }}/${books.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Estantería con imagen de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Imagen de fondo
            if (backgroundRes != null) {
                Image(
                    painter = painterResource(id = backgroundRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Capa oscura suave para que destaquen los libros
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF2C241B))
                )
            }

            // Libros encima
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                items(books.filter { it.id in readBookIds }) { book ->
                    BookSpine(book = book)
                }
            }
        }
    }
}

@Composable
private fun BookSpine(
    book: Book
) {
    Image(
        painter = painterResource(id = getCoverResource(book.cover)),
        contentDescription = book.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(42.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(4.dp))
    )
}