package com.eldi.akubutuhbakso.data

import kotlinx.coroutines.flow.Flow

interface LocationShareRepo {
    suspend fun updateLocation()
    suspend fun fetchAllSellerLocation(): Flow<String?>
    suspend fun fetchAllBuyerLocation()
}
