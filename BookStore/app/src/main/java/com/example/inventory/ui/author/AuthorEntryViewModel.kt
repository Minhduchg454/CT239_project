package com.example.inventory.ui.author

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Author
import com.example.inventory.data.AuthorsRepository


class AuthorEntryViewModel (
    private val authorsRepository: AuthorsRepository
): ViewModel() {
    var authorUiState by mutableStateOf(AuthorUiState())
        private set

    fun updateUiState(authorDetails: AuthorDetails) {
        authorUiState =
            AuthorUiState(authorDetails = authorDetails, isEntryValid = validateInput(authorDetails))
    }


    //Kiem tra gia tri hop le truoc khi them vao database
    /*
            Nếu không truyền tham số đầu vào giá trị mặc định là bookUiState
     */
    private fun validateInput(uiState: AuthorDetails = authorUiState.authorDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() }
    }

    //Chi thuc thi khi goi no
    suspend fun saveAuthor() {
        if (validateInput()) {
            authorsRepository.insertAuthor(authorUiState.authorDetails.toAuthor())
        }

    }
}

fun AuthorDetails.toAuthor(): Author = Author(
    id = id,
    name = name,
)



data class AuthorDetails(
    val id: Int = 0,
    var name: String = "",
)

data class AuthorUiState(
    val authorDetails: AuthorDetails = AuthorDetails(),
    val isEntryValid: Boolean = false
)