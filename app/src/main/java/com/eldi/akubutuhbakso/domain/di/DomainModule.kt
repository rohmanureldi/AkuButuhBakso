package com.eldi.akubutuhbakso.domain.di

import com.eldi.akubutuhbakso.domain.usecase.LocationShareInteractor
import com.eldi.akubutuhbakso.domain.usecase.LocationShareUseCase
import org.koin.dsl.module

val appDomainModule = module {
    single<LocationShareUseCase> {
        LocationShareInteractor(get())
    }
}
