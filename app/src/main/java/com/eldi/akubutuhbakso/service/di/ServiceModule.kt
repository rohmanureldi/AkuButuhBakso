package com.eldi.akubutuhbakso.service.di

import com.eldi.akubutuhbakso.BuildConfig
import com.eldi.akubutuhbakso.service.WsClient
import okhttp3.OkHttpClient
import org.koin.dsl.module

val serviceModule = module {
    single {
        WsClient(
            client = OkHttpClient(),
            socketUrl = "wss://${BuildConfig.FIREBASE_REALTIME_HOST}/.ws?v=5",
        )
    }
}
