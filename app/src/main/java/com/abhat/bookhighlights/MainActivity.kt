package com.abhat.bookhighlights

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.abhat.anumathi.PermissionResult
import com.abhat.anumathi.anumathi
import com.abhat.anumathi.registerPermissions
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksListViewModel.Event.CheckStoragePermission
import com.abhat.bookhighlights.ui.BottomBar
import com.abhat.bookhighlights.ui.theme.BookHighlightsComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val viewModel: BooksListViewModel by viewModels()
    private val registerPermission = registerPermissions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvents()
        setContent {
            BookHighlightsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        bottomBar = { BottomBar() }
                    ) {

                    }
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
                                Manifest.permission.ACCESS_MEDIA_LOCATION
                            )
                        )
                        handlePermissionResult(result, event)
                    }
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