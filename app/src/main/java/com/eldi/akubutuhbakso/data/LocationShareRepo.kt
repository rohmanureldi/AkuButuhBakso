package com.eldi.akubutuhbakso.data

import com.eldi.akubutuhbakso.data.model.UserDataResponse
import kotlinx.coroutines.flow.Flow

interface LocationShareRepo {
    suspend fun updateLocation()
    suspend fun fetchAllSellerLocation(): Flow<List<UserDataResponse>>
    suspend fun fetchAllBuyerLocation()
}
