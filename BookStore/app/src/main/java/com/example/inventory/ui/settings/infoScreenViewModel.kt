package com.example.inventory.ui.settings


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class infoScreenViewModel: ViewModel(){

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun authenticate(password: String) {
        if (password == "123456") {
            _isAuthenticated.value = true
        }else{
            _isAuthenticated.value = false
        }
    }

    fun resetAuthentication() {
        _isAuthenticated.value = false
    }
}