package com.eldi.akubutuhbakso.data

import android.location.Location
import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.data.source.LocationShareRemoteSource
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow

class LocationShareRepoImpl(
    private val locationShareRemoteSource: LocationShareRemoteSource,
) : LocationShareRepo {

    override suspend fun updateLocation(userName: String, role: UserRole, coord: Location) {
        locationShareRemoteSource.updateLocation(userName = userName, role = role, coord = coord)
    }

    override suspend fun listenToAllOnlineUsers(role: UserRole): Flow<List<UserDataResponse>> {
        return locationShareRemoteSource.listenAllOnlineUsers(role)
    }
}
