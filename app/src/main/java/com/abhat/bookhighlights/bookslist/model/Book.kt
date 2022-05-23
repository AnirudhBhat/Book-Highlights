package com.abhat.bookhighlights.bookslist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.abhat.bookhighlights.database.DataHighlightConvertor
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "book")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo val title: String,
    @ColumnInfo val thumbnail: String,
    @ColumnInfo val highlights: MutableList<Highlight> = mutableListOf(),
) : Parcelable

@Parcelize
data class Highlight(
    val line: String,
    val heading: String = "",
    val highlightText: String
) : Parcelable