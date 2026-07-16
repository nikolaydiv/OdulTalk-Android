package com.odultalk.apk.ui

sealed class Screen {

    data object Categories : Screen()

    data class Phrases(val category: String) : Screen()

    data object Favorites : Screen()

}