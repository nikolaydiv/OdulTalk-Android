package com.odultalk.apk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.odultalk.apk.data.local.FavoritesStore
import com.odultalk.apk.data.repository.PhraseRepository
import com.odultalk.apk.ui.AppScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = PhraseRepository(this)
        val data = repo.loadPhrases()

        val store = FavoritesStore(this)

        setContent {
            AppScreen(
                phrases = data,
                favoritesStore = store
            )
        }
    }
}