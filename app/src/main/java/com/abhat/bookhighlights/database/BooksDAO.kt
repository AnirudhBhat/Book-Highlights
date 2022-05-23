package com.abhat.bookhighlights.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BooksDAO {
    @Query("SELECT * from books")
    fun getBooksList(): Books

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooksList(books: Books)

    @Query("DELETE from books")
    fun deleteBooks()
}