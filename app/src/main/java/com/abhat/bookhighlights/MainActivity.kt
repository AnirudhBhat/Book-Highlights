package com.abhat.bookhighlights

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.abhat.anumathi.PermissionResult
import com.abhat.anumathi.anumathi
import com.abhat.anumathi.registerPermissions
import com.abhat.bookhighlights.bookslist.BooksListUIState
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.CheckStoragePermission
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.ParseBooksFromStorage
import com.abhat.bookhighlights.bookslist.BooksParser
import com.abhat.bookhighlights.bookslist.HtmlParser
import com.abhat.bookhighlights.ui.BooksList
import com.abhat.bookhighlights.ui.BottomBar
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private val viewModel: BooksListViewModel by viewModels {
        ViewModelFactory(this, BooksParser(HtmlParser))
    }
    private val registerPermission = registerPermissions()


    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents()
        val onBookClick = { title: String ->
        }
        setContent {
            BookHighlightsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        bottomBar = { BottomBar() },
                        content = { padding ->
                            val books: BooksListUIState by viewModel.viewState.observeAsState(BooksListUIState.Loading)
                            when (books) {
                                is BooksListUIState.Error -> {

                                }
                                BooksListUIState.Loading -> {

                                }
                                is BooksListUIState.Success -> {
                                    BooksList(
                                        booksList = books.booksList,
                                        paddingValues = padding,
                                        onBookClick = onBookClick
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                is CheckStoragePermission -> {
                    lifecycleScope.launch {
                        val result = anumathi(
                            registerPermission,
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                        handlePermissionResult(result, event)
                    }
                }
                is ParseBooksFromStorage -> {
                    event.parseBooks.invoke(File(
                        getExternalFilesDir(
                            Environment.DIRECTORY_DOWNLOADS
                        )?.toURI()
                    ).listFiles().toList())
                }
            }
        })
    }

    private fun handlePermissionResult(
        result: Map<String, PermissionResult>,
        event: CheckStoragePermission
    ) {
        result.map { (permission, state) ->
            when (state) {
                is PermissionResult.Denied -> {
                        event.onStoragePermissionsResult
                            .invoke(false, state.shouldShowRationale)
                }
                PermissionResult.Granted -> {
                    event.onStoragePermissionsResult
                        .invoke(true, false)
                }
            }
        }
    }
}