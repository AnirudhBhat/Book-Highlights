package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.bookslist.repository.model.BooksListResponse
import kotlinx.coroutines.flow.Flow

interface BooksListRepository {
    suspend fun getBooksList(title: String): Flow<BooksListResponse>
}