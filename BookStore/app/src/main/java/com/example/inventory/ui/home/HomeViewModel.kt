package com.example.inventory.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.data.AuthorsRepository
import com.example.inventory.data.Book
import com.example.inventory.data.BooksRepository
import com.example.inventory.ui.author.toAuthorUiState
import com.example.inventory.ui.search.resStringToSearchText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class HomeViewModel (
    val booksRepository: BooksRepository,
    val authorsRepository: AuthorsRepository
) : ViewModel() {

        //Nó giúp xác định khoảng thời gian chờ trước khi hủy kết nối với Flow khi không có subscriber nào.
    companion object {      //Tao doi tuong tinh, co the truy cap boi tap ca doi tuong tao boi lop do, la mot bien nho ton tai xuyen suot
        private const val TIMEOUT_MILLIS = 5_000L
    }


    //Flow là một luồng dữ liệu có thể phản ứng với các thay đổi dữ liệu trong thời gian thực.
    /*

    Muc dich
        + HomeViewModel có nhiệm vụ quản lý và cung cấp dữ liệu từ cơ sở dữ liệu cho giao diện người dùng.
        + homeUiState là một StateFlow cung cấp trạng thái UI được cập nhật mỗi khi dữ liệu thay đổi trong cơ sở dữ liệu.


    Cách hoạt động:
        + Khi có bất kỳ thay đổi nào trong dữ liệu Item trong cơ sở dữ liệu, Flow từ itemsRepository.getAllItemsStream() sẽ phát ra giá trị mới.
        + Giá trị này sẽ được chuyển đổi thành một đối tượng HomeUiState, và StateFlow homeUiState sẽ phát ra giá trị mới này cho các observer đang lắng nghe.
        + StateFlow giữ trạng thái hiện tại của luồng dữ liệu, đảm bảo rằng bất kỳ đối tượng UI nào đăng ký lắng nghe sẽ nhận được giá trị mới nhất ngay lập tức.
        + viewModelScope đảm bảo rằng luồng dữ liệu này sẽ tự động bị hủy khi ViewModel bị hủy, giúp tránh rò rỉ bộ nhớ.




    ViewModel giúp cho lưu trữ các giá trị của giao diện người dùng đảm bảo không mất đi khi thay đổi cấu hình,trong vòng đời phần mềm

    MutableStateFlow: Đây là một loại biến trạng thái có khả năng thay đổi giá trị của nó
                        và phát các giá trị mới cho các thành phần khác khi dữ liệu thay đổi.
                        Nó là một biến có thể thay đổi (mutable). Nó dùng để lưu trũ các dữ liệu và cập nhật khi có thay đổi
    StateFlow: Đây là một loại biến trạng thái chỉ có thể đọc (read-only), được sử dụng để phát các giá trị từ MutableStateFlow
                  và được các thành phần UI thu nhận. Giúp UI có thể quan sát và phản ứng khi có thay đổi
                  , cho phép UI quan sát và nhận giá trị hiện tại của chuỗi tìm kiếm, nhưng không thể thay đổi giá trị này trực tiếp.

     */


    private val _searchQuery = MutableStateFlow("") //Bien nhan gia tri khi co thay doi
    val searchQuery: StateFlow<String> =_searchQuery //Bien truyen toi cac thanh phan ui va chi cho phep doc khong thay doi gia tri


    private val _selectType = MutableStateFlow("")
    val selectType: StateFlow<String> = _selectType

    private val _selectAuthor = MutableStateFlow("")
    val selectAuthor: StateFlow<String> = _selectAuthor

    private val _selectSubject = MutableStateFlow("")
    val selectSubject: StateFlow<String> = _selectSubject

    private val _selectIdAuthor = MutableStateFlow<Int?>(null)
    val selectIdAuthor: StateFlow<Int?> = _selectIdAuthor



   //Tu dong khoi tao khi cap nhat ten tac gia
    init {
        viewModelScope.launch {
            selectAuthor
                .flatMapLatest { authorName ->                      //lang nghe cac thay doi cua selectAuthor (stateFlow), khi thay doi thi goi ham thuc hien
                    if (authorName.isNotEmpty()) {
                        authorsRepository.getIdByName(authorName) //tra ve flow chua ten tac gia
                    } else {

                        flowOf(null)        // Khi tên tác giả trống, phát ra giá trị null
                    }
                }
                .filterNotNull()                // Loại bỏ giá trị null, neu co null thi khong xu li tiep theo
                .collect { authorId ->          //Gia tri nhan duoc khong phai null va tien hanh rang -- Lay ket qua tu flow va xu li no
                    _selectIdAuthor.value = authorId // Cập nhật giá trị ID tác giả
                }
        }
    }

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery // Cập nhật chuỗi tìm kiếm
    }

    fun updateSelectType(newType: String) {
        _selectType.value = newType
    }

    fun updateSelectAuthor(newAuthor: String) {
        _selectAuthor.value = newAuthor
    }

    fun updateSelectSubject(newSubject: String) {
        _selectSubject.value = newSubject
    }



    private val searchParametersFlow = combine(
        searchQuery,
        selectType,
        selectIdAuthor,
        selectSubject
    ) { query, typeSelection, authorId, subject ->
        SearchParameters(query, typeSelection, authorId, subject)
    }


    val searchUiState: StateFlow<BooksUiState> = searchParametersFlow
        .flatMapLatest { parameters -> // Phản ứng với sự thay đổi của chuỗi tìm kiếm
            if (parameters.query.isEmpty()) {
                flowOf(BooksUiState(emptyList())) // Khi chuỗi tìm kiếm trống, phát ra một HomeUiState với danh sách rỗng
            } else {
                    booksRepository.searchBooks(
                        query = parameters.query,
                        type = parameters.typeSelection,
                        subject = parameters.subject,
                        authorId = parameters.authorId
                    )
                        .map { booksList -> BooksUiState(booksList) } //Chuyen doi
                }
                // Chuyển đổi kết quả thành HomeUiState
        }
        .stateIn( // Chuyển đổi từ Flow thành StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = BooksUiState(emptyList())
        )


    //Admin Screen
    private val _adminsSearchQuery = MutableStateFlow("") //Bien nhan gia tri khi co thay doi
    val adminsSearchQuery: StateFlow<String> =_adminsSearchQuery //Bien truyen toi cac thanh phan ui va chi cho phep doc khong thay doi gia tri

    val adminsSearchUiState: StateFlow<BooksUiState> = adminsSearchQuery
        .flatMapLatest { query -> // Phản ứng với sự thay đổi của chuỗi tìm kiếm
            if (query.isEmpty()) {
                booksRepository.getAllBooksStream() // Nếu không có chuỗi tìm kiếm thì hiện tất cả sách
                    .map { booksList -> BooksUiState(booksList) } // Chuyển đổi kết quả thành HomeUiState
            } else {
                booksRepository.searchBooksByName(query) // Nếu có chuỗi tìm kiếm, tìm kiếm theo tên
                    .map { booksList -> BooksUiState(booksList) } // Chuyển đổi kết quả thành HomeUiState
            }
        }
        .stateIn( // Chuyển đổi từ Flow thành StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = BooksUiState(emptyList())
        )

    fun updateAdminSearchQuery(newQuery: String) {
        _adminsSearchQuery.value = newQuery // Cập nhật chuỗi tìm kiếm
    }


    // Nguon du lieu sach
    val BooksUiState : StateFlow<BooksUiState> = booksRepository.getAllBooksStream()
        .map { BooksUiState(it) }
        .stateIn( //Chuyen doi tu flow thanh StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = BooksUiState() //Khoi tao gai tri ban dau
        )

    //Nguon du lieu tac gia
    val AuthorsUiState : StateFlow<AuthorsUiState> = authorsRepository.getAllAuthorsStream()
        .map { AuthorsUiState(it) }
        .stateIn( //Chuyen doi tu flow thanh StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = AuthorsUiState() //Khoi tao gai tri ban dau
        )

    //Nguon du lieu trang thai luu tru
    val BooksSaveUiState: StateFlow<BooksUiState> = booksRepository.getAllSaveBooks()
        .map { BooksUiState(it) }
        .stateIn( //Chuyen doi tu flow thanh StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = BooksUiState() //Khoi tao gai tri ban dau
        )

}

/**
 * Ui State for HomeScreen
 */
data class BooksUiState(
    val bookList: List<Book> = listOf(),
)

data class AuthorsUiState(
    val authorList: List<Author> = listOf(),
)


data class SearchParameters(
    val query: String,
    val typeSelection: String,
    val authorId: Int?,
    val subject: String
)