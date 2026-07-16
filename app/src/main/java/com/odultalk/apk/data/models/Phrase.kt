package com.odultalk.apk.data.models

data class Phrase(
    val id: Int,
    val ru: String,
    val ykg: String,
    val category: String,
    val audio: String? = null
)