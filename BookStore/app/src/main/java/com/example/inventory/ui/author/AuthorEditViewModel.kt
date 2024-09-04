package com.example.inventory.ui.author

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.AuthorsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthorEditViewModel(
    savedStateHandle: SavedStateHandle,
    val authorsRepository: AuthorsRepository
): ViewModel() {

    var authorEditUiState by mutableStateOf(AuthorUiState())

    val authorId: Int = checkNotNull(savedStateHandle[AuthorEditDestination.authorIdArg]) {
        "Author id is required"
    }

    //Tao ngay khi duoc goi, chuyen doi Author thanh AuthorUiState
    init {
        viewModelScope.launch {
            authorEditUiState = authorsRepository.getAuthorStream(authorId)
                .filterNotNull()
                .first()
                .toAuthorUiState(true)
        }
    }

    //Kiem tra nguoi dung nhap hop le
    private fun validateInput(uiState: AuthorDetails = authorEditUiState.authorDetails): Boolean {
        with(uiState) {
            return name.isNotBlank() //Khong trong du lieu
        }
    }

    //Cap nhat gia tri khi nguoi dung thay doi
    fun updateUiState(authorDetails: AuthorDetails) {
        authorEditUiState = AuthorUiState(
            authorDetails = authorDetails,
            isEntryValid = validateInput(authorDetails)
        )
    }

    //Cap nhat vao co so du lieu
    suspend fun updateAuthor() {
        if (validateInput(authorEditUiState.authorDetails)) {
            authorsRepository.updateAuthor(authorEditUiState.authorDetails.toAuthor())
        }
    }
    suspend fun deleteAuthor() {
        authorsRepository.deleteAuthor(authorEditUiState.authorDetails.toAuthor())
    }

}