/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface ItemsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<Item>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<Item?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Item)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: Item)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: Item)
}



interface BooksRepository {
    fun getAllBooksStream(): Flow<List<Book>>
    fun getBookStream(id: Int): Flow<Book?>
    suspend fun insertBook(book: Book)
    suspend fun deleteBook(book: Book)
    suspend fun updateBook(book: Book)
    fun searchBooksByName(name: String): Flow<List<Book>>
    fun searchBooksBySubject(subject: String): Flow<List<Book>>
    suspend fun updateSaveToLibrary(bookId: Int, saveToLibrary: Boolean)
    fun getBookSavedState(bookId: Int): Flow<Boolean>
    fun getAllSaveBooks(): Flow<List<Book>>
}

interface AuthorsRepository {
    fun getAllAuthorsStream(): Flow<List<Author>>
    fun getAuthorStream(id: Int): Flow<Author>
    suspend fun insertAuthor(author: Author)
    suspend fun deleteAuthor(author: Author)
    suspend fun updateAuthor(author: Author)
    fun searchAuthorsByName(name: String): Flow<List<Author>>
}