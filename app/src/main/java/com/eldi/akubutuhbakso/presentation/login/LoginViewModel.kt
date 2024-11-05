package com.eldi.akubutuhbakso.presentation.login

import androidx.lifecycle.ViewModel
import com.eldi.akubutuhbakso.domain.usecase.LocationShareUseCase

class LoginViewModel(
    private val repo: LocationShareUseCase,
) : ViewModel()
