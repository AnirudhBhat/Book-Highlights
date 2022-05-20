package com.abhat.bookhighlights.ui.theme

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.abhat.bookhighlights.bookslist.BooksListUIState
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.navigation.Screen
import com.abhat.bookhighlights.ui.BooksList
import com.abhat.bookhighlights.ui.BottomBar
import com.google.gson.Gson

@ExperimentalMaterialApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: BooksListViewModel,
) {
    val onBookClick = { book: Book ->
        val json = Uri.encode(Gson().toJson(book))
        navController.navigate(Screen.HighlightsListScreen.route + "/$json")
    }
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