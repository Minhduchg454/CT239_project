package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */


@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("SELECT * FROM books Where bookId = :bookId")
    fun getBookById(bookId: Int): Flow<Book>

    @Query ("SELECT * FROM books ORDER By name ASC") //asc: tang dan (A->Z) (0->9) desc: giam dan
    fun getAllBooks(): Flow<List<Book>>

    @Query ("SELECT * FROM books WHERE saveToLibrary = 1 ORDER BY name ASC")
    fun getAllSaveBooks(): Flow<List<Book>>


    @Query ("SELECT * FROM books WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%' ORDER BY name ASC")
    fun searchBooksByName(name: String): Flow<List<Book>>

    @Query ("SELECT * FROM books WHERE LOWER(subject) LIKE '%' || LOWER(:subject) || '%' ORDER BY name ASC")
    fun searchBooksBySubject(subject: Int): Flow<List<Book>>

    @Query ("SELECT * FROM books WHERE LOWER(type) LIKE '%' || LOWER(:type) || '%' ORDER BY name ASC")
    fun searchBooksByType(type: Int): Flow<List<Book>>

    @Query ("SELECT * FROM books WHERE authorId = :authorId ORDER BY name ASC")
    fun searchBooksByAuthor(authorId: Int): Flow<List<Book>>




    @Query("UPDATE books SET saveToLibrary = :saveToLibrary WHERE bookId = :bookId")
    suspend fun updateSaveToLibrary(bookId: Int, saveToLibrary: Boolean)

    @Query("SELECT saveToLibrary FROM books WHERE bookId = :bookId")
    fun getBookSavedState(bookId: Int): Flow<Boolean>
    //Để ui phản hồi theo sự thay đổi dữ liệu nên dùng flow

}

@Dao
interface AuthorDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(author: Author)

    @Update
    suspend fun update(author: Author)

    @Delete
    suspend fun delete(author: Author)

    @Query("Select * FROM authors WHERE Id = :authorId ORDER BY name ASC")
    fun searchAuthorById (authorId: Int): Flow<Author>

    @Query("SELECT * FROM authors WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%' ORDER BY name ASC")
    fun searchAuthorByName(name: String): Flow<List<Author>>

    @Query ("SELECT * FROM authors ORDER BY name ASC")
    fun getAllAuthors(): Flow<List<Author>>

    @Query("SELECT id FROM authors WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%'")
    fun getIdByName(name: String): Flow<Int>

}
