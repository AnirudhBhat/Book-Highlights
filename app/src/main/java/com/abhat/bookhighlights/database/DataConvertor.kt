package com.abhat.bookhighlights.database

import androidx.room.TypeConverter
import com.abhat.bookhighlights.bookslist.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type


/**
 * Created by Anirudh Uppunda on 28,September,2020
 */
class DataConvertor: Serializable {
    @TypeConverter
    fun fromItemList(itemList: List<Book>?): String? {
        if (itemList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Book>?>() {}.type
        return gson.toJson(itemList, type)
    }

    @TypeConverter
    fun toBookList(itemListString: String?): List<Book>? {
        if (itemListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Book>?>() {}.type
        return gson.fromJson(itemListString, type)
    }
}