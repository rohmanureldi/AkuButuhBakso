package com.eldi.akubutuhbakso.data.source

import android.location.Location
import android.util.Log
import com.eldi.akubutuhbakso.data.mapper.UserDataMapper
import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.service.WsClient
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeDbRemoteSource(
    private val db: FirebaseDatabase,
    private val wsClient: WsClient,
) : LocationShareRemoteSource {
    private val TAG = "FirestoreRemoteSource"

    override suspend fun updateLocation(userName: String, role: UserRole, coord: Location) {
        runCatching {
            val myRef = db.getReference("bakso/indonesia/${role.name.lowercase()}")
                .child("${role.name.lowercase()}-$userName")

            myRef.setValue(
                mapOf(
                    "coord" to "${coord.latitude},${coord.longitude}",
                    "name" to userName,
                    "role" to role.name,
                ),
            )
        }.getOrElse {
            Log.e(TAG, "updateLocation: ", it)
        }
    }

    override suspend fun listenAllOnlineUsers(role: UserRole): Flow<List<UserDataResponse>> {
        return callbackFlow {
            val socketListener = object : WsClient.LocationShareSocketListener {
                override fun onOpen() {
                    super.onOpen()
                    val targetUser = if (role == UserRole.Buyer) {
                        UserRole.Seller
                    } else {
                        UserRole.Buyer
                    }

                    wsClient.requestListen(targetUser)
                }

                override fun onMessage(message: String) {
                    super.onMessage(message)
                    val mapped = UserDataMapper.parseFromWsMessage(message)
                    trySend(mapped)
                }
            }

            wsClient.connect(socketListener)

            awaitClose { wsClient.disconnect() }
        }
    }
}
