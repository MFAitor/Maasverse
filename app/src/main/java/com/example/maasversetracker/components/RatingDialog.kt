package com.example.maasversetracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maasversetracker.model.Book

@Composable
fun RatingDialog(
    book: Book,
    currentRating: Int = 0,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit,
    onClear: (() -> Unit)? = null
) {
    var selectedRating by remember { mutableIntStateOf(currentRating) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.series,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.height(12.dp))

                // Estrellas
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= selectedRating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "$i estrellas",
                            tint = if (i <= selectedRating) Color(0xFFC9A227) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                            modifier = Modifier
                                .size(42.dp)
                                .clickable { selectedRating = i }
                                .padding(4.dp)
                        )
                    }
                }

                if (selectedRating > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$selectedRating / 5",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFC9A227)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedRating > 0) onSave(selectedRating)
                },
                enabled = selectedRating > 0
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Row {
                if (currentRating > 0 && onClear != null) {
                    TextButton(onClick = onClear) {
                        Text("Quitar nota")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}