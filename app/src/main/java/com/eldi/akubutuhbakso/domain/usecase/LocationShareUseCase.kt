package com.eldi.akubutuhbakso.domain.usecase

import com.eldi.akubutuhbakso.domain.models.UserDataType
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationShareUseCase {
    fun listenToAllOnlineUsers(userName: String, role: UserRole): Flow<UserDataType>
    suspend fun updateCurrentLocation(userName: String, role: UserRole, coord: LatLng, timeStampIdentifier: String)
    suspend fun deleteUser(userName: String, role: UserRole, timeStampIdentifier: String)
}
