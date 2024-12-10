package com.example.inventory.ui.book

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.BooksRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class BookEditViewModel (
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BooksRepository
): ViewModel() {

    /*
    Biến có thể thay đổi trạng thái được quản lý bởi viewmodl

    Biến này lưu trữ trạng thái UI hiện tại của book,
        + bao gồm các chi tiết của item (bookDetails)
        + trạng thái hợp lệ của dữ liệu nhập (isEntryValid).
     */
    var bookEditUiState by mutableStateOf(BookUiState())
        private set


    /*
    Id của book dang quản lý lấy từ savedStateHandle
     */
    private val bookId: Int = checkNotNull(savedStateHandle[BookEditDestination.bookIdArg]) {
        "Book ID must be provided"
    }
    /*
    Nhận một đối tượng BookDetail (Các thuộc tính của book)
        + đc gán một giá trị mặc định là bookUiState.bookDetails

    Kiểm tra tính hợp lệ của dữ liệu nhập, xem các thuộc tính có trống không.
        +Nếu không trống nhận true người lại nhân false
     */
    private fun validateInput(uiState: BookDetails = bookEditUiState.bookDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && publicationInfo.isNotBlank() && shelfNumber.isNotBlank()
                    && physicalDescription.isNotBlank() && MFN > 0
                    && type != null && type > 0 // Check for a valid selection from list
                    && authorId != null && authorId > 0 // Check for a valid author selection
                    && subject != null && subject > 0 // Check for a valid subject selection
        }
    }


    /*
    Khởi tạo trạng thái của item khi ViewModel được khởi tạo.

    Khi lớp ItemEditViewModel được khởi tạo, phương thức init sẽ được gọi.
        +   Trong phương thức này, một coroutine được khởi chạy trong viewModelScope.

    Coroutine này lấy luồng dữ liệu của item từ ItemsRepository
        +   bằng cách sử dụng phương thức getItemStream(itemId).
        +   Luồng này sẽ phát ra giá trị null khi item không tồn tại và giá trị của item khi nó tồn tại.

    Hàm filterNotNull() được sử dụng để loại bỏ các giá trị null từ luồng.
    Sau đó, first() lấy giá trị đầu tiên không null từ luồng và
        + chuyển đổi nó thành ItemUiState bằng cách gọi hàm mở rộng toItemUiState(true) với đối số true để bật nút "Save" (nút lưu).

     */
    init {
        viewModelScope.launch {
            bookEditUiState = bookRepository.getBookStream(bookId)
                .filterNotNull()
                .first()
                .toBookUiState(true)
        }
    }


    /*
    Phương thức này cập nhật trạng thái UI (itemUiState)
        + khi người dùng thay đổi thông tin chi tiết của item (itemDetails).

    Nó gọi lại validateInput để cập nhật trạng thái hợp lệ (isEntryValid) dựa trên dữ liệu mới.
     */
    fun updateUiState (bookDetails: BookDetails){
        bookEditUiState = BookUiState(bookDetails = bookDetails, isEntryValid = validateInput(bookDetails))
    }

    suspend fun updateBook(){
        if (validateInput(bookEditUiState.bookDetails)) { //Kiem tra nguoi dung nhap hop le
            bookRepository.updateBook(bookEditUiState.bookDetails.toBook()) //cap nhat lai gia tri
        }
    }
}