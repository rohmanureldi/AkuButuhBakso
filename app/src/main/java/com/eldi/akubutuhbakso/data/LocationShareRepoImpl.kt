package com.eldi.akubutuhbakso.data

import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.data.source.LocationShareRemoteSource
import kotlinx.coroutines.flow.Flow

class LocationShareRepoImpl(
    private val locationShareRemoteSource: LocationShareRemoteSource,
) : LocationShareRepo {

    override suspend fun updateLocation() {
        locationShareRemoteSource.updateLocation()
    }

    override suspend fun fetchAllSellerLocation(): Flow<List<UserDataResponse>> {
        return locationShareRemoteSource.fetchAllSellerLocation()
    }

    override suspend fun fetchAllBuyerLocation() {
        locationShareRemoteSource.fetchAllBuyerLocation()
    }
}
