package com.abhat.bookhighlights

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.CheckStoragePermission
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.ParseBooksFromStorage
import com.abhat.bookhighlights.bookslist.BooksParser
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BooksListViewModelTest {
    private lateinit var viewModel: BooksListViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle()
        viewModel = BooksListViewModel(savedStateHandle, BooksParser())
    }

    @Test
    fun `when view model init, CheckStoragePermission event is triggered`() {
        assertThat(viewModel.event.value).isInstanceOf(
            CheckStoragePermission::class.java
        )
    }

    @Test
    fun `given viewmodel init, when storage permission granted, ParseBookFromStorage event is triggered`() {
        (viewModel.event.value as CheckStoragePermission).onStoragePermissionsResult.invoke(
            true, false
        )
        assertThat(viewModel.event.value).isInstanceOf(
            ParseBooksFromStorage::class.java
        )
    }
}