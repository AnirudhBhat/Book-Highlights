package com.abhat.bookhighlights.bookslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abhat.bookhighlights.SingleLiveEvent
import java.io.File

class BooksListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val booksParser: BooksParser,
) : ViewModel() {

    private val booksUiState: MutableLiveData<BooksListUIState> = MutableLiveData(BooksListUIState.Loading)
    val viewState: LiveData<BooksListUIState> = booksUiState

    val event: SingleLiveEvent<Event> = SingleLiveEvent()

    init {
        event.value = Event.CheckStoragePermission(::onStoragePermissionsResult)
    }

    private fun onStoragePermissionsResult(
        permissionGranted: Boolean,
        showRationale: Boolean
    ) {
        event.value = Event.ParseBooksFromStorage
    }

    fun parseBooks(books: List<File>) {
        booksUiState.value = BooksListUIState.Success(
            booksList = booksParser.parseBooks(books)
        )
    }


    sealed class Event {
        data class CheckStoragePermission(
            val onStoragePermissionsResult: (
                permissionGranted: Boolean,
                showRationale: Boolean
            ) -> Unit
        ) : Event()
        object ParseBooksFromStorage : Event()
    }
}