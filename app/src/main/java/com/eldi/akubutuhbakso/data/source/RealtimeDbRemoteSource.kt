package com.eldi.akubutuhbakso.data.source

import android.util.Log
import com.eldi.akubutuhbakso.data.mapper.UserDataMapper
import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.service.WsClient
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeDbRemoteSource(
    private val db: FirebaseDatabase,
    private val wsClient: WsClient,
) : LocationShareRemoteSource {
    private val TAG = "FirestoreRemoteSource"

    override suspend fun updateLocation() {
        Log.e(TAG, "updateLocation: ")
        runCatching {
            val myRef = db.getReference("message")

            myRef.setValue("Hello, World!")
        }.getOrElse {
            Log.e(TAG, "updateLocation: ", it)
        }
    }

    override suspend fun fetchAllSellerLocation(): Flow<List<UserDataResponse>> = callbackFlow {
        val socketListener = object : WsClient.LocationShareSocketListener {
            override fun onOpen() {
                super.onOpen()
                wsClient.requestListenToSeller()
            }

            override fun onMessage(message: String) {
                super.onMessage(message)
                Log.e(TAG, "onMessage: $message")
                val mapped = UserDataMapper.parseFromWsMessage(message)

                trySend(mapped)
            }
        }

        wsClient.connect(socketListener)

        awaitClose { wsClient.disconnect() }
    }

    override suspend fun fetchAllBuyerLocation() {
    }
}
