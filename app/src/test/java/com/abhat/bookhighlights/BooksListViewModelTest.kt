package com.abhat.bookhighlights

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.abhat.bookhighlights.bookslist.BooksListUIState
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.AddBooksToDatabase
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.GetBookDetails
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.GetBooksListFromDatabase
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.ParseBooksFromStorage
import com.abhat.bookhighlights.bookslist.parser.BooksParser
import com.abhat.bookhighlights.bookslist.parser.Parser
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import com.abhat.bookhighlights.bookslist.model.HtmlDocument
import com.abhat.bookhighlights.bookslist.repository.BooksListRepoState
import com.abhat.bookhighlights.bookslist.repository.BooksListRepository
import com.abhat.bookhighlights.bookslist.repository.model.ImageLinks
import com.abhat.bookhighlights.bookslist.repository.model.Items
import com.abhat.bookhighlights.bookslist.repository.model.VolumeInfo
import com.abhat.bookhighlights.database.Books
import com.abhat.bookhighlights.di.CoroutineContextProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
        viewModel = BooksListViewModel(
            savedStateHandle,
            TestCoroutineContextProvider(),
            booksListRepository,
            BooksParser(booksParser)
        )

    }

    private class TestCoroutineContextProvider: CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    private fun init() {
        (viewModel.event.value as GetBooksListFromDatabase).getBooksFromDatabase.invoke()

    }

    @Test
    fun `when view model init, GetBooksListFromDatabase event is triggered`() {
        assertThat(viewModel.event.value).isInstanceOf(
            GetBooksListFromDatabase::class.java
        )
    }

    @Test
    fun `given viewmodel init, then ParseBookFromStorage event is triggered`() {
        whenever(booksListRepository.getBooksList()).thenReturn(Books(
            booksList = emptyList()
        ))
        init()

        assertThat(viewModel.event.value).isInstanceOf(
            ParseBooksFromStorage::class.java
        )
    }

    @Test
    fun `given books found on storage and not in database, then AddBooksToDatabase event is triggered`() {
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
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.event.value).isInstanceOf(
                AddBooksToDatabase::class.java
            )
        }
    }

    @Test
    fun `given books found on storage and not in database, then books are deleted first and then inserted`() {
        runTest {
            val booksList = listOf(
                Book(
                    title = "Book name 1",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                )
            )
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
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            (viewModel.event.value as AddBooksToDatabase).addBooksToDatabase.invoke(booksList)

            inOrder(booksListRepository) {
               verify(booksListRepository).deleteBooks()
                verify(booksListRepository).insertBooks(Books(
                    booksList = booksList
                ))
            }
        }
    }

    @Test
    fun `given books not found on storage, when books found on database then UI is updated with books from database`() {
        runTest {
            val booksList = listOf(
                Book(
                    title = "Book name 1",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                )
            )
            whenever(booksParser.parseHtml(any())).thenReturn(mutableListOf())
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = booksList
            ))
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat(viewModel.viewState.value).isEqualTo(
                BooksListUIState.Success(
                    booksList = booksList
                )
            )
        }
    }

    @Test
    fun `given books found on both storage and database, then UI is updated with distinct books from both the sources`() {
        runTest {
            val expectedBooksList = listOf(
                Book(
                    title = "Book name 1",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                ),
                Book(
                    title = "Book name 2",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 2",
                            highlightText = "highlight 1 book 2"
                        ),
                    )
                )
            )
            val booksListFromDatabase = listOf(
                Book(
                    title = "Book name 1",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                )
            )
            val booksListFromStorage = mutableListOf(
                HtmlDocument(
                    heading = listOf(
                        "Highlight (yellow) - heading 1 book 2"
                    ),
                    highlight = listOf(
                        "highlight 1 book 2",
                    ),
                    bookName = "Book name 2"
                ),
            )
            whenever(booksParser.parseHtml(any())).thenReturn(booksListFromStorage)
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = booksListFromDatabase
            ))
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            (viewModel.event.value as AddBooksToDatabase).addBooksToDatabase.invoke(expectedBooksList)

            assertThat(viewModel.viewState.value).isEqualTo(
                BooksListUIState.Success(
                    booksList = expectedBooksList
                )
            )
        }
    }

    @Test
    fun `given books found on storage, then AddBooksToDatabase event is triggered with correct data`() {
        runTest {
            val expectedBooksList = listOf(
                Book(
                    title = "Book name 1",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                )
            )
            whenever(booksParser.parseHtml(any())).thenReturn(
                mutableListOf(
                    HtmlDocument(
                        heading = listOf(
                            "Highlight (yellow) - heading 1 book 1"
                        ),
                        highlight = listOf(
                            "highlight 1 book 1",
                        ),
                        bookName = "Book name 1"
                    ),
                )
            )
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())

            assertThat((viewModel.event.value as AddBooksToDatabase).booksList).isEqualTo(expectedBooksList)
        }
    }

    @Test
    fun `given books found on storage, when add to database is success, then GetBookDetails event is triggered`() {
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
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            whenever(booksListRepository.getBooksList()).thenReturn(
                Books(
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
                            )
                        )
                    )
                )
            )
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            (viewModel.event.value as AddBooksToDatabase).addBooksToDatabase.invoke(listOf())

            assertThat(viewModel.event.value).isInstanceOf(
                GetBookDetails::class.java
            )
        }
    }

    @Test
    fun `given books NOT found on storage and Database, then error state is shown`() {
        runTest {
            whenever(booksParser.parseHtml(any())).thenReturn(mutableListOf())
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
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
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
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
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            init()
            val expectedErrorMessage = "No books found in the specified location!"
            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            val actualErrorMessage = (viewModel.viewState.value as BooksListUIState.Error).error.message

            assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage)
        }
    }

    @Test
    fun `given books found on storage, when google api succeeds, then book updated with the thumbnail`() {
        runTest {
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)
            val htmlFilesList = mutableListOf(
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
            whenever(booksParser.parseHtml(any())).thenReturn(htmlFilesList)
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            whenever(booksListRepository.getBookDetails("Book name 1")).thenReturn(
                flow {
                    emit(BooksListRepoState.Success(
                        booksList = listOf(
                            Items(
                                VolumeInfo(
                                    ImageLinks(
                                        smallThumbnail = "",
                                        thumbnail = "https://www.examplethumbnail.com/1.jpg"
                                    )
                                )
                            )
                        )
                    ))
                }
            )
             val booksList = listOf(
                 Book(
                     title = "Book name 1",
                     thumbnail = "",
                     highlights = mutableListOf(
                         Highlight(
                             line = "",
                             heading = "heading 1 book 1",
                             highlightText = "highlight 1 book 1"
                         ),
                     )
                 )
             )
            init()
            val expectedState = BooksListUIState.Success(
                booksList = listOf(
                    Book(
                        title = "Book name 1",
                        thumbnail = "https://www.examplethumbnail.com/1.jpg",
                        highlights = mutableListOf(
                            Highlight(
                                line = "",
                                heading = "heading 1 book 1",
                                highlightText = "highlight 1 book 1"
                            ),
                        )
                    )
                )

            )
            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            (viewModel.event.value as AddBooksToDatabase).addBooksToDatabase.invoke(booksList)
            (viewModel.event.value as GetBookDetails).getBookDetails.invoke(booksList)

            assertThat(viewModel.viewState.value).isEqualTo(expectedState)
        }
    }

    @Test
    fun `given books found on storage, when google api succeeds, then book list updated with the thumbnail`() {
        runTest {
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)
            val htmlFilesList = mutableListOf(
                HtmlDocument(
                    heading = listOf(
                        "Highlight (yellow) - heading 1 book 1"
                    ),
                    highlight = listOf(
                        "highlight 1 book 1"
                    ),
                    bookName = "Book name 1"
                ),
                HtmlDocument(
                    heading = listOf(
                        "Highlight (yellow) - heading 1 book 1"
                    ),
                    highlight = listOf(
                        "highlight 1 book 1"
                    ),
                    bookName = "Book name 2"
                ),
            )
            whenever(booksParser.parseHtml(any())).thenReturn(htmlFilesList)
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            whenever(booksListRepository.getBookDetails("Book name 1")).thenReturn(
                flow {
                    emit(BooksListRepoState.Success(
                        booksList = listOf(
                            Items(
                                VolumeInfo(
                                    ImageLinks(
                                        smallThumbnail = "",
                                        thumbnail = "https://www.examplethumbnail.com/1.jpg"
                                    )
                                )
                            ),
                        )
                    ))
                }
            )
            whenever(booksListRepository.getBookDetails("Book name 2")).thenReturn(
                flow {
                    emit(BooksListRepoState.Success(
                        booksList = listOf(
                            Items(
                                VolumeInfo(
                                    ImageLinks(
                                        smallThumbnail = "",
                                        thumbnail = "https://www.examplethumbnail.com/2.jpg"
                                    )
                                )
                            )
                        )
                    ))
                }
            )
            val booksList = listOf(
                Book(
                    title = "Book name 1",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                ),
                Book(
                    title = "Book name 2",
                    thumbnail = "",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "heading 1 book 1",
                            highlightText = "highlight 1 book 1"
                        ),
                    )
                )
            )
            init()
            val expectedState = BooksListUIState.Success(
                booksList = listOf(
                    Book(
                        title = "Book name 1",
                        thumbnail = "https://www.examplethumbnail.com/1.jpg",
                        highlights = mutableListOf(
                            Highlight(
                                line = "",
                                heading = "heading 1 book 1",
                                highlightText = "highlight 1 book 1"
                            ),
                        )
                    ),
                    Book(
                        title = "Book name 2",
                        thumbnail = "https://www.examplethumbnail.com/2.jpg",
                        highlights = mutableListOf(
                            Highlight(
                                line = "",
                                heading = "heading 1 book 1",
                                highlightText = "highlight 1 book 1"
                            ),
                        )
                    )
                )

            )
            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            (viewModel.event.value as AddBooksToDatabase).addBooksToDatabase.invoke(booksList)
            (viewModel.event.value as GetBookDetails).getBookDetails.invoke(booksList)

            assertThat(viewModel.viewState.value).isEqualTo(expectedState)
        }
    }

    @Test
    fun `given books found on storage, when viewmodel init , then AddBooksToDatabase event is triggered`() {
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
            whenever(booksListRepository.getBooksList()).thenReturn(Books(
                booksList = emptyList()
            ))
            val events = mutableListOf<BooksListViewModel.Event>()
            viewModel.event.observeForever {
                events.add(it)
            }
            init()

            (viewModel.event.value as ParseBooksFromStorage).parseBooks.invoke(listOf())
            (viewModel.event.value as AddBooksToDatabase).addBooksToDatabase.invoke(listOf())


            assertThat(events[2]).isInstanceOf(
                AddBooksToDatabase::class.java
            )
        }
    }
}