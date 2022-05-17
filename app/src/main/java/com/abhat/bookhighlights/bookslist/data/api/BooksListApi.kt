package com.abhat.bookhighlights.bookslist.data.api

import com.abhat.bookhighlights.bookslist.repository.model.BooksListResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface BooksListApi {
    @GET
    fun getBooksListAsync(title: String): Deferred<BooksListResponse>
}