package com.eldi.akubutuhbakso.domain.usecase

import com.eldi.akubutuhbakso.data.LocationShareRepo
import com.eldi.akubutuhbakso.data.mapper.UserDataMapper
import com.eldi.akubutuhbakso.domain.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationShareInteractor(
    private val repo: LocationShareRepo,
) : LocationShareUseCase {
    override suspend fun fetchAllSellerLocation(): Flow<List<UserData>> {
        return repo.fetchAllSellerLocation().map {
            UserDataMapper.parseToUserData(it)
        }
    }
}
