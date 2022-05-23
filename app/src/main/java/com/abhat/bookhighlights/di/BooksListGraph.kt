package com.abhat.bookhighlights.di

import androidx.lifecycle.ViewModelStoreOwner
import com.abhat.bookhighlights.MainActivity
import com.abhat.bookhighlights.bookslist.data.api.BooksListApi
import com.abhat.bookhighlights.bookslist.repository.BooksListRepository
import com.abhat.bookhighlights.bookslist.repository.BooksListRepositoryImpl
import com.abhat.bookhighlights.database.BooksHighlightsDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Anirudh Uppunda on 27,October,2020
 */
interface BooksListGraph {
    val booksListRepository: BooksListRepository
}

class NetworkGraphImpl(viewModelStoreOwner: ViewModelStoreOwner) : BooksListGraph {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NetworkConstants.BASE_URL)
        .client(okHttpClient)
        .build()



    override val booksListRepository: BooksListRepository = BooksListRepositoryImpl(
        retrofit.create(BooksListApi::class.java),
        BooksHighlightsDatabase.getDatabaseInstance(viewModelStoreOwner as MainActivity)
    )
}