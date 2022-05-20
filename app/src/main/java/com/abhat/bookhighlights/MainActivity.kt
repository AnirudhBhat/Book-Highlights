package com.abhat.bookhighlights

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.abhat.anumathi.PermissionResult
import com.abhat.anumathi.anumathi
import com.abhat.anumathi.registerPermissions
import com.abhat.bookhighlights.bookslist.BooksListUIState
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.CheckStoragePermission
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.ParseBooksFromStorage
import com.abhat.bookhighlights.bookslist.parser.BooksParser
import com.abhat.bookhighlights.bookslist.parser.HtmlParser
import com.abhat.bookhighlights.di.BooksListGraph
import com.abhat.bookhighlights.di.NetworkGraphImpl
import com.abhat.bookhighlights.navigation.Navigation
import com.abhat.bookhighlights.ui.BooksList
import com.abhat.bookhighlights.ui.BottomBar
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private val booksListGraph: BooksListGraph by lazy { NetworkGraphImpl(this) }
    private val viewModel: BooksListViewModel by viewModels {
        ViewModelFactory(this, booksListGraph.booksListRepository, BooksParser(HtmlParser))
    }
    private val registerPermission = registerPermissions()




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