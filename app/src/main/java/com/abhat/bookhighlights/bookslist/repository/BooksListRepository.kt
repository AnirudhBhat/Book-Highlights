package com.abhat.bookhighlights.bookslist.repository

import kotlinx.coroutines.flow.Flow

interface BooksListRepository {
    suspend fun getBookDetails(title: String): Flow<BooksListRepoState>
}