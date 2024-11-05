package com.eldi.akubutuhbakso.utils.locations

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eldi.akubutuhbakso.utils.findActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first

@SuppressLint("MissingPermission")
fun listenToLocationChange(
    locationProvider: FusedLocationProviderClient,
    updateOnce: Boolean = false,
) = callbackFlow {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000L,
    ).setMaxUpdates(if (updateOnce) 1 else Integer.MAX_VALUE)
        .build()

    val locationCallback = LocationListener {
        trySend(it)
    }

    locationProvider.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper(),
    )

    awaitClose {
        locationProvider.removeLocationUpdates(locationCallback)
        channel.close()
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(
    locationProvider: FusedLocationProviderClient,
) = listenToLocationChange(locationProvider = locationProvider, updateOnce = true).first()

@Composable
inline fun requestLocationPermissionLauncher(
    crossinline onGranted: () -> Unit,
): ManagedActivityResultLauncher<String, Boolean> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onGranted()
            } else {
                Toast.makeText(
                    context,
                    "Please allow location permission to continue",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        },
    )
}

inline fun requestLocationPermissions(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    crossinline onGranted: () -> Unit,
) {
    val activity = context.findActivity()
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    when {
        ContextCompat.checkSelfPermission(
            context,
            permission,
        ) == PackageManager.PERMISSION_GRANTED -> {
            onGranted()
        }

        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) -> {
            launcher.launch(permission)
        }

        else -> {
            launcher.launch(permission)
        }
    }
}
