package com.eldi.akubutuhbakso.presentation.map

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.eldi.akubutuhbakso.presentation.navigation.MapDestination
import com.eldi.akubutuhbakso.utils.locations.getCurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val locationProvider: FusedLocationProviderClient,
    handle: SavedStateHandle,
) : ViewModel() {
    val args = handle.toRoute<MapDestination>()

    private val _userLastLocation = MutableStateFlow<Location?>(null)
    val userLastLocation: StateFlow<Location?> = _userLastLocation

    init {
        getCurrentUserLocation()
    }

    private fun getCurrentUserLocation() {
        viewModelScope.launch {
            val lastLocation = getCurrentLocation(
                locationProvider = locationProvider,
            )
            _userLastLocation.update {
                lastLocation
            }
        }
    }
}
