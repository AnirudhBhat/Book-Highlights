package com.abhat.bookhighlights.ui

import androidx.compose.foundation.layout.Column
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

@Composable
fun HighlightsList(
    book: Book
) {
    LazyColumn(
        modifier = Modifier.padding(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    ) {
        items(book.highlights.size) {
            HighlightsListItem(
                heading = book.highlights[it].heading,
                highlight = book.highlights[it].highlightText
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

@Composable
fun HighlightsListItem(
    heading: String,
    highlight: String
) {
    Card(
        elevation = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = heading,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            )
            Text(
                text = highlight,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun HighlightsListPreview() {
    BookHighlightsComposeTheme {
        HighlightsList(
            Book(
                title = "Make Time",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "Sample highlight from the book Make Time",
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "Sample highlight from the book Make Time",
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "Sample highlight from the book Make Time",
                    )
                )
            )
        )
    }
}