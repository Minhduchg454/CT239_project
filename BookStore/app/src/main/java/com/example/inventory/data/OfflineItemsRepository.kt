package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
    *4
    *Cai dat cac phuong thuc truy xuat du lieu tu database
 */


class OfflineBookRepository(private val booksDao: BooksDao) : BooksRepository {
    override fun getAllBooksStream(): Flow<List<BOOK>> = booksDao.getAllBooks()
    override fun getBookStream(id: Int): Flow<BOOK?> = booksDao.getBookById(id)
    override suspend fun insertBook(book: BOOK) = booksDao.insert(book)
    override suspend fun deleteBook(book: BOOK) = booksDao.delete(book)
    override suspend fun updateBook(book: BOOK) = booksDao.update(book)
    override fun searchBooksByName(name: String): Flow<List<BOOK>> = booksDao.searchBooksByName(name)
    override fun searchBooksBySubject(subjectId: Int): Flow<List<BOOK>> = booksDao.searchBooksBySubject(subjectId)
    override fun searchBooksByAuthor(authorId: Int): Flow<List<BOOK>> = booksDao.searchBooksByAuthor(authorId)
    override fun searchBooksByType(typeId: Int): Flow<List<BOOK>> = booksDao.searchBooksByType(typeId)

    override suspend fun updateSaveToLibrary(bookId: Int, saveToLibrary: Boolean) = booksDao.updateSaveToLibrary(bookId, saveToLibrary)
    override fun getBookSavedState(bookId: Int): Flow<Boolean> = booksDao.getBookSavedState(bookId)
    override fun getAllSaveBooks(): Flow<List<BOOK>> = booksDao.getAllSaveBooks()
    override fun searchBooks(
        query: String?,
        typeId: Int?,
        subjectId: Int?,
        authorId: Int?
    ): Flow<List<BOOK>> = booksDao.searchBooks(query, typeId, subjectId, authorId)
}

class OfflineAuthorRepository(private val authorsDao: AuthorDao) : AuthorsRepository {
    override fun getAllAuthorsStream(): Flow<List<AUTHOR>> = authorsDao.getAllAuthors()
    override fun getAuthorStream(id: Int): Flow<AUTHOR?> = authorsDao.searchAuthorById(id)
    override suspend fun insertAuthor(author: AUTHOR) = authorsDao.insert(author)
    override suspend fun deleteAuthor(author: AUTHOR) = authorsDao.delete(author)
    override suspend fun updateAuthor(author: AUTHOR) = authorsDao.update(author)
    override fun searchAuthorsByName(name: String): Flow<List<AUTHOR>> = authorsDao.searchAuthorByName(name)
    override fun getIdByName(name: String): Flow<Int> = authorsDao.getIdByName(name)
}

/**
 * Repository for [SUBJECT] providing methods to insert, update, delete, and retrieve subjects.
 */
class OfflineSubjectRepository(private val subjectDao: SubjectDao) : SubjectsRepository {
    override fun getAllSubjectsStream(): Flow<List<SUBJECT>> = subjectDao.getAllSubjects()
    override fun getSubjectStream(id: Int): Flow<SUBJECT?> = subjectDao.getSubjectById(id)
    override suspend fun insertSubject(subject: SUBJECT) = subjectDao.insert(subject)
    override suspend fun deleteSubject(subject: SUBJECT) = subjectDao.delete(subject)
    override suspend fun updateSubject(subject: SUBJECT) = subjectDao.update(subject)
    override fun searchSubjectsByName(name: String): Flow<List<SUBJECT>> = subjectDao.searchSubjectByName(name)
    override fun getIdByName(name: String): Flow<Int> = subjectDao.getIdByName(name)
}

/**
 * Repository for [BOOK_TYPE] providing methods to insert, update, delete, and retrieve book types.
 */
class OfflineBookTypeRepository(private val bookTypeDao: BookTypeDao) : BookTypesRepository {
    override fun getAllBookTypesStream(): Flow<List<BOOK_TYPE>> = bookTypeDao.getAllBookTypes()
    override fun getBookTypeStream(id: Int): Flow<BOOK_TYPE?> = bookTypeDao.getBookTypeById(id)
    override suspend fun insertBookType(bookType: BOOK_TYPE) = bookTypeDao.insert(bookType)
    override suspend fun deleteBookType(bookType: BOOK_TYPE) = bookTypeDao.delete(bookType)
    override suspend fun updateBookType(bookType: BOOK_TYPE) = bookTypeDao.update(bookType)
    override fun searchBookTypesByName(name: String): Flow<List<BOOK_TYPE>> = bookTypeDao.searchBookTypeByName(name)
    override fun getIdByName(name: String): Flow<Int> = bookTypeDao.getIdByName(name)
}

