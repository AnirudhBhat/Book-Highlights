package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.bookslist.repository.model.Items

sealed class BooksListRepoState {
    data class Success(val booksList: List<Items>): BooksListRepoState()
    data class Error(
        val error: Throwable?
    ): BooksListRepoState()
}