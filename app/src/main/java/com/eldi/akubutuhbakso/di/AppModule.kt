package com.eldi.akubutuhbakso.di

import com.eldi.akubutuhbakso.BuildConfig
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

private val firebaseRealtimeDbModule = module {
    single {
        FirebaseDatabase.getInstance(BuildConfig.FIREBASE_REALTIME_URL)
    }
}

val appModule = listOf(firebaseRealtimeDbModule)
