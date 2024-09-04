package com.example.inventory.ui.author

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.AuthorsRepository
import com.example.inventory.ui.home.AuthorsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ListAuthorViewModel(
    private val authorsRepository: AuthorsRepository
): ViewModel() {

    companion object { //Tao doi tuong tinh, co the truy cap boi tap ca doi tuong tao boi lop do, la mot bien nho ton tai xuyen suot
        private const val TIMEOUT_MILLIS = 5_000L
    }


    val authorsUiState : StateFlow<AuthorsUiState> = authorsRepository.getAllAuthorsStream()
        .map { AuthorsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = AuthorsUiState()
        )


}