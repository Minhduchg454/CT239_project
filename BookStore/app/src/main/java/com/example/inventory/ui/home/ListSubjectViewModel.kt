package com.example.inventory.ui.home


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.AuthorsRepository
import com.example.inventory.data.BooksRepository
import com.example.inventory.data.SubjectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListSubjectViewModel(
    savedStateHandle: SavedStateHandle, //Luu tru du lieu qua cac lan thay doi cau hinh va tai tao lai viewmodel
    val booksRepository: BooksRepository,
    val authorsRepository: AuthorsRepository,
    val subjectsRepository: SubjectsRepository
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

    val authorsUiState : StateFlow<AuthorsUiState> =
        authorsRepository.getAllAuthorsStream().map { AuthorsUiState(it) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AuthorsUiState() //Khoi tao gai tri ban dau
            )


    val subjectsUiState : StateFlow<SubjectsUiState> =
        subjectsRepository.getAllSubjectsStream().map { SubjectsUiState(it) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SubjectsUiState() //Khoi tao gai tri ban dau
            )

    val subjectKey: String = checkNotNull(savedStateHandle[ListSubjectScreen.subject])

    val subjectForBooktUiState: StateFlow<BooksUiState> =
        booksRepository.searchBooksBySubject(subjectKey.toInt())
            .map { booksList -> BooksUiState(booksList) }
            .stateIn( //Chuyen doi tu flow thanh StateFlow
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BooksUiState() //Khoi tao gai tri ban dau
            )


}

