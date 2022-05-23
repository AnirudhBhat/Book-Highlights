package com.abhat.bookhighlights.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhat.bookhighlights.bookslist.model.Book

@Entity(tableName = "books")
data class Books(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "booksList") val booksList: List<Book>
)