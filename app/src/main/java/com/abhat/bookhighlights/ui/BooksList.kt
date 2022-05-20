package com.abhat.bookhighlights.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun BooksList(
    booksList: List<Book>,
    paddingValues: PaddingValues,
    onBookClick: (Book) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.padding(
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        items(booksList.size) {
            BooksListItem(
                book = booksList[it],
                thumbnail = booksList[it].thumbnail,
                onBookClick = onBookClick
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BooksListItem(
    book: Book,
    thumbnail: String,
    onBookClick: (Book) -> Unit
) {
    Card(
        elevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onBookClick(book)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            AsyncImage(
                model = thumbnail.replace("http", "https"),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(220.dp)
                    .width(150.dp)
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun BooksListPreview() {
    BookHighlightsComposeTheme {
        BooksList(
            booksList = listOf(
                Book(
                    title = "Make Time",
                    thumbnail = "",
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
                    thumbnail = "",
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
            paddingValues = PaddingValues(24.dp),
            onBookClick = {}
        )
    }
}