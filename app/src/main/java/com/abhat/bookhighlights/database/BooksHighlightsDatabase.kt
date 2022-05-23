package com.abhat.bookhighlights.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhat.bookhighlights.bookslist.model.Book

@Database(
    entities = [Books::class, Book::class],
    version = 1,
)
@TypeConverters(DataConvertor::class, DataHighlightConvertor::class)
abstract class BooksHighlightsDatabase : RoomDatabase() {
    abstract fun booksDAO(): BooksDAO

    companion object {
        fun getDatabaseInstance(context: Context): BooksHighlightsDatabase {
            return Room.databaseBuilder(
                context,
                BooksHighlightsDatabase::class.java, "bookshighlights-database"
            ).build()
        }
    }
}