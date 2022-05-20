package com.abhat.bookhighlights.navigation

sealed class Screen(val route: String) {
    object BooksListScreen : Screen("bookslist_screen")
    object HighlightsListScreen : Screen("highlights_list")
}