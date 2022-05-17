package com.abhat.bookhighlights.bookslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abhat.bookhighlights.SingleLiveEvent
import com.abhat.bookhighlights.bookslist.parser.BooksParser
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
        event.value = Event.ParseBooksFromStorage(::parseBooks)
    }

    private fun parseBooks(books: List<File>) {
        val booksList = booksParser.parseBooks(books)
        if (!booksList.isNullOrEmpty()) {
            booksUiState.value = BooksListUIState.Success(
                booksList = booksList
            )
        } else {
            booksUiState.value = BooksListUIState.Error(
                error = Throwable("No books found in the specified location!")
            )
        }
    }


    sealed class Event {
        data class CheckStoragePermission(
            val onStoragePermissionsResult: (
                permissionGranted: Boolean,
                showRationale: Boolean
            ) -> Unit
        ) : Event()
        data class ParseBooksFromStorage(
            val parseBooks: (List<File>) -> Unit
        ) : Event()
    }
}