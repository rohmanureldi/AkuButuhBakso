package com.eldi.akubutuhbakso.domain.usecase

import android.location.Location
import com.eldi.akubutuhbakso.data.LocationShareRepo
import com.eldi.akubutuhbakso.data.mapper.UserDataMapper
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationShareInteractor(
    private val repo: LocationShareRepo,
) : LocationShareUseCase {
    override suspend fun listenToAllOnlineUsers(role: UserRole): Flow<List<UserData>> {
        return repo.listenToAllOnlineUsers(role).map {
            UserDataMapper.parseToUserData(it)
        }
    }

    override suspend fun updateCurrentLocation(userName: String, role: UserRole, coord: Location) {
        repo.updateLocation(userName = userName, role = role, coord = coord)
    }

    override suspend fun deleteUser(userName: String, role: UserRole) {
    }
}
