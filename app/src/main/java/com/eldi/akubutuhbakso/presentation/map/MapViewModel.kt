package com.eldi.akubutuhbakso.presentation.map

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.domain.models.UserDataType
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val useCase: LocationShareUseCase,
    private val locationProvider: FusedLocationProviderClient,
    handle: SavedStateHandle,
) : ViewModel() {
    private val args = handle.toRoute<MapDestination>()

    private val _initialLoading = MutableStateFlow(true)
    val initialLoading: StateFlow<Boolean> = _initialLoading

    private val _userLastLocation = MutableStateFlow<Location?>(null)
    val userLastLocation: StateFlow<Location?> = _userLastLocation

    private val _onlineUsers = MutableStateFlow<ImmutableList<UserData>>(persistentListOf())

    val onlineUsers: StateFlow<ImmutableList<UserData>> = useCase.listenToAllOnlineUsers(args.userName, args.userRole)
        .combine(_onlineUsers) { wsResponse, currentUsers ->
            when (wsResponse) {
                is UserDataType.ListOfUsers -> {
                    wsResponse.data.toPersistentList()
                }
                is UserDataType.UserAdded -> {
                    (currentUsers + wsResponse.data).distinctBy { it.timestampId }.toPersistentList()
                }
                is UserDataType.UserDeleted -> {
                    currentUsers.filter { it.timestampId != wsResponse.userTimestamp }.toPersistentList()
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), persistentListOf())

    fun init() {
        if (args.userRole == UserRole.Buyer) {
            updateByUserCurrentLocation()
        } else {
            updateByUserLiveLocation()
        }
    }

    private fun updateByUserCurrentLocation() {
        viewModelScope.launch {
            _initialLoading.value = true
            val lastLocation = getCurrentLocation(
                locationProvider = locationProvider,
            )

            useCase.updateCurrentLocation(
                userName = args.userName,
                role = args.userRole,
                coord = lastLocation,
                timeStampIdentifier = args.timeStampIdentifier,
            )

            _userLastLocation.update {
                lastLocation
            }

            _initialLoading.value = false
        }
    }

    private fun updateByUserLiveLocation() {
        viewModelScope.launch {
            _initialLoading.value = true

            listenToLocationChange(
                locationProvider = locationProvider,
            ).collectLatest { newLocation ->
                useCase.updateCurrentLocation(
                    userName = args.userName,
                    role = args.userRole,
                    coord = newLocation,
                    timeStampIdentifier = args.timeStampIdentifier,
                )

                _userLastLocation.update {
                    newLocation
                }
                _initialLoading.value = false
            }
        }
    }

    fun deleteUserLocation() {
        viewModelScope.launch {
            useCase.deleteUser(args.userName, args.userRole, args.timeStampIdentifier)
        }
    }
}
