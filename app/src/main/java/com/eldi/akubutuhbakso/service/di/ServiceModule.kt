package com.eldi.akubutuhbakso.service.di

import com.eldi.akubutuhbakso.BuildConfig
import com.eldi.akubutuhbakso.service.WsClient
import org.koin.dsl.module

val serviceModule = module {
    factory {
        WsClient(
            socketUrl = "wss://${BuildConfig.FIREBASE_REALTIME_HOST}/.ws?v=5",
        )
    }
}
