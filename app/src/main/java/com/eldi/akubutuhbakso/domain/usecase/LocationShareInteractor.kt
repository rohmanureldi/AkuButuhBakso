package com.eldi.akubutuhbakso.domain.usecase

import android.location.Location
import com.eldi.akubutuhbakso.data.LocationShareRepo
import com.eldi.akubutuhbakso.data.mapper.UserDataMapper
import com.eldi.akubutuhbakso.domain.models.UserDataType
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationShareInteractor(
    private val repo: LocationShareRepo,
) : LocationShareUseCase {
    override fun listenToAllOnlineUsers(
        userName: String,
        role: UserRole,
    ): Flow<UserDataType> {
        return repo.listenToAllOnlineUsers(userName, role).map {
            UserDataMapper.parseToUserData(it)
        }
    }

    override suspend fun updateCurrentLocation(
        userName: String,
        role: UserRole,
        coord: Location,
        timeStampIdentifier: String,
    ) {
        repo.updateLocation(
            userName = userName,
            role = role,
            coord = coord,
            timeStampIdentifier = timeStampIdentifier,
        )
    }

    override suspend fun deleteUser(
        userName: String,
        role: UserRole,
        timeStampIdentifier: String,
    ) {
        repo.deleteLocation(
            userName = userName,
            role = role,
            timeStampIdentifier = timeStampIdentifier,
        )
    }
}
