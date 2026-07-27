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
import androidx.compose.animation.AnimatedVisibility
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf

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
    val isPlayingAudio = remember { mutableStateOf<String?>(null)}
    val audioPlayer = remember {
        AudioPlayer(context) {
            isPlayingAudio.value = null
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_STOP) {
                audioPlayer.stop()
                isPlayingAudio.value = null
            }

        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        favorites.addAll(favoritesStore.load())
        delay(1000)
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
                                        if (isPlayingAudio.value == file) {
                                            audioPlayer.stop()
                                            isPlayingAudio.value = null
                                        } else {
                                            audioPlayer.play(file)
                                            isPlayingAudio.value = file
                                        }
                                    },
                                    isPlayingAudio = isPlayingAudio.value
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
                                onPlayAudio = { file ->
                                    if (isPlayingAudio.value == file) {
                                        audioPlayer.stop()
                                        isPlayingAudio.value = null
                                    } else {
                                        audioPlayer.play(file)
                                        isPlayingAudio.value = file
                                    }
                                },
                                isPlayingAudio = isPlayingAudio.value
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
                                onPlayAudio = { file ->
                                    if (isPlayingAudio.value == file) {
                                        audioPlayer.stop()
                                        isPlayingAudio.value = null
                                    } else {
                                        audioPlayer.play(file)
                                        isPlayingAudio.value = file
                                    }
                                },
                                isPlayingAudio = isPlayingAudio.value,
                                emptyMessage = "🤍\n\nИзбранное пусто\n\nДобавляйте фразы,\nнажимая ❤️"
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
            AnimatedVisibility(
                visible = showSplash,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.splash),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
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

                    val listState = rememberLazyListState()

                    val showScrollHint by remember {
                        derivedStateOf {
                            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()

                            lastVisibleItem != null && lastVisibleItem.index < listState.layoutInfo.totalItemsCount - 1
                        }
                    }

                    Column {

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.heightIn(max = 420.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            item {
                                Text(
                                    "Юкагирский (лесной) язык — один из древнейших языков Севера, сохранивший мудрость и культуру лесных юкагиров. Сегодня на нём говорят лишь единицы. Для молодёжи родной язык часто остаётся недостаточно доступным."
                                )
                            }

                            item {
                                Text(
                                    "И мы создали приложение OdulTalk, чтобы вернуть юкагирский язык в повседневную жизнь. Это живой разговорник для тех, кто хочет понимать своих близких и говорить на языке предков здесь и сейчас."
                                )
                            }

                            item {
                                Text("С помощью OdulTalk вы можете:")
                            }

                            item {
                                Text("• слушать живую речь — знакомьтесь с правильным произношением от носителя языка;")
                            }

                            item {
                                Text("• учить фразы для жизни — от приветствия до разговора в магазине или школе;")
                            }

                            item {
                                Text("• сохранять наследие вместе — помогите детям и внукам услышать голос своей родной культуры.")
                            }

                            item {
                                Text(
                                    "Наш проект объединяет село Нелемное со всем миром. Он доступен каждому жителю Якутии, исследователю, педагогу и любому человеку, которому небезразличны языки коренных народов России."
                                )
                            }

                            item {
                                Text(
                                    "Давайте сохраним звучание юкагирского языка вместе."
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        if (showScrollHint) {
                            Text(
                                text = "⬇ Прокрутите вниз",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    }
}