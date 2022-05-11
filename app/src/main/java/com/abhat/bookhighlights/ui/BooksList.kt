package com.abhat.bookhighlights.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme

@ExperimentalMaterialApi
@Composable
fun BooksList(
    booksList: List<Book>,
    onBookClick: (String) -> Unit
) {
    LazyColumn {
        items(booksList.size) {
            BooksListItem(
                title = booksList[it].title,
                onBookClick = onBookClick
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color.LightGray,
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BooksListItem(
    title: String,
    onBookClick: (String) -> Unit
) {
    Card(
        elevation = 16.dp,
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onBookClick(title)
        }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun BooksListPreview() {
    BookHighlightsComposeTheme {
        BooksList(
            booksList = listOf(
                Book(
                    title = "Make Time",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "",
                            highlightText = "Sample highlight from the book Make Time",
                        )
                    )
                ),
                Book(
                    title = "The Culture Code",
                    highlights = mutableListOf(
                        Highlight(
                            line = "",
                            heading = "",
                            highlightText = "Sample highlight from the book The Culture Code",
                        ),
                        Highlight(
                            line = "",
                            heading = "",
                            highlightText = "Another sample highlight from the book The Culture Code",
                        )
                    )
                )
            ),
            onBookClick = {}
        )
    }
}