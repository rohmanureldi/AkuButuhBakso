package com.eldi.akubutuhbakso.data

import com.eldi.akubutuhbakso.data.model.UserDataResponseType
import com.eldi.akubutuhbakso.data.source.LocationShareRemoteSource
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class LocationShareRepoImpl(
    private val locationShareRemoteSource: LocationShareRemoteSource,
) : LocationShareRepo {

    override suspend fun updateLocation(userName: String, role: UserRole, coord: LatLng, timeStampIdentifier: String) {
        locationShareRemoteSource.updateLocation(userName = userName, role = role, coord = coord, timeStampIdentifier = timeStampIdentifier)
    }

    override fun listenToAllOnlineUsers(userName: String, role: UserRole): Flow<UserDataResponseType> {
        return locationShareRemoteSource.listenAllOnlineUsers(userName, role)
    }

    override suspend fun deleteLocation(userName: String, role: UserRole, timeStampIdentifier: String) {
        locationShareRemoteSource.deleteLocation(userName = userName, role = role, timeStampIdentifier = timeStampIdentifier)
    }
}
