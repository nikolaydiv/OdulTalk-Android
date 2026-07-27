package com.odultalk.apk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.odultalk.apk.data.models.Phrase
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PhraseScreen(
    phrases: List<Phrase>,
    favorites: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onBack: () -> Unit,
    query: String,
    onPlayAudio: (String) -> Unit,
    isPlayingAudio: String?,
    emptyMessage: String = "Ничего не найдено"
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        //  BACK
        Button(
            onClick = onBack,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Text("← Назад")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (phrases.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emptyMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {

                // =========================
                // PHRASES LIST
                // =========================
                items(phrases) { phrase ->

                    val hasAudio = phrase.audio != null
                    val isFav = favorites.contains(phrase.id)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = buildAnnotatedString {
                                        val startIndex = phrase.ru.lowercase().indexOf(query.lowercase())

                                        if (startIndex >= 0 && query.isNotBlank()) {
                                            append(phrase.ru.substring(0, startIndex))

                                            withStyle(
                                                style = androidx.compose.ui.text.SpanStyle(
                                                    background = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                                )
                                            ) {
                                                append(phrase.ru.substring(startIndex, startIndex + query.length))
                                            }

                                            append(phrase.ru.substring(startIndex + query.length))
                                        } else {
                                            append(phrase.ru)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                //  AUDIO
                                IconButton(
                                    onClick = {
                                        phrase.audio?.let { onPlayAudio(it) }
                                    },
                                    enabled = hasAudio,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Text(
                                        text = if (phrase.audio == isPlayingAudio) "⏹️" else "🔊",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (hasAudio)
                                            MaterialTheme.colorScheme.onSurface
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.width(4.dp)
                                )

                                //  FAVORITE
                                IconButton(
                                    onClick = { onToggleFavorite(phrase.id) },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Text(
                                        text = if (isFav) "❤️" else "🤍"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = buildAnnotatedString {
                                    val startIndex = phrase.ykg.lowercase().indexOf(query.lowercase())

                                    if (startIndex >= 0 && query.isNotBlank()) {
                                        append(phrase.ykg.substring(0, startIndex))

                                        withStyle(
                                            style = androidx.compose.ui.text.SpanStyle(
                                                background = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                            )
                                        ) {
                                            append(phrase.ykg.substring(startIndex, startIndex + query.length))
                                        }

                                        append(phrase.ykg.substring(startIndex + query.length))
                                    } else {
                                        append(phrase.ykg)
                                    }
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}