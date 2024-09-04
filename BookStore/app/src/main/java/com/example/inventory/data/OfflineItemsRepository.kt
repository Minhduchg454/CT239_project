package com.example.inventory.data

import kotlinx.coroutines.flow.Flow


class OfflineBookRepository (private val booksDao: BooksDao) : BooksRepository {
    override fun getAllBooksStream(): Flow<List<Book>> = booksDao.getAllBooks()
    override fun getBookStream(id: Int): Flow<Book?> = booksDao.getBookById(id)
    override suspend fun insertBook(book: Book) = booksDao.insert(book)
    override suspend fun deleteBook(book: Book) = booksDao.delete(book)
    override suspend fun updateBook(book: Book) = booksDao.update(book)
    override fun searchBooksByName(name: String): Flow<List<Book>> = booksDao.searchBooksByName(name)
    override fun searchBooksBySubject(subject: String): Flow<List<Book>> = booksDao.searchBooksBySubject(subject)
    override fun searchBooksByAuthor(authorId: Int): Flow<List<Book>> = booksDao.searchBooksByAuthor(authorId)
    override fun searchBooksByType(type: String): Flow<List<Book>> = booksDao.searchBooksByType(type)

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
    override fun getIdByName(name: String): Flow<Int> = authorsDao.getIdByName(name)

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

