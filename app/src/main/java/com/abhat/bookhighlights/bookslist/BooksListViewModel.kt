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
import com.abhat.bookhighlights.database.Books
import com.abhat.bookhighlights.di.CoroutineContextProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class BooksListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val booksListRepository: BooksListRepository,
    private val booksParser: BooksParser,
) : ViewModel() {

    private val booksUiState: MutableLiveData<BooksListUIState> =
        MutableLiveData(BooksListUIState.Loading)
    val viewState: LiveData<BooksListUIState> = booksUiState

    val event: SingleLiveEvent<Event> = SingleLiveEvent()

    init {
        event.value = Event.GetBooksListFromDatabase(::updateBooksFromDatabase)
    }

    private fun parseBooks(books: List<File>) {
        val booksList = booksParser.parseBooks(books)
        if (!booksList.isNullOrEmpty()) {
            when (val res = viewState.value) {
                is BooksListUIState.Success -> {
                    val newBooksList =
                        res.booksList.union(booksList)
                    val updatedBooksList = newBooksList.distinctBy { it.title }
                    if (!updatedBooksList.isNullOrEmpty()) {
                        event.value = Event.AddBooksToDatabase(updatedBooksList, ::addBooksToDatabase)
                    }
                }
                BooksListUIState.Loading -> {
                    event.value = Event.AddBooksToDatabase(booksList, ::addBooksToDatabase)
                }
            }
        } else if (viewState.value !is BooksListUIState.Success) {
            booksUiState.value = BooksListUIState.Error(
                error = Throwable("No books found in the specified location!")
            )
        }
    }

    private fun updateBooksFromDatabase() {
        viewModelScope.launch(coroutineContextProvider.IO) {
            val result = booksListRepository.getBooksList()
            withContext(coroutineContextProvider.Main) {
                if (!result.booksList.isNullOrEmpty()) {
                    booksUiState.value = BooksListUIState.Success(
                        booksList = result.booksList
                    )
                    event.value = Event.GetBookDetails(result.booksList, ::getBooksDetails)
                }
                event.value = Event.ParseBooksFromStorage(::parseBooks)
            }
        }
    }

    private fun addBooksToDatabase(booksList: List<Book>) {
        viewModelScope.launch(coroutineContextProvider.IO) {
            booksListRepository.deleteBooks()
            booksListRepository.insertBooks(
                Books(
                    booksList = booksList
                )
            )
            withContext(coroutineContextProvider.Main) {
                booksUiState.value = BooksListUIState.Success(
                    booksList = booksList
                )
                event.value = Event.GetBookDetails(booksList, ::getBooksDetails)
            }
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
            data class ParseBooksFromStorage(
                val parseBooks: suspend (List<File>) -> Unit
            ) : Event()

            data class GetBookDetails(
                val booksList: List<Book>,
                val getBookDetails: (booksList: List<Book>) -> Unit
            ) : Event()

            data class AddBooksToDatabase(
                val booksList: List<Book>,
                val addBooksToDatabase: (booksList: List<Book>) -> Unit
            ) : Event()

            data class GetBooksListFromDatabase(
                val getBooksFromDatabase: () -> Unit
            ) : Event()
        }
    }