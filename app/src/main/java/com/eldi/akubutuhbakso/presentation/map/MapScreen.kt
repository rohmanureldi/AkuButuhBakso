package com.eldi.akubutuhbakso.presentation.map

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
//    userRole: UserRole,
    modifier: Modifier = Modifier,
) {
    val vm: MapViewModel = koinViewModel()

    val userLastLocation by vm.userLastLocation.collectAsStateWithLifecycle()

    val userLocation by remember(userLastLocation) {
        val coord = userLastLocation?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)
        mutableStateOf(coord)
    }
    val markerState = rememberMarkerState(position = userLocation)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }

    LaunchedEffect(userLocation) {
        markerState.position = userLocation
        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier,
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = markerState,
                title = "User Location",
                snippet = "You are here",
            )
        }
    }
}
