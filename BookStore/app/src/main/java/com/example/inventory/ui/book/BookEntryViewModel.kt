package com.example.inventory.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.BOOK
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
            name.isNotBlank() && publicationInfo.isNotBlank() && shelfNumber.isNotBlank()
                    && physicalDescription.isNotBlank() && MFN > 0
                    && type != null && type > 0 // Check for a valid selection from list
                    && authorId != null && authorId > 0 // Check for a valid author selection
                    && subject != null && subject > 0 // Check for a valid subject selection
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
fun BookDetails.toBook(): BOOK = BOOK(
    BOOK_Id = bookId,
    BOOK_Name = name,
    BT_Id = type,
    AUTHOR_Id = authorId,
    Book_PublicationInfo = publicationInfo,
    BOOK_ShelfNumber = shelfNumber,
    SUBJECT_Id = subject,
    BOOK_PhysicalDescription = physicalDescription,
    BOOK_saveToLibrary = saveToLibrary,
    Book_MFN = MFN
)

/*
    Book to BookDetails
 */

fun BOOK.toBookDetails(): BookDetails = BookDetails(
    bookId = BOOK_Id,
    name = BOOK_Name,
    type = BT_Id,
    authorId = AUTHOR_Id,
    publicationInfo = Book_PublicationInfo,
    shelfNumber = BOOK_ShelfNumber,
    subject = SUBJECT_Id,
    physicalDescription = BOOK_PhysicalDescription,
    saveToLibrary = BOOK_saveToLibrary,
    MFN = Book_MFN
)


/*
    Hàm mở rộng từ một đối tượng. Bằng cách gọi tên lớp tạo đối tượng đó

     Hàm mở rộng Book.toBookUiState với tham số có giá trị mặc định là false
        + this. là đại diện cho đối tượng gọi hàm mở rộng

 */
fun BOOK.toBookUiState(isEntryValid: Boolean = false): BookUiState = BookUiState(
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
    val type: Int? = 0,
    val authorId: Int? = 0, //cho phep gia tri null
    val publicationInfo: String ="",
    val shelfNumber: String ="",
    val subject: Int? = 0,
    val physicalDescription: String ="",
    val saveToLibrary: Boolean = false,
    val MFN: Int = 0
)


