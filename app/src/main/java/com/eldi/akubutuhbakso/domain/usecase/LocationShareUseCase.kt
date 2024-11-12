package com.eldi.akubutuhbakso.domain.usecase

import android.location.Location
import com.eldi.akubutuhbakso.domain.models.UserDataType
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow

interface LocationShareUseCase {
    fun listenToAllOnlineUsers(userName: String, role: UserRole): Flow<UserDataType>
    suspend fun updateCurrentLocation(userName: String, role: UserRole, coord: Location, timeStampIdentifier: String)
    suspend fun deleteUser(userName: String, role: UserRole, timeStampIdentifier: String)
}
