package com.odultalk.apk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import com.odultalk.apk.data.local.FavoritesStore
import com.odultalk.apk.data.models.Phrase
import com.odultalk.apk.ui.components.TopBar
import com.odultalk.apk.ui.screens.CategoriesScreen
import com.odultalk.apk.ui.screens.PhraseScreen
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import com.odultalk.apk.audio.AudioPlayer
import com.odultalk.apk.R

@Composable
fun AppScreen(
    phrases: List<Phrase>,
    favoritesStore: FavoritesStore
) {

    var screen by remember { mutableStateOf<Screen>(Screen.Categories) }
    var query by remember { mutableStateOf("") }

    val favorites = remember { mutableStateListOf<Int>() }

    val isFavoritesActive = screen == Screen.Favorites
    val isCategoriesActive = screen == Screen.Categories && query.isBlank()

    var showSettings by remember { mutableStateOf(false) }
    var showSplash by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }

    LaunchedEffect(Unit) {
        favorites.addAll(favoritesStore.load())
        delay(800)
        showSplash = false
    }

    fun toggleFavorite(id: Int) {
        if (favorites.contains(id)) favorites.remove(id)
        else favorites.add(id)

        favoritesStore.save(favorites.toSet())
    }

    fun resetToCategories() {
        screen = Screen.Categories
        query = ""
    }

    BackHandler {
        when {
            query.isNotBlank() -> resetToCategories()
            screen is Screen.Phrases -> resetToCategories()
            screen == Screen.Favorites -> resetToCategories()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // =========================
        //   MAIN APP UI
        // =========================
        Column(modifier = Modifier.fillMaxSize()) {

            TopBar(
                search = query,
                onSearchChange = {
                    query = it
                    if (it.isNotBlank()) {
                        screen = Screen.Categories
                    }
                },

                isFavoritesActive = isFavoritesActive,
                isCategoriesActive = isCategoriesActive,

                onFavoritesClick = {
                    screen = Screen.Favorites
                    query = ""
                },

                onAllClick = {
                    resetToCategories()
                },

                onSettingsClick = {
                    showSettings = true
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AnimatedContent(
                    targetState = screen,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "screen"
                ) { currentScreen ->

                    when (val s = currentScreen) {

                        Screen.Categories -> {

                            if (query.isNotBlank()) {

                                PhraseScreen(
                                    phrases = phrases.filter {
                                        it.ru.contains(query, true) ||
                                                it.ykg.contains(query, true)
                                    },
                                    favorites = favorites.toSet(),
                                    onToggleFavorite = { toggleFavorite(it) },
                                    onBack = { resetToCategories() },
                                    query = query,
                                    onPlayAudio = { file ->
                                        audioPlayer.play(file)
                                    }
                                )

                            } else {

                                CategoriesScreen(
                                    phrases = phrases,
                                    onCategoryClick = { category ->
                                        screen = Screen.Phrases(category)
                                    }
                                )
                            }
                        }

                        is Screen.Phrases -> {

                            PhraseScreen(
                                phrases = phrases.filter {
                                    it.category == s.category &&
                                            (
                                                    query.isBlank() ||
                                                            it.ru.contains(query, true) ||
                                                            it.ykg.contains(query, true)
                                                    )
                                },
                                favorites = favorites.toSet(),
                                onToggleFavorite = { toggleFavorite(it) },
                                onBack = { resetToCategories() },
                                query = query,
                                onPlayAudio = { file -> audioPlayer.play(file)}
                            )
                        }

                        Screen.Favorites -> {

                            PhraseScreen(
                                phrases = phrases.filter {
                                    favorites.contains(it.id)
                                },
                                favorites = favorites.toSet(),
                                onToggleFavorite = { toggleFavorite(it) },
                                onBack = { resetToCategories() },
                                query = query,
                                onPlayAudio = { file -> audioPlayer.play(file)}
                            )
                        }
                    }
                }
            }
        }

        // =========================
        //   SPLASH OVERLAY
        // =========================
        if (showSplash) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.splash),
                    contentDescription = "Splash",
                    modifier = Modifier.size(180.dp)
                )
            }
        }

        // =========================
        //  SETTINGS DIALOG (WEB MODAL)
        // =========================
        if (showSettings) {

            AlertDialog(
                onDismissRequest = { showSettings = false },
                confirmButton = {
                    TextButton(onClick = { showSettings = false }) {
                        Text("Закрыть")
                    }
                },
                title = {
                    Text("О приложении")
                },
                text = {
                    Column {

                        Text("OdulTalk - русско-юкагирский разговорник")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Версия: 1.0")

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider()

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Контакты")

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Email: nikolaydiv@gmail.com")
                        Text("Telegram: @nickdiv")
                    }
                }
            )
        }
    }
}