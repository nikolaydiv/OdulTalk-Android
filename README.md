# OdulTalk Android

Русско-юкагирский разговорник для Android.

Приложение помогает изучать юкагирские фразы с переводом на русский язык.

## Features

- 📚 Категории фраз
- 🔎 Поиск по русскому и юкагирскому тексту
- ❤️ Избранное
- 🔊 Аудио произношение

## Screens

- Categories screen
- Phrase list screen
- Favorites screen
- Search mode
- About application dialog

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Android SDK
- Gradle
- JSON local data storage

## Project Structure
app/
└── src/main/java/com/odultalk/apk/
                                    ├── audio/
                                    │    └── AudioPlayer.kt
                                    ├── data/
                                    │    ├── local/
                                    │    │    └── FavoritesStore.kt
                                    │    ├── models/
                                    │    │    └── Phrase.kt
                                    │    └── repository/
                                    │         └── PhraseRepository.kt
                                    ├── ui/
                                    │    ├── components/
                                    │    │    ├── PhraseCard.kt
                                    │    │    └── TopBar.kt
                                    │    ├── data/
                                    │    │    └── CategoryIcons.kt
                                    │    ├── screens/
                                    │    │    ├── CategoriesScreen.kt
                                    │    │    └── PhrasesScreen.kt
                                    │    ├── AppScreen.kt
                                    │    └── Screen.kt