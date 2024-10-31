package com.eldi.akubutuhbakso.domain.usecase

import com.eldi.akubutuhbakso.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface LocationShareUseCase {
    suspend fun fetchAllSellerLocation(): Flow<List<UserData>>
}
