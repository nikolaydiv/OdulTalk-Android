package com.odultalk.apk.audio

import android.content.Context
import android.media.MediaPlayer

class AudioPlayer(
    private val context: Context,
    private val onComplete: () -> Unit
    ) {

        private var player: MediaPlayer? = null
        private var currentFile: String? = null

        fun play(fileName: String) {

            if (currentFile == fileName && player?.isPlaying == true) {
                stop()
                return
            }

            stop()

            val resourceName = "audio_$fileName"

            val resId = context.resources.getIdentifier(
                resourceName,
                "raw",
                context.packageName
            )

            if (resId == 0) return

            player = MediaPlayer.create(context, resId)
            currentFile = fileName
            player?.start()

            player?.setOnCompletionListener {
                stop()
                onComplete()
            }
        }

        fun stop() {
            player?.release()
            player = null
            currentFile = null
        }
    }