package com.abhat.bookhighlights.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme

@Composable
fun HighlightOfTheDay(
    highlight: String,
    book: String
) {
    Card(
        elevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = highlight,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = book,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighlightOfTheDayPreview() {
    BookHighlightsComposeTheme {
        HighlightOfTheDay(
            highlight = "Health lies in action, and so it graces youth. To be busy is the secret of grace, and half the secret of content. Let us ask the gods not for possessions, but for things to do; happiness is in making things rather than in consuming them",
            book = "Test book title")
    }
}