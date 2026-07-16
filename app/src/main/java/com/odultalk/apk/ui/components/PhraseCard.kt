package com.odultalk.apk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.odultalk.apk.data.models.Phrase

@Composable
fun PhraseCard(
    phrase: Phrase,
    isFavorite: Boolean,
    onToggleFavorite: (Int) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = phrase.ru,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { onToggleFavorite(phrase.id) }
                ) {

                    Text(
                        if (isFavorite) "❤️" else "🤍"
                    )

                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = phrase.ykg,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}