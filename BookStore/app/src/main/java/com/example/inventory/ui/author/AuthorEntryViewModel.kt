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

data class AuthorUiState(
    val authorDetails: AuthorDetails = AuthorDetails(),  //AuthorDetails chua thong tin nguoi dung nhap
    val isEntryValid: Boolean = false //Kiem tra hop le
)

data class AuthorDetails(
    val id: Int = 0,
    var name: String = "",
)



//AuthorDetails to Author
fun AuthorDetails.toAuthor(): Author = Author(
    id = id,
    name = name,
)


//Author to AuthorDetails
fun Author.toAuthorDetails(): AuthorDetails = AuthorDetails(
   id = id,
   name = name,
)


//Author to AuthorUiState
fun Author.toAuthorUiState(isEntryValid: Boolean = false): AuthorUiState = AuthorUiState(
    authorDetails = this.toAuthorDetails(),
    isEntryValid = isEntryValid
)
