package com.abhat.bookhighlights.bookslist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val title: String,
    val thumbnail: String,
    val highlights: MutableList<Highlight> = mutableListOf(),
) : Parcelable

@Parcelize
data class Highlight(
    val line: String,
    val heading: String = "",
    val highlightText: String
) : Parcelable