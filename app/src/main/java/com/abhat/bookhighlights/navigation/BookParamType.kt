package com.abhat.bookhighlights.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.abhat.bookhighlights.bookslist.model.Book
import com.google.gson.Gson

class BookParamType : NavType<Book>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Book? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Book {
        return Gson().fromJson(value, Book::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Book) {
        bundle.putParcelable(key, value)
    }
}