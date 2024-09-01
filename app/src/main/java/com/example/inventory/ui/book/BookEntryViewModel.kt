package com.example.inventory.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Book
import com.example.inventory.data.BooksRepository


class BookEntryViewModel (
    val booksRepository: BooksRepository
) : ViewModel() {

    var bookUiState by mutableStateOf(BookUiState())
        private set
    /*
    Bien luu tru trang thai cua sach: , thong tin va tinh hop le cua du lieu,

    mutableStateOf là một hàm trong Jetpack Compose, giúp tạo ra một đối tượng State có thể thay đổi được.
        Bất kỳ khi nào giá trị của State này thay đổi, Jetpack Compose sẽ tự động cập nhật giao diện tương ứng.
    */

    fun updateUiState(bookDetails: BookDetails) {
        bookUiState =
            BookUiState(bookDetails = bookDetails, isEntryValid = validateInput(bookDetails))
    }


    //Kiem tra gia tri hop le truoc khi them vao database
    /*
            Nếu không truyền tham số đầu vào giá trị mặc định là bookUiState
     */
    private fun validateInput(uiState: BookDetails = bookUiState.bookDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && type.isNotBlank() && publicationInfo.isNotBlank()
                    && shelfNumber.isNotBlank() && subject.isNotBlank() && physicalDescription.isNotBlank()
        }
    }

    //Chi thuc thi khi goi no
    suspend fun saveBook() {
        if (validateInput()) {
            booksRepository.insertBook(bookUiState.bookDetails.toBook())
        }

    }

}

//Chuyen tu BookDetails sang Book
fun BookDetails.toBook(): Book = Book(
    bookId = bookId,
    name = name,
    type = type,
    authorId = authorId,
    publicationInfo = publicationInfo,
    shelfNumber = shelfNumber,
    subject = subject,
    physicalDescription = physicalDescription,
    saveToLibrary = saveToLibrary
)

/*
    Book to BookDetails
 */

fun Book.toBookDetails(): BookDetails = BookDetails(
    bookId = bookId,
    name = name,
    type = type,
    authorId = authorId,
    publicationInfo = publicationInfo,
    shelfNumber = shelfNumber,
    subject = subject,
    physicalDescription = physicalDescription,
    saveToLibrary = saveToLibrary
)


/*
    Hàm mở rộng từ một đối tượng. Bằng cách gọi tên lớp tạo đối tượng đó

     Hàm mở rộng Book.toBookUiState với tham số có giá trị mặc định là false
        + this. là đại diện cho đối tượng gọi hàm mở rộng

 */
fun Book.toBookUiState(isEntryValid: Boolean = false): BookUiState = BookUiState(
    bookDetails = this.toBookDetails(),
    isEntryValid = isEntryValid
)


data class BookUiState(
    val bookDetails: BookDetails = BookDetails(),
    val isEntryValid: Boolean = false
)


data class BookDetails(
    val bookId: Int = 0,
    val name: String = "",
    val type: String ="",
    val authorId: Int? = 0, //cho phep gia tri null
    val publicationInfo: String ="",
    val shelfNumber: String ="",
    val subject: String = "",
    val physicalDescription: String ="",
    val saveToLibrary: Boolean = false
)


