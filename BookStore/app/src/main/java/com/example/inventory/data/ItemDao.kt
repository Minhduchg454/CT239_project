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
interface ItemDao {

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}

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


    @Query ("SELECT * FROM books WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    fun searchBooksByName(name: String): Flow<List<Book>>

    @Query ("SELECT * FROM books WHERE subject = :subject ORDER BY name ASC")
    fun searchBooksBySubject(subject: String): Flow<List<Book>>

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

    @Query("SELECT * FROM authors WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    fun searchAuthorByName(name: String): Flow<List<Author>>

    @Query ("SELECT * FROM authors ORDER BY name ASC")
    fun getAllAuthors(): Flow<List<Author>>
}
