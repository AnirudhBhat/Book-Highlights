package com.abhat.bookhighlights.bookslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.bookhighlights.SingleLiveEvent
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.parser.BooksParser
import com.abhat.bookhighlights.bookslist.repository.BooksListRepoState
import com.abhat.bookhighlights.bookslist.repository.BooksListRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class BooksListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val booksListRepository: BooksListRepository,
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

    private suspend fun parseBooks(books: List<File>) {
        val booksList = booksParser.parseBooks(books)
        if (!booksList.isNullOrEmpty()) {
            booksUiState.value = BooksListUIState.Success(
                booksList = booksList
            )
            event.value = Event.GetBookDetails(booksList, ::getBooksDetails)
        } else {
            booksUiState.value = BooksListUIState.Error(
                error = Throwable("No books found in the specified location!")
            )
        }
    }

    private fun getBooksDetails(booksList: List<Book>) {
        booksList.forEach { book ->
            viewModelScope.launch {
                getBookDetails(book.title)
            }
        }
    }

    private suspend fun getBookDetails(title: String) {
        booksListRepository.getBookDetails(title).collect { repoState ->
            when (repoState) {
                is BooksListRepoState.Success -> {
                    val updatedBooksList = mutableListOf<Book>()
                    (viewState.value as BooksListUIState.Success).booksList.forEach {
                        if (it.title == title) {
                            updatedBooksList.add(
                                it.copy(
                                    thumbnail = repoState.booksList[0].volumeInfo.imageLinks.thumbnail
                                )
                            )
                        } else {
                            updatedBooksList.add(it)
                        }
                    }
                    booksUiState.value = BooksListUIState.Success(
                        booksList = updatedBooksList
                    )
                }
            }
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
            val parseBooks: suspend (List<File>) -> Unit
        ) : Event()
        data class GetBookDetails(
            val booksList: List<Book>,
            val getBookDetails: (booksList: List<Book>) -> Unit
        ) : Event()
    }
}