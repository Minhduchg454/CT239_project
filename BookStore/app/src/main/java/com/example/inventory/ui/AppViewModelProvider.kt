package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.InventoryApplication
import com.example.inventory.ui.author.AuthorEditViewModel
import com.example.inventory.ui.author.AuthorEntryViewModel
import com.example.inventory.ui.author.ListAuthorViewModel
import com.example.inventory.ui.book.BookEditViewModel
import com.example.inventory.ui.book.BookEntryViewModel
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.home.ListSubjectViewModel
import com.example.inventory.ui.book.BookDetailsViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */


object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for BookDetailsViewModel
        initializer {
            BookDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.booksRepository,
                inventoryApplication().container.authorsRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                inventoryApplication().container.booksRepository,
                inventoryApplication().container.authorsRepository
            )
        }

        // Initializer for BookEntryViewModel
        initializer {
            BookEntryViewModel(
                inventoryApplication().container.booksRepository,
            )
        }

        // Initializer for AuthorEntryViewModel
        initializer {
            AuthorEntryViewModel(
                inventoryApplication().container.authorsRepository,
            )
        }

        // Initializer for BookEditViewModel
        initializer {
            BookEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.booksRepository
            )
        }

        // Initializer for ListSubjectViewModel
        initializer {
            ListSubjectViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.booksRepository,
                inventoryApplication().container.authorsRepository
            )
        }

        // Initializer for ListAuthorViewModel
        initializer {
            ListAuthorViewModel(
                inventoryApplication().container.authorsRepository
            )
        }

        // Initializer for AuthorEditViewModel
        initializer {
            AuthorEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.authorsRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
