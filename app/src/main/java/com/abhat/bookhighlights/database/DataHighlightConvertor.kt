package com.abhat.bookhighlights.database

import androidx.room.TypeConverter
import com.abhat.bookhighlights.bookslist.model.Highlight
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type


/**
 * Created by Anirudh Uppunda on 28,September,2020
 */
class DataHighlightConvertor: Serializable {
    @TypeConverter
    fun fromItemList(itemList: List<Highlight>?): String? {
        if (itemList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Highlight>?>() {}.type
        return gson.toJson(itemList, type)
    }

    @TypeConverter
    fun toHighlightList(itemListString: String?): List<Highlight>? {
        if (itemListString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Highlight>?>() {}.type
        return gson.fromJson(itemListString, type)
    }
}