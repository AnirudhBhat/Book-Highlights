package com.abhat.bookhighlights.ui

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme

@Composable
fun BottomBar() {
    var selectedState = rememberSaveable {
        mutableStateOf("Highlights")
    }

    BottomAppBar(
        elevation = 12.dp,
    ) {
        BottomNavigationItem(
            icon = {
                //Icon(Icons.Filled.Favorite , "")
            },
            label = { Text(text = "Highlights") },
            selectedContentColor= Color.Black,
            unselectedContentColor= Color.Black.copy(alpha = 0.4f),
            selected = selectedState.value == "Highlights",
            onClick = {
                selectedState.value = "Highlights"
            },
            alwaysShowLabel = true
        )

        BottomNavigationItem(
            icon = {
                //Icon(Icons.Filled.Favorite , "")
            },
            label = { Text(text = "Starred") },
            selectedContentColor= Color.Black,
            unselectedContentColor= Color.Black.copy(alpha = 0.4f),
            selected = selectedState.value == "Starred",
            onClick = {
                selectedState.value = "Starred"
            },
            alwaysShowLabel = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BookHighlightsComposeTheme {
        BottomBar()
    }
}