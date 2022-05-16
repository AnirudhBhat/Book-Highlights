package com.abhat.bookhighlights

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.abhat.bookhighlights.bookslist.BooksListViewModel
import com.abhat.bookhighlights.bookslist.BooksParser

class ViewModelFactory(owner: SavedStateRegistryOwner,
                       private val booksParser: BooksParser,
                       defaultArgs: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(key: String,
                                        modelClass: Class<T>,
                                        handle: SavedStateHandle
        ):T = BooksListViewModel(handle, booksParser) as T
}