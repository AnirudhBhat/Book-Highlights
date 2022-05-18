package com.abhat.bookhighlights.bookslist.data.api

import com.abhat.bookhighlights.bookslist.repository.model.BooksListResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksListApi {
    @GET("volumes")
    fun getBooksListAsync(@Query("q") q: String): Deferred<BooksListResponse>
}