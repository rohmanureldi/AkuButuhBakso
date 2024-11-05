package com.eldi.akubutuhbakso.presentation.map

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.domain.usecase.LocationShareUseCase
import com.eldi.akubutuhbakso.presentation.navigation.MapDestination
import com.eldi.akubutuhbakso.utils.locations.getCurrentLocation
import com.eldi.akubutuhbakso.utils.locations.listenToLocationChange
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val useCase: LocationShareUseCase,
    private val locationProvider: FusedLocationProviderClient,
    handle: SavedStateHandle,
) : ViewModel() {
    private val args = handle.toRoute<MapDestination>()

    private val _userLastLocation = MutableStateFlow<Location?>(null)
    val userLastLocation: StateFlow<Location?> = _userLastLocation

    private val _onlineUsers = MutableStateFlow<ImmutableList<UserData>>(persistentListOf())
    val onlineUsers: StateFlow<ImmutableList<UserData>> = _onlineUsers

    init {
        if (args.userRole == UserRole.Buyer) {
            updateByUserCurrentLocation()
        } else {
            updateByUserLiveLocation()
        }

        listenToAllOnlineUsers()
    }

    private fun updateByUserCurrentLocation() {
        viewModelScope.launch {
            val lastLocation = getCurrentLocation(
                locationProvider = locationProvider,
            )

            useCase.updateCurrentLocation(
                userName = args.userName,
                role = args.userRole,
                coord = lastLocation,
            )

            _userLastLocation.update {
                lastLocation
            }
        }
    }

    private fun updateByUserLiveLocation() {
        viewModelScope.launch {
            listenToLocationChange(
                locationProvider = locationProvider,
            ).collectLatest { newLocation ->
                useCase.updateCurrentLocation(
                    userName = args.userName,
                    role = args.userRole,
                    coord = newLocation,
                )

                _userLastLocation.update {
                    newLocation
                }
            }
        }
    }

    private fun listenToAllOnlineUsers() {
        viewModelScope.launch {
            useCase.listenToAllOnlineUsers(args.userRole).collect { newUsers ->
                _onlineUsers.update { currentUsers ->
                    val allUsers = (newUsers + currentUsers).distinctBy { it.name }.filter {
                        it.name != args.userName && it.role != args.userRole.toString()
                    }
                    allUsers.toPersistentList()
                }
            }
        }
    }

    private fun removeUserLocatoin() {
    }
}
