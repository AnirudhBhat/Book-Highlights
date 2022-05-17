package com.abhat.bookhighlights.bookslist.repository

import com.abhat.bookhighlights.bookslist.repository.model.BooksListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BooksListRepositoryImpl: BooksListRepository {
    override suspend fun getBooksList(title: String): Flow<BooksListResponse> {
        return flow {

        }
    }
}