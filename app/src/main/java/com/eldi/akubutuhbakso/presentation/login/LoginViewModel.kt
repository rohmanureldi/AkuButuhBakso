package com.eldi.akubutuhbakso.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.domain.usecase.LocationShareUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: LocationShareUseCase,
) : ViewModel() {
    private val _messages: MutableStateFlow<List<UserData>> = MutableStateFlow(listOf())
    val messages: StateFlow<List<UserData>?> = _messages

    fun update() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.updateLocation()
//        }
    }

    fun listen() {
        viewModelScope.launch {
            _messages.emitAll(repo.fetchAllSellerLocation())
        }
    }
}
