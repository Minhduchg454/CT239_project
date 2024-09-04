package com.example.inventory.ui.home


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.AuthorsRepository
import com.example.inventory.data.BooksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ListSubjectViewModel(
    savedStateHandle: SavedStateHandle, //Luu tru du lieu qua cac lan thay doi cau hinh va tai tao lai viewmodel
    private val booksRepository: BooksRepository,
    private val authorsRepository: AuthorsRepository
) : ViewModel() {

    /*
    SavedStateHandle hoạt động như một bản đồ (Map),
        +trong đó mỗi mục bao gồm một khóa (key) và một giá trị (value). Khi bạn cần lưu trữ một giá trị vào SavedStateHandle, bạn chỉ cần cung cấp khóa và giá trị tương ứng.
    Khi cần truy xuất, bạn chỉ cần sử dụng khóa đó để lấy lại giá trị.

    Ở đây, ItemDetailsDestination.itemIdArg là khóa (key), và itemId là giá trị (value) được lưu trữ trong SavedStateHandle.



    Biến itemId đóng vai trò là khóa chính xác định mặt hàng mà ViewModel hiện tại đang quản lý.
    Nó được lấy từ SavedStateHandle thông qua một khóa cụ thể (ItemDetailsDestination.itemIdArg), và tính hợp lệ của nó được đảm bảo bằng cách sử dụng checkNotNull.
    Biến này rất quan trọng vì nó xác định dữ liệu sẽ được truy xuất từ kho dữ liệu và quản lý trong ViewModel.
     */


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val subjectKey: String = checkNotNull(savedStateHandle[ListSubjectScreen.subject])

    val subjectUiState: StateFlow<BooksUiState> =
        booksRepository.searchBooksBySubject(subjectKey)
            .map { booksList -> BooksUiState(booksList) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BooksUiState() //Khoi tao gai tri ban dau
            )
    val authorsUiState : StateFlow<AuthorsUiState> =
        authorsRepository.getAllAuthorsStream().map { AuthorsUiState(it) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AuthorsUiState() //Khoi tao gai tri ban dau
            )
}





    /*
    fun updateSubjectQuery(newQuery: String) {
        _subjectQuery.value = newQuery // Cập nhật chuỗi tìm kiếm
    }










    /*
    + itemsRepository.getItemStream(itemId): Gọi phương thức này để lấy luồng (Flow) chứa dữ liệu của mặt hàng dựa trên itemId.
    + filterNotNull(): Lọc các giá trị null ra khỏi luồng, chỉ giữ lại các giá trị không null.
    + map { ... }: Chuyển đổi mỗi mặt hàng nhận được từ luồng thành đối tượng ItemDetailsUiState bằng cách sử dụng phương thức mở rộng toItemDetails(). Điều này giúp tạo ra trạng thái UI (ItemDetailsUiState) từ dữ liệu của mặt hàng.
    + stateIn(...): Biến đổi luồng Flow thành một StateFlow có trạng thái ban đầu là ItemDetailsUiState() và sẽ bắt đầu chia sẻ dữ liệu trong viewModelScope khi có ít nhất một subscriber. Luồng sẽ tiếp tục chia sẻ dữ liệu trong vòng 5 giây sau khi subscriber cuối cùng hủy đăng ký (timeout).

     */
    /*
        val authorDetailsUiState : StateFlow<AuthorDetailsUiState> =
            authorsRepository.getAuthorStream(bookDetailsUiState.value.bookDetails.bookId))
            .map { AuthorDetailsUiState(author = it) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(HomeViewModel.TIMEOUT_MILLIS),
                initialValue = AuthorDetailsUiState() //Khoi tao gai tri ban dau
            )
    */







    val AuthorsUiState : StateFlow<AuthorsUiState> =
        authorsRepository.getAllAuthorsStream().map { AuthorsUiState(it) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AuthorsUiState() //Khoi tao gai tri ban dau
            )



    val bookDetailsUiState: StateFlow<BookDetailsUiState> =
        booksRepository.getBookStream(bookId)
            .filterNotNull()
            .map {
                BookDetailsUiState(bookDetails = it.toBookDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BookDetailsUiState()
            )

    //Lấy trạng thái lưu trữ của sách dựa trên bookId được cập nhật liên tục
    val isBooKSave: StateFlow<Boolean> = //Boolean
        booksRepository.getBookSavedState(bookId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = false // Nếu không có thông tin nào khác, giá trị này là mặc định
            )


    fun DeleteBook(){
        viewModelScope.launch {
            val currentBook = bookDetailsUiState.value.bookDetails.toBook()
            booksRepository.deleteBook(currentBook)
        }
    }

    private val _subjectQuery = MutableStateFlow("") //Bien nhan gia tri khi co thay doi
    val subjectQuery: StateFlow<String> =_subjectQuery //Bien truyen toi cac thanh phan ui va chi cho phep doc khong thay doi gia tri

/*
    val subjectUiState: StateFlow<BooksUiState> = subjectQuery
        .flatMapLatest { subjectQuery ->
            booksRepository.searchBooksBySubject(subjectQuery)
                .map { booksList -> BooksUiState(booksList) }
        }
        .stateIn( //Chuyen doi tu flow thanh StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(HomeViewModel.TIMEOUT_MILLIS),
            initialValue = BooksUiState() //Khoi tao gai tri ban dau
        )

    fun updateSubjectQuery(newQuery: String) {
        _subjectQuery.value = newQuery // Cập nhật chuỗi tìm kiếm
    }

 */

}

     */