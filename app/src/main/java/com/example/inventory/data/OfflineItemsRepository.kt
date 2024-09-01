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

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}



class OfflineBookRepository (private val booksDao: BooksDao) : BooksRepository {
    override fun getAllBooksStream(): Flow<List<Book>> = booksDao.getAllBooks()
    override fun getBookStream(id: Int): Flow<Book?> = booksDao.getBookById(id)
    override suspend fun insertBook(book: Book) = booksDao.insert(book)
    override suspend fun deleteBook(book: Book) = booksDao.delete(book)
    override suspend fun updateBook(book: Book) = booksDao.update(book)
    override fun searchBooksByName(name: String): Flow<List<Book>> = booksDao.searchBooksByName(name)
    override fun searchBooksBySubject(subject: String): Flow<List<Book>> = booksDao.searchBooksBySubject(subject)
    override suspend fun updateSaveToLibrary(bookId: Int, saveToLibrary: Boolean) = booksDao.updateSaveToLibrary(bookId, saveToLibrary)
    override fun getBookSavedState(bookId: Int): Flow<Boolean> = booksDao.getBookSavedState(bookId)
    override fun getAllSaveBooks(): Flow<List<Book>> = booksDao.getAllSaveBooks()
}


class OfflineAuthorRepository (private val authorsDao: AuthorDao): AuthorsRepository{
    override fun getAllAuthorsStream(): Flow<List<Author>> = authorsDao.getAllAuthors()
    override fun getAuthorStream(id: Int): Flow<Author> = authorsDao.searchAuthorById(id)
    override suspend fun insertAuthor(author: Author) = authorsDao.insert(author)
    override suspend fun deleteAuthor(author: Author) = authorsDao.delete(author)
    override suspend fun updateAuthor(author: Author) = authorsDao.update(author)
    override fun searchAuthorsByName(name: String): Flow<List<Author>> = authorsDao.searchAuthorByName(name)
}

/*
interface PaymentMethod {
    fun pay (amount : Double): String
}

class CreditCardPayment : PaymentMethod {
    override fun pay (amount: Double): String {
        return "Paid $amount with CreditCard"
    }
}

class BankTransferPayment : PaymentMethod {
    override fun pay (amount: Double): String {
        return "Paid $amount with Bank Transfer"
    }
}


class CheckOut (private val paymentMethod: PaymentMethod){
    fun processPayment (amount: Double){
        println (paymentMethod.pay(amount))
    }
}


fun main() {
    val paymentMethod: PaymentMethod = CreditCardPayment()
    val checkOut = CheckOut(paymentMethod)
    checkOut.processPayment(100.00)

}


3. Tại sao có thể gán đối tượng lớp con cho biến kiểu interface?
    Kotlin cho phép bạn gán một đối tượng của bất kỳ lớp nào cài đặt (hoặc kế thừa) từ một interface
        hoặc lớp cha cho một biến có kiểu của interface hoặc lớp cha đó.

    Điều này là do:

    Đa hình (Polymorphism): Một biến có kiểu của interface hoặc lớp cha có thể chứa bất kỳ đối tượng
    nào cài đặt (hoặc kế thừa) từ nó.

    Điều này cho phép bạn viết mã linh hoạt và tổng quát hơn, vì bạn có thể thay đổi kiểu đối tượng
        được gán cho biến mà không cần thay đổi mã nơi biến đó được sử dụng.

4. Không cần từ khóa new
    Trong Kotlin, bạn không cần sử dụng từ khóa new khi khởi tạo
    một đối tượng như trong Java. Thay vào đó, bạn chỉ cần gọi constructor của lớp:



 */

