package com.abhat.bookhighlights.bookslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abhat.bookhighlights.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BooksListViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val booksUiState: MutableStateFlow<BooksListUIState> = MutableStateFlow(BooksListUIState.Loading)
    val viewState: StateFlow<BooksListUIState> = booksUiState.asStateFlow()

    val event: SingleLiveEvent<Event> = SingleLiveEvent()

    init {
        event.value = Event.CheckStoragePermission(::onStoragePermissionsResult)
    }

    private fun onStoragePermissionsResult(
        permissionGranted: Boolean,
        showRationale: Boolean
    ) {
        event.value = Event.ParseBooksFromStorage
    }


    sealed class Event {
        data class CheckStoragePermission(
            val onStoragePermissionsResult: (
                permissionGranted: Boolean,
                showRationale: Boolean
            ) -> Unit
        ) : Event()
        object ParseBooksFromStorage : Event()
    }
}