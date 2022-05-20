package com.abhat.bookhighlights.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.ui.HighlightsList
import com.abhat.bookhighlights.ui.theme.MainScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Navigation(viewModel: BooksListViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.BooksListScreen.route) {
        composable(route = Screen.BooksListScreen.route) {
            MainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.HighlightsListScreen.route + "/{book}",
            arguments = listOf(
                navArgument(name = "book") {
                    type = BookParamType()
                }
            )
        ) { entry ->
            HighlightsList(book = entry.arguments?.getParcelable("book")!!)
        }
    }
}