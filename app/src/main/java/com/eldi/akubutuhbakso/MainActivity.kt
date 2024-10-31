package com.eldi.akubutuhbakso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.eldi.akubutuhbakso.presentation.navigation.AppNavigation
import com.eldi.akubutuhbakso.ui.theme.AkuButuhBaksoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AkuButuhBaksoTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    innerPadding
//                    LoginScreen(
//                        modifier = Modifier.fillMaxSize().imePadding().statusBarsPadding(),
//                    )
//                }
                AppNavigation(
                    navController = navController,
                )
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
