package com.abhat.bookhighlights.bookslist.model

data class Book(
    val title: String,
    val highlights: MutableList<Highlight> = mutableListOf(),
)

data class Highlight(
    val line: String,
    val heading: String = "",
    val highlightText: String
)