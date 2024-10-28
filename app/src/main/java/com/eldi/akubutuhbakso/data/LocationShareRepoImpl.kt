package com.eldi.akubutuhbakso.data

import com.eldi.akubutuhbakso.data.source.LocationShareRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationShareRepoImpl(
    val locationShareRemoteSource: LocationShareRemoteSource,
) : LocationShareRepo {

    override suspend fun updateLocation() {
    }

    override suspend fun fetchAllSellerLocation(): Flow<String?> = callbackFlow {
    }

    override suspend fun fetchAllBuyerLocation() {
    }
}
