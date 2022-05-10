package com.abhat.bookhighlights.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar() {
    var selectedState = remember {
        "Highlight"
    }

    BottomAppBar(
        elevation = 8.dp,
        content = {
            Row {
                BottomNavigationItem(
                    icon = {
                        //Icon(Icons.Filled.Favorite , "")
                    },
                    label = { Text(text = "Highlights") },
                    selected = selectedState == "Highlights",
                    onClick = {
                        selectedState = "Highlights"
                    },
                    alwaysShowLabel = true
                )

                BottomNavigationItem(
                    icon = {
                        //Icon(Icons.Filled.Favorite , "")
                    },
                    label = { Text(text = "Starred") },
                    selected = selectedState == "Starred",
                    onClick = {
                        selectedState = "Starred"
                    },
                    alwaysShowLabel = true
                )
            }
        }
    )
}