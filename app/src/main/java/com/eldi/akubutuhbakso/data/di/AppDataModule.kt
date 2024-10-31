package com.eldi.akubutuhbakso.data.di

import com.eldi.akubutuhbakso.BuildConfig
import com.eldi.akubutuhbakso.data.LocationShareRepo
import com.eldi.akubutuhbakso.data.LocationShareRepoImpl
import com.eldi.akubutuhbakso.data.source.LocationShareRemoteSource
import com.eldi.akubutuhbakso.data.source.RealtimeDbRemoteSource
import com.google.firebase.Firebase
import com.google.firebase.database.database
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val FIREBASE_REALTIME_DB = "firebaseRealtimeDb"
private val firebaseRealtimeDbModule = module {
    single { Firebase.database(BuildConfig.FIREBASE_REALTIME_URL) }
}

private val dataModule = module {
    single<LocationShareRepo> {
        LocationShareRepoImpl(get(named(FIREBASE_REALTIME_DB)))
    }

    single<LocationShareRemoteSource>(named(FIREBASE_REALTIME_DB)) { RealtimeDbRemoteSource(get(), get()) }
}

val appDataModule = listOf(firebaseRealtimeDbModule, dataModule)
