package com.odultalk.apk.audio

import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore

class AudioPlayer(private val context: Context) {

    private var player: MediaPlayer? = null
    private var currentFile: String? = null

    fun play(fileName: String) {

        if (currentFile == fileName && player?.isPlaying == true) {
            stop()
            return
        }

        stop()

        val resId = context.resources.getIdentifier(
            fileName,
            "raw",
            context.packageName
        )

        if (resId == 0) return

        player = MediaPlayer.create(context, resId)
        player?.start()

        player?.setOnCompletionListener {
            stop()
        }
    }

    fun stop() {
        player?.release()
        player = null
    }

    fun isPlaying(fileName: String): Boolean {
        return currentFile == fileName && player?.isPlaying == true
    }
}