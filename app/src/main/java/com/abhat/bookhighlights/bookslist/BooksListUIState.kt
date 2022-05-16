package com.abhat.bookhighlights.bookslist

import com.abhat.bookhighlights.bookslist.model.Book

sealed class BooksListUIState(
    val isLoading: Boolean = false,
    open val booksList: List<Book> = emptyList(),
    open val error: Throwable? = null
) {
    object Loading : BooksListUIState(isLoading = true)

    data class Success(override val booksList: List<Book>) : BooksListUIState()

    data class Error(
        override val error: Throwable
    ) : BooksListUIState()
}