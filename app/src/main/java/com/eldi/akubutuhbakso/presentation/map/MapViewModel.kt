package com.eldi.akubutuhbakso.presentation.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.domain.models.UserDataType
import com.eldi.akubutuhbakso.domain.usecase.LocationShareUseCase
import com.eldi.akubutuhbakso.presentation.navigation.MapDestination
import com.google.android.gms.maps.model.LatLng
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val useCase: LocationShareUseCase,
    handle: SavedStateHandle,
) : ViewModel() {
    private val args = handle.toRoute<MapDestination>()

    private val _initialLoading = MutableStateFlow(true)
    val initialLoading: StateFlow<Boolean> = _initialLoading

    private val _userLastLocation = MutableStateFlow<LatLng?>(null)
    val userLastLocation: StateFlow<LatLng?> = _userLastLocation

    val onlineUsers: StateFlow<ImmutableList<UserData>> =
        useCase.listenToAllOnlineUsers(args.userName, args.userRole)
            .runningFold(persistentListOf<UserData>()) { initial, wsResponse ->
                when (wsResponse) {
                    is UserDataType.ListOfUsers -> {
                        wsResponse.data.toPersistentList()
                    }

                    is UserDataType.UserAdded -> {
                        (initial.removeAll { it.timestampId == wsResponse.data.timestampId } + wsResponse.data).distinctBy { it.timestampId }.toPersistentList()
                    }

                    is UserDataType.UserDeleted -> {
                        initial.filter { it.timestampId != wsResponse.timestampId }
                            .toPersistentList()
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000L), persistentListOf())

    fun updateUserLocation(coord: LatLng) {
        viewModelScope.launch {
            useCase.updateCurrentLocation(
                userName = args.userName,
                role = args.userRole,
                coord = coord,
                timeStampIdentifier = args.timeStampIdentifier,
            )

            _userLastLocation.update {
                coord
            }

            if (_initialLoading.value) {
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
