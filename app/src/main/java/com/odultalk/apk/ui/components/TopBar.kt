package com.odultalk.apk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    search: String,
    onSearchChange: (String) -> Unit,
    onFavoritesClick: () -> Unit,
    onAllClick: () -> Unit,
    onSettingsClick: () -> Unit = {},

    isFavoritesActive: Boolean,
    isCategoriesActive: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        // ================= HEADER =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "OdulTalk",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onSettingsClick) {
                Text("⚙️")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= SEARCH =================
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("🔍 Поиск...") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // ================= CONTROLS =================
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // ⭐ FAVORITES
            Button(
                onClick = onFavoritesClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavoritesActive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isFavoritesActive)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("❤️")
            }

            // 📂 ALL / CATEGORIES
            Button(
                onClick = onAllClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCategoriesActive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isCategoriesActive)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Все категории")
            }
        }
    }
}