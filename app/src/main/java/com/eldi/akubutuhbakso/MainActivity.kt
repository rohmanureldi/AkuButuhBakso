package com.eldi.akubutuhbakso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.eldi.akubutuhbakso.presentation.LoginScreen
import com.eldi.akubutuhbakso.ui.theme.AkuButuhBaksoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AkuButuhBaksoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding
                    LoginScreen()
                }
            }
        }
    }

//    @Composable
//    fun MyMap(modifier: Modifier = Modifier) {
//        val singapore = LatLng(1.35, 103.87)
//        val singaporeMarkerState = rememberMarkerState(position = singapore)
//        val cameraPositionState = rememberCameraPositionState {
//            position = CameraPosition.fromLatLngZoom(singapore, 10f)
//        }
//        GoogleMap(
//            modifier = modifier,
//            cameraPositionState = cameraPositionState
//        ) {
//            Marker(
//                state = singaporeMarkerState,
//                title = "Singapore",
//                snippet = "Marker in Singapore"
//            )
//        }
//    }
}
