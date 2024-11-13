package com.eldi.akubutuhbakso.data.source

import android.util.Log
import com.eldi.akubutuhbakso.data.mapper.UserDataMapper
import com.eldi.akubutuhbakso.data.model.UserDataResponseType
import com.eldi.akubutuhbakso.service.WsClient
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class RealtimeDbRemoteSource(
    private val db: FirebaseDatabase,
    private val wsClient: WsClient,
) : LocationShareRemoteSource {
    private val TAG = "FirestoreRemoteSource"

    override suspend fun updateLocation(
        userName: String,
        role: UserRole,
        coord: LatLng,
        timeStampIdentifier: String,
    ) {
        runCatching {
            withContext(Dispatchers.IO) {
                val myRef = db.getDbReferencePath(
                    username = userName,
                    role = role,
                    timeStampIdentifier = timeStampIdentifier,
                )

                myRef.setValue(
                    mapOf(
                        "coord" to "${coord.latitude},${coord.longitude}",
                        "name" to userName,
                        "role" to role.name,
                        "timestampId" to timeStampIdentifier,
                    ),
                )
            }
        }.getOrElse {
            Log.e(TAG, "updateLocation: ", it)
        }
    }

    override fun listenAllOnlineUsers(
        userName: String,
        role: UserRole,
    ): Flow<UserDataResponseType> {
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
                    mapped?.let {
                        trySend(it)
                    }
                }
            }

            wsClient.connect(socketListener)

            awaitClose {
                wsClient.disconnect()
            }
        }
    }

    override suspend fun deleteLocation(
        userName: String,
        role: UserRole,
        timeStampIdentifier: String,
    ) {
        withContext(Dispatchers.IO + NonCancellable) {
            runCatching {
                val myRef = db.getDbReferencePath(
                    username = userName,
                    role = role,
                    timeStampIdentifier = timeStampIdentifier,
                )
                myRef.setValue(null)
            }.getOrElse {
                Log.e(TAG, "deleteLocation: ", it)
            }
        }
    }

    private fun FirebaseDatabase.getDbReferencePath(
        username: String,
        role: UserRole,
        timeStampIdentifier: String,
    ): DatabaseReference {
        return getReference("bakso/indonesia/${role.name.lowercase()}").child("${role.name.lowercase()}-$username-$timeStampIdentifier")
    }
}
