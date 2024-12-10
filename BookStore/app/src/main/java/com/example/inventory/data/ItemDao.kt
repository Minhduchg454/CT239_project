package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 2.
 * Khai bao cac phuong thuc cua thuc the
 */

//Dao se tu dong sinh ma nguon dua tren cac cau truy van. Vi vay sd interface
@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: BOOK)

    @Update
    suspend fun update(book: BOOK)

    @Delete
    suspend fun delete(book: BOOK)

    @Query("SELECT * FROM BOOK WHERE BOOK_Id = :bookId")
    fun getBookById(bookId: Int): Flow<BOOK>

    @Query("SELECT * FROM BOOK ORDER BY BOOK_Name ASC")
    fun getAllBooks(): Flow<List<BOOK>>

    @Query("SELECT * FROM BOOK WHERE BOOK_saveToLibrary = 1 ORDER BY BOOK_Name ASC")
    fun getAllSaveBooks(): Flow<List<BOOK>>

    @Query("SELECT * FROM BOOK WHERE LOWER(BOOK_Name) LIKE '%' || LOWER(:name) || '%' ORDER BY BOOK_Name ASC")
    fun searchBooksByName(name: String): Flow<List<BOOK>>

    @Query("SELECT * FROM BOOK WHERE SUBJECT_Id = :subjectId ORDER BY BOOK_Name ASC")
    fun searchBooksBySubject(subjectId: Int): Flow<List<BOOK>>

    @Query("SELECT * FROM BOOK WHERE BT_Id = :typeId ORDER BY BOOK_Name ASC")
    fun searchBooksByType(typeId: Int): Flow<List<BOOK>>

    @Query("SELECT * FROM BOOK WHERE AUTHOR_Id = :authorId ORDER BY BOOK_Name ASC")
    fun searchBooksByAuthor(authorId: Int): Flow<List<BOOK>>

    @Query("UPDATE BOOK SET BOOK_saveToLibrary = :saveToLibrary WHERE BOOK_Id = :bookId")
    suspend fun updateSaveToLibrary(bookId: Int, saveToLibrary: Boolean)

    @Query("SELECT BOOK_saveToLibrary FROM BOOK WHERE BOOK_Id = :bookId")
    fun getBookSavedState(bookId: Int): Flow<Boolean>

    @Query("""
        SELECT * FROM BOOK 
        WHERE 
            (LOWER(BOOK_Name) LIKE '%' || LOWER(:query) || '%' OR :query IS NULL) AND 
            (BT_Id = :typeId OR :typeId IS NULL) AND
            (SUBJECT_Id = :subjectId OR :subjectId IS NULL) AND
            (AUTHOR_Id = :authorId OR :authorId IS NULL)
        ORDER BY BOOK_Name ASC
    """)
    fun searchBooks(
        query: String? = null,
        typeId: Int? = null,
        subjectId: Int? = null,
        authorId: Int? = null
    ): Flow<List<BOOK>>
}


@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(author: AUTHOR)

    @Update
    suspend fun update(author: AUTHOR)

    @Delete
    suspend fun delete(author: AUTHOR)

    @Query("SELECT * FROM AUTHOR WHERE AUTHOR_Id = :authorId ORDER BY AUTHOR_Name ASC")
    fun searchAuthorById(authorId: Int): Flow<AUTHOR>

    @Query("SELECT * FROM AUTHOR WHERE LOWER(AUTHOR_Name) LIKE '%' || LOWER(:name) || '%' ORDER BY AUTHOR_Name ASC")
    fun searchAuthorByName(name: String): Flow<List<AUTHOR>>

    @Query("SELECT * FROM AUTHOR ORDER BY AUTHOR_Name ASC")
    fun getAllAuthors(): Flow<List<AUTHOR>>

    @Query("SELECT AUTHOR_Id FROM AUTHOR WHERE LOWER(AUTHOR_Name) = LOWER(:name)")
    fun getIdByName(name: String): Flow<Int>
}

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(subject: SUBJECT)

    @Update
    suspend fun update(subject: SUBJECT)

    @Delete
    suspend fun delete(subject: SUBJECT)

    @Query("SELECT * FROM SUBJECT WHERE SUBJECT_Id = :subjectId")
    fun getSubjectById(subjectId: Int): Flow<SUBJECT>

    @Query("SELECT * FROM SUBJECT ORDER BY SUBJECT_Id ASC")
    fun getAllSubjects(): Flow<List<SUBJECT>>

    @Query("SELECT * FROM SUBJECT WHERE LOWER(SUBJECT_Name) LIKE '%' || LOWER(:name) || '%' ORDER BY SUBJECT_Name ASC")
    fun searchSubjectByName(name: String): Flow<List<SUBJECT>>

    @Query("SELECT SUBJECT_Id FROM SUBJECT WHERE LOWER(SUBJECT_Name) = LOWER(:name)")
    fun getIdByName(name: String): Flow<Int>
}

/**
 * Database access object for the BOOK_TYPE table
 */
@Dao
interface BookTypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookType: BOOK_TYPE)

    @Update
    suspend fun update(bookType: BOOK_TYPE)

    @Delete
    suspend fun delete(bookType: BOOK_TYPE)

    @Query("SELECT * FROM BOOK_TYPE WHERE BT_Id = :bookTypeId")
    fun getBookTypeById(bookTypeId: Int): Flow<BOOK_TYPE>

    @Query("SELECT * FROM BOOK_TYPE ORDER BY BT_Name ASC")
    fun getAllBookTypes(): Flow<List<BOOK_TYPE>>

    @Query("SELECT * FROM BOOK_TYPE WHERE LOWER(BT_Name) LIKE '%' || LOWER(:name) || '%' ORDER BY BT_Name ASC")
    fun searchBookTypeByName(name: String): Flow<List<BOOK_TYPE>>


    @Query("SELECT BT_Id FROM BOOK_TYPE WHERE LOWER(BT_Name) = LOWER(:name)")
    fun getIdByName(name: String): Flow<Int>
}