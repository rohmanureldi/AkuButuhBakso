package com.eldi.akubutuhbakso.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eldi.akubutuhbakso.presentation.login.LoginScreen
import com.eldi.akubutuhbakso.presentation.map.MapScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = LoginDestination,
    ) {
        composable<LoginDestination> {
            LoginScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .statusBarsPadding(),
                onLoginClick = { userName, userRole ->
                    navController.navigate(
                        route = MapDestination(
                            userName = userName,
                            userRole = userRole,
                        ),
                    )
                },
            )
        }
        composable<MapDestination> {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
