package com.odultalk.apk.data.repository

import android.content.Context
import com.odultalk.apk.data.models.Phrase
import org.json.JSONArray

class PhraseRepository(private val context: Context) {

    fun loadPhrases(): List<Phrase> {

        val json = context.assets
            .open("phrases.json")
            .bufferedReader()
            .use { it.readText() }

        val array = JSONArray(json)

        val result = mutableListOf<Phrase>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            result.add(
                Phrase(
                    id = obj.getInt("id"),
                    ru = obj.getString("ru"),
                    ykg = obj.getString("ykg"),
                    category = obj.getString("category"),
                    audio = if (obj.isNull("audio")) null else obj.getString("audio")
                )
            )
        }

        return result
    }
}