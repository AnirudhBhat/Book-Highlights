package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.database.Books
import kotlinx.coroutines.flow.Flow

interface BooksListRepository {
    suspend fun getBookDetails(title: String): Flow<BooksListRepoState>
    fun insertBooks(books: Books)
    fun deleteBooks()
    fun getBooksList(): Books
}