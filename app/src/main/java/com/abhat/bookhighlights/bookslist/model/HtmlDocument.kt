package com.abhat.bookhighlights.bookslist.model

data class HtmlDocument(
    val heading: List<String>,
    val highlight: List<String>,
    val bookName: String
)