package com.eldi.akubutuhbakso.utils.di

import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }
}
