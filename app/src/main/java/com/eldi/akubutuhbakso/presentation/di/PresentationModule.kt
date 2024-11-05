package com.eldi.akubutuhbakso.presentation.di

import com.eldi.akubutuhbakso.presentation.login.LoginViewModel
import com.eldi.akubutuhbakso.presentation.map.MapViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appPresentationModule = module {
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        MapViewModel(get(), get(), get())
    }
}
