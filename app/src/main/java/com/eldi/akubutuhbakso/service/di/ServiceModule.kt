package com.eldi.akubutuhbakso.service.di

import com.eldi.akubutuhbakso.BuildConfig
import com.eldi.akubutuhbakso.service.WsClient
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val serviceModule = module {
    factory {
        OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
    }

    factory {
        WsClient(
            client = get(),
            socketUrl = "wss://${BuildConfig.FIREBASE_REALTIME_HOST}/.ws?v=5",
        )
    }
}
