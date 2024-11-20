package com.eldi.akubutuhbakso.presentation.map

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eldi.akubutuhbakso.R
import com.eldi.akubutuhbakso.presentation.components.CircleButton
import com.eldi.akubutuhbakso.presentation.components.CloseMapSheet
import com.eldi.akubutuhbakso.ui.theme.Paddings
import com.eldi.akubutuhbakso.utils.locations.getCurrentLocation
import com.eldi.akubutuhbakso.utils.locations.listenToLocationChange
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    userRole: UserRole,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: MapViewModel = koinViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    val userLastLocation by vm.userLastLocation.collectAsStateWithLifecycle()
    val allOnlineUsers by vm.onlineUsers.collectAsStateWithLifecycle()
    val initialLoading by vm.initialLoading.collectAsStateWithLifecycle()

    val userLocation by remember(userLastLocation) {
        val coord = userLastLocation?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)
        mutableStateOf(coord)
    }
    val markerState = rememberMarkerState(position = userLocation)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }

    val closeSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val fusedProvider: FusedLocationProviderClient = koinInject()

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val closeBottomSheetAction = remember {
        {
            scope.launch { closeSheetState.hide() }.invokeOnCompletion {
                if (!closeSheetState.isVisible) {
                    showBottomSheet = false
                }
            }
        }
    }

    val onCloseClick = remember {
        {
            showBottomSheet = true
        }
    }

    val onCloseOk = remember {
        {
            vm.deleteUserLocation()
            closeBottomSheetAction()
            onBackClick()
        }
    }

    val onCloseCancel: () -> Unit = remember {
        {
            closeBottomSheetAction()
        }
    }

    BackHandler(
        onBack = onCloseClick,
    )
    LaunchedEffect(userLocation) {
        markerState.position = userLocation
        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }

    LifecycleResumeEffect(Unit, lifecycleOwner = lifecycleOwner) {
        this.lifecycleScope.launch {
            if (userRole == UserRole.Buyer) {
                val lastLocation =
                    getCurrentLocation(fusedProvider).let { LatLng(it.latitude, it.longitude) }
                vm.updateUserLocation(lastLocation)
            } else {
                listenToLocationChange(fusedProvider).collectLatest { location ->
                    val lastLocation = location.let { LatLng(it.latitude, it.longitude) }
                    vm.updateUserLocation(lastLocation)
                }
            }
        }

        onPauseOrDispose {
            vm.deleteUserLocation()
        }
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier,
            cameraPositionState = cameraPositionState,
        ) {
            if (!initialLoading) {
                MapMarker(
                    markerState = markerState,
                    text = "Lokasi Anda",
                    iconRes = if (userRole == UserRole.Buyer) R.drawable.ic_buyer else R.drawable.ic_seller,
                )
            }

            allOnlineUsers.forEach {
                key(it.timestampId) {
                    MapMarker(
                        markerState = MarkerState(position = it.coord),
                        text = it.name,
                        iconRes = it.iconRes,
                    )
                }
            }
        }

        CircleButton(
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopEnd)
                .padding(Paddings.small),
            onCloseClick = onCloseClick,
            iconContent = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            },
        )

        if (initialLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }

        if (showBottomSheet) {
            CloseMapSheet(
                sheetState = closeSheetState,
                onDismissRequest = {
                    showBottomSheet = false
                },
                onCloseOk = onCloseOk,
                onCloseCancel = onCloseCancel,
            )
        }
    }
}

@Composable
private fun MapMarker(
    markerState: MarkerState,
    text: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
) {
    MarkerComposable(
        state = markerState,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                color = Color.Transparent,
                modifier = Modifier.padding(
                    horizontal = Paddings.small + Paddings.extraSmall,
                    vertical = Paddings.extraSmall,
                ),
            ) // just to make the icon centered to its location

            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Surface(
                shape = RoundedCornerShape(Paddings.extraSmall),
                modifier = Modifier.padding(start = Paddings.extraSmall),
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(
                        horizontal = Paddings.small,
                        vertical = Paddings.extraSmall,
                    ),
                )
            }
        }
    }
}
