package com.abhat.bookhighlights

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.ParseBooksFromStorage
import com.abhat.bookhighlights.bookslist.parser.BooksParser
import com.abhat.bookhighlights.bookslist.parser.HtmlParser
import com.abhat.bookhighlights.di.BooksListGraph
import com.abhat.bookhighlights.di.NetworkGraphImpl
import com.abhat.bookhighlights.navigation.Navigation
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private val booksListGraph: BooksListGraph by lazy { NetworkGraphImpl(this) }
    private val viewModel: BooksListViewModel by viewModels {
        ViewModelFactory(this, booksListGraph.booksListRepository, BooksParser(HtmlParser))
    }

    @OptIn(ExperimentalMaterialApi::class,
        androidx.compose.foundation.ExperimentalFoundationApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents()
        val onBookClick = { title: String ->
        }
        setContent {
            BookHighlightsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(viewModel)
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                is ParseBooksFromStorage -> {
                    lifecycleScope.launch {
                        event.parseBooks.invoke(File(
                            getExternalFilesDir(
                                Environment.DIRECTORY_DOWNLOADS
                            )?.toURI()
                        ).listFiles().toList())
                    }
                }
                is BooksListViewModel.Event.GetBookDetails -> {
                    event.getBookDetails.invoke(event.booksList)
                }
                is BooksListViewModel.Event.AddBooksToDatabase -> {
                    event.addBooksToDatabase.invoke(event.booksList)
                }
                is BooksListViewModel.Event.GetBooksListFromDatabase -> {
                    event.getBooksFromDatabase.invoke()
                }
            }
        })
    }
}