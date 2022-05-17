package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.bookslist.repository.model.BooksListResponse

sealed class BooksListRepoState {
    data class Success(val news: BooksListResponse): BooksListRepoState()
    data class Error(
        val error: Throwable?
    ): BooksListRepoState()
}