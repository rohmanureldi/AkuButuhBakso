package com.eldi.akubutuhbakso.data.source

import com.eldi.akubutuhbakso.data.model.UserDataResponseType
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationShareRemoteSource {
    suspend fun updateLocation(userName: String, role: UserRole, coord: LatLng, timeStampIdentifier: String)
    fun listenAllOnlineUsers(userName: String, role: UserRole): Flow<UserDataResponseType>
    suspend fun deleteLocation(userName: String, role: UserRole, timeStampIdentifier: String)
}
