package com.eldi.akubutuhbakso.utils.locations

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private fun Context.checkLocationPermission() {
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    } else {
//        startListeningForLocationUpdates()
//    }
}

@SuppressLint("MissingPermission")
private fun startListeningForLocationUpdates(context: Context) {
//    val locationRequest = LocationRequest().apply {
//        interval = 10000 // Update interval in milliseconds
//        fastestInterval = 5000
//        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    fusedLocationClient.lastLocation.addOnSuccessListener {
//        it?.latitude
//    }
//    fusedLocationClient.requestLocationUpdates(
//        locationRequest,
//        locationCallback,
//        Looper.getMainLooper()
//    )
}

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
                Toast.makeText(context, "Please allow location permission to continue", Toast.LENGTH_SHORT).show()
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

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
