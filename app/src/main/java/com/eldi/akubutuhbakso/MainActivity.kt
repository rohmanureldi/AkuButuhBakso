package com.eldi.akubutuhbakso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.eldi.akubutuhbakso.presentation.navigation.AppNavigation
import com.eldi.akubutuhbakso.ui.theme.AkuButuhBaksoTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KoinContext {
                val navController = rememberNavController()
                AkuButuhBaksoTheme {
                    AppNavigation(
                        navController = navController,
                    )
                }
            }
        }
    }
}
