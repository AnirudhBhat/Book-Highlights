package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.bookslist.data.api.BooksListApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BooksListRepositoryImpl(
    private val booksListApi: BooksListApi
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
}