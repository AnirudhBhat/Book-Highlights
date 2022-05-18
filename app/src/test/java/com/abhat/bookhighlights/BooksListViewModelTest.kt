package com.abhat.bookhighlights

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.abhat.bookhighlights.bookslist.BooksListUIState
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.CheckStoragePermission
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.GetBookDetails
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.ParseBooksFromStorage
import com.abhat.bookhighlights.bookslist.parser.BooksParser
import com.abhat.bookhighlights.bookslist.parser.Parser
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import com.abhat.bookhighlights.bookslist.model.HtmlDocument
import com.abhat.bookhighlights.bookslist.repository.BooksListRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BooksListViewModelTest {
    private lateinit var viewModel: BooksListViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var booksParser: Parser
    private lateinit var booksListRepository: BooksListRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle()
        booksParser = mock()
        booksListRepository = mock()
        viewModel = BooksListViewModel(savedStateHandle, booksListRepository, BooksParser(booksParser))

    }

    private fun init() {
        (viewModel.event.value as CheckStoragePermission).onStoragePermissionsResult.invoke(
            true, false
        )

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

    @Test
    fun `given storage permission granted, when books found on storage, then success state is shown`() {
        runTest {
            whenever(booksParser.parseHtml(any())).thenReturn(
                mutableListOf(
                    HtmlDocument(
                        heading = listOf(
                            "Highlight (yellow) - heading 1 book 1",
                            "Highlight (yellow) - heading 2 book 1",
                            "Highlight (yellow) - heading 3 book 1"
                        ),
                        highlight = listOf(
                            "highlight 1 book 1",
                            "highlight 2 book 1",
                            "highlight 3 book 1"
                        ),
                        bookName = "Book name 1"
                    ),
                )
            )
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.viewState.value).isInstanceOf(
                BooksListUIState.Success::class.java
            )
        }
    }

    @Test
    fun `given storage permission granted, when books found on storage, then success state with correct data is shown`() {
        runTest {

            whenever(booksParser.parseHtml(any())).thenReturn(
                mutableListOf(
                    HtmlDocument(
                        heading = listOf(
                            "Highlight (yellow) - heading 1 book 1",
                            "Highlight (yellow) - heading 2 book 1",
                            "Highlight (yellow) - heading 3 book 1"
                        ),
                        highlight = listOf(
                            "highlight 1 book 1",
                            "highlight 2 book 1",
                            "highlight 3 book 1"
                        ),
                        bookName = "Book name 1"
                    ),
                )
            )
            val expectedSuccessState = BooksListUIState.Success(
                booksList = listOf(
                    Book(
                        title = "Book name 1",
                        thumbnail = "",
                        highlights = mutableListOf(
                            Highlight(
                                line = "",
                                heading = "heading 1 book 1",
                                highlightText = "highlight 1 book 1"
                            ),
                            Highlight(
                                line = "",
                                heading = "heading 2 book 1",
                                highlightText = "highlight 2 book 1"
                            ),
                            Highlight(
                                line = "",
                                heading = "heading 3 book 1",
                                highlightText = "highlight 3 book 1"
                            )
                        )
                    )
                )
            )
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.viewState.value).isEqualTo(expectedSuccessState)
        }
    }

    @Test
    fun `given storage permission granted, when books found on storage, then GetBookDetails event is triggered`() {
        runTest {

            whenever(booksParser.parseHtml(any())).thenReturn(
                mutableListOf(
                    HtmlDocument(
                        heading = listOf(
                            "Highlight (yellow) - heading 1 book 1"
                        ),
                        highlight = listOf(
                            "highlight 1 book 1"
                        ),
                        bookName = "Book name 1"
                    ),
                )
            )
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.event.value).isInstanceOf(
                GetBookDetails::class.java
            )
        }
    }

    @Test
    fun `given storage permission granted, when books NOT found on storage, then error state is shown`() {
        runTest {
            whenever(booksParser.parseHtml(any())).thenReturn(mutableListOf())
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.viewState.value).isInstanceOf(
                BooksListUIState.Error::class.java
            )
        }
    }

    @Test
    fun `given storage permission granted, when books NOT found on storage, then GetBookDetails event is not triggered`() {
        runTest {
            whenever(booksParser.parseHtml(any())).thenReturn(mutableListOf())
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.event.value).isNotInstanceOf(
                GetBookDetails::class.java
            )
        }
    }

    @Test
    fun `given storage permission granted, when books NOT found on storage, then error state is shown with correct message`() {
        runTest {
            whenever(booksParser.parseHtml(any())).thenReturn(mutableListOf())
            init()
            val expectedErrorMessage = "No books found in the specified location!"
            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            val actualErrorMessage = (viewModel.viewState.value as BooksListUIState.Error).error.message

            assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage)
        }
    }
}