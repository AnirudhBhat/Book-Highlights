package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.bookslist.data.api.BooksListApi
import com.abhat.bookhighlights.database.Books
import com.abhat.bookhighlights.database.BooksHighlightsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BooksListRepositoryImpl(
    private val booksListApi: BooksListApi,
    private val booksHighlightsDatabase: BooksHighlightsDatabase
): BooksListRepository {
    override suspend fun getBookDetails(title: String): Flow<BooksListRepoState> {
        return flow {
            val result = booksListApi.getBooksListAsync(title).await()
            if (!result.items.isNullOrEmpty()) {
                emit(
                    BooksListRepoState.Success(
                        booksList = result.items
                    )
                )
            } else {
                emit(
                    BooksListRepoState.Error(
                        error = Throwable("Didn't find any books with that title!")
                    )
                )
            }
        }
    }

    override fun insertBooks(books: Books) {
        with(booksHighlightsDatabase.booksDAO()) {
            insertBooksList(books)
        }
    }

    override fun deleteBooks() {
        with(booksHighlightsDatabase.booksDAO()) {
            deleteBooks()
        }
    }

    override fun getBooksList(): Books {
        return with(booksHighlightsDatabase.booksDAO()) {
            getBooksList()
        }
    }
}