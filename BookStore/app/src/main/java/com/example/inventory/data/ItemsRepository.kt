package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
 * 3.
 * Tao giao dien de truy xuat du lieu tu database
 */



/**
 * Repository for [Author] providing methods to insert, update, delete, and retrieve authors.
 */
interface BooksRepository {
    fun getAllBooksStream(): Flow<List<BOOK>>
    fun getBookStream(id: Int): Flow<BOOK?>
    suspend fun insertBook(book: BOOK)
    suspend fun deleteBook(book: BOOK)
    suspend fun updateBook(book: BOOK)
    fun searchBooksByName(name: String): Flow<List<BOOK>>
    fun searchBooksBySubject(subjectId: Int): Flow<List<BOOK>>
    fun searchBooksByAuthor(authorId: Int): Flow<List<BOOK>>
    fun searchBooksByType(typeId: Int): Flow<List<BOOK>>
    suspend fun updateSaveToLibrary(bookId: Int, saveToLibrary: Boolean)
    fun getBookSavedState(bookId: Int): Flow<Boolean>
    fun getAllSaveBooks(): Flow<List<BOOK>>
    fun searchBooks(
        query: String? = null,
        typeId: Int? = null,
        subjectId: Int? = null,
        authorId: Int? = null
    ): Flow<List<BOOK>>
}

/**
 * Repository for [AUTHOR] providing methods to insert, update, delete, and retrieve authors.
 */
interface AuthorsRepository {
    fun getAllAuthorsStream(): Flow<List<AUTHOR>>
    fun getAuthorStream(id: Int): Flow<AUTHOR?>
    suspend fun insertAuthor(author: AUTHOR)
    suspend fun deleteAuthor(author: AUTHOR)
    suspend fun updateAuthor(author: AUTHOR)
    fun searchAuthorsByName(name: String): Flow<List<AUTHOR>>
    fun getIdByName(name: String): Flow<Int>
}

/**
 * Repository for [SUBJECT] providing methods to insert, update, delete, and retrieve subjects.
 */
interface SubjectsRepository {
    fun getAllSubjectsStream(): Flow<List<SUBJECT>>
    fun getSubjectStream(id: Int): Flow<SUBJECT?>
    suspend fun insertSubject(subject: SUBJECT)
    suspend fun deleteSubject(subject: SUBJECT)
    suspend fun updateSubject(subject: SUBJECT)
    fun searchSubjectsByName(name: String): Flow<List<SUBJECT>>
    fun getIdByName(name: String): Flow<Int>
}

/**
 * Repository for [BOOK_TYPE] providing methods to insert, update, delete, and retrieve book types.
 */
interface BookTypesRepository {
    fun getAllBookTypesStream(): Flow<List<BOOK_TYPE>>
    fun getBookTypeStream(id: Int): Flow<BOOK_TYPE?>
    suspend fun insertBookType(bookType: BOOK_TYPE)
    suspend fun deleteBookType(bookType: BOOK_TYPE)
    suspend fun updateBookType(bookType: BOOK_TYPE)
    fun searchBookTypesByName(name: String): Flow<List<BOOK_TYPE>>
    fun getIdByName(name: String): Flow<Int>
}

