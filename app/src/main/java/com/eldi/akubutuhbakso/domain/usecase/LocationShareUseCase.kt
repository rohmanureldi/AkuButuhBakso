package com.eldi.akubutuhbakso.domain.usecase

import android.location.Location
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow

interface LocationShareUseCase {
    suspend fun listenToAllOnlineUsers(role: UserRole): Flow<List<UserData>>
    suspend fun updateCurrentLocation(userName: String, role: UserRole, coord: Location)
    suspend fun deleteUser(userName: String, role: UserRole)
}
