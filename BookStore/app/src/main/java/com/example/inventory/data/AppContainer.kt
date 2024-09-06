package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val booksRepository: BooksRepository
    val authorsRepository: AuthorsRepository
}


/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [BookRepository], [AuthorsRepository]
     */

    override val booksRepository: BooksRepository by lazy {
        OfflineBookRepository(InventoryDatabase.getDatabase(context).bookDao())
    }

    override val authorsRepository: AuthorsRepository by lazy {
        OfflineAuthorRepository(InventoryDatabase.getDatabase(context).authorDao())
    }

}
