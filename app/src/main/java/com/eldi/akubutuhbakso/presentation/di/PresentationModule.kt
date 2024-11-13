package com.eldi.akubutuhbakso.presentation.di

import com.eldi.akubutuhbakso.presentation.map.MapViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appPresentationModule = module {
    viewModel {
        MapViewModel(get(), get())
    }
}
