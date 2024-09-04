package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */

interface BooksRepository {
    fun getAllBooksStream(): Flow<List<Book>>
    fun getBookStream(id: Int): Flow<Book?>
    suspend fun insertBook(book: Book)
    suspend fun deleteBook(book: Book)
    suspend fun updateBook(book: Book)
    fun searchBooksByName(name: String): Flow<List<Book>>
    fun searchBooksBySubject(subject: String): Flow<List<Book>>
    fun searchBooksByAuthor(authorId: Int): Flow<List<Book>>
    fun searchBooksByType(type: String): Flow<List<Book>>


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
    fun getIdByName(name: String): Flow<Int>
}