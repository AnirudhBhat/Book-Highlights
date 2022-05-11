package com.abhat.bookhighlights

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.abhat.bookhighlights.bookslist.BooksListViewModel
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
        viewModel = BooksListViewModel(savedStateHandle)
    }

    @Test
    fun `when view model init, CheckStoragePermission event is triggered`() {
        assertThat(viewModel.event.value).isInstanceOf(
            BooksListViewModel.Event.CheckStoragePermission::class.java
        )
    }
}