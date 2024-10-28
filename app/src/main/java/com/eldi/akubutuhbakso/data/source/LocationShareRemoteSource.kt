package com.eldi.akubutuhbakso.data.source

import kotlinx.coroutines.flow.Flow

interface LocationShareRemoteSource {
    suspend fun updateLocation()
    suspend fun fetchAllSellerLocation(): Flow<String?>
    suspend fun fetchAllBuyerLocation()
}
