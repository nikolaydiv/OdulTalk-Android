package com.odultalk.apk.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("favorites")

class FavoritesStore(private val context: Context) {

    private val KEY = stringSetPreferencesKey("favorites_ids")

    fun load(): MutableSet<Int> {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            val set = prefs[KEY] ?: emptySet()
            set.map { it.toInt() }.toMutableSet()
        }
    }

    fun save(favorites: Set<Int>) {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[KEY] = favorites.map { it.toString() }.toSet()
            }
        }
    }
}