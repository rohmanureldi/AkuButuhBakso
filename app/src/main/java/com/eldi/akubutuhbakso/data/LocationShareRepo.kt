package com.eldi.akubutuhbakso.data

import android.location.Location
import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow

interface LocationShareRepo {
    suspend fun updateLocation(userName: String, role: UserRole, coord: Location)
    suspend fun listenToAllOnlineUsers(role: UserRole): Flow<List<UserDataResponse>>
}
