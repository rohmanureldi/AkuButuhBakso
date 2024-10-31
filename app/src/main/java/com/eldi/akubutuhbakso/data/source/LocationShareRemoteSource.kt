package com.eldi.akubutuhbakso.data.source

import com.eldi.akubutuhbakso.data.model.UserDataResponse
import kotlinx.coroutines.flow.Flow

interface LocationShareRemoteSource {
    suspend fun updateLocation()
    suspend fun fetchAllSellerLocation(): Flow<List<UserDataResponse>>
    suspend fun fetchAllBuyerLocation()
}
