package com.eldi.akubutuhbakso.data.source

import android.util.Log
import com.eldi.akubutuhbakso.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeDbRemoteSource : LocationShareRemoteSource {
    private val TAG = "FirestoreRemoteSource"

    override suspend fun updateLocation() {
        val database = Firebase.database(BuildConfig.FIREBASE_REALTIME_URL)
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
    }

    override suspend fun fetchAllSellerLocation(): Flow<String?> = callbackFlow {
        val database = Firebase.database

        val myRef = database.getReference("message")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")

                trySend(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override suspend fun fetchAllBuyerLocation() {
    }
}
