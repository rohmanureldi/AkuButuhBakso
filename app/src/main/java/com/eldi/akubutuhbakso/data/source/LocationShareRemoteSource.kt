package com.eldi.akubutuhbakso.data.source

import android.location.Location
import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow

interface LocationShareRemoteSource {
    suspend fun updateLocation(userName: String, role: UserRole, coord: Location)
    suspend fun listenAllOnlineUsers(role: UserRole): Flow<List<UserDataResponse>>
}
