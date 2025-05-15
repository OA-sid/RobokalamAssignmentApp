package com.example.robokalamassignmentapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.robokalamassignmentapp.data.UserPreferencesManager
import com.example.robokalamassignmentapp.ui.screens.AboutScreen
import com.example.robokalamassignmentapp.ui.screens.DashboardScreen
import com.example.robokalamassignmentapp.ui.screens.LoginScreen
import com.example.robokalamassignmentapp.ui.screens.PortfolioScreen
import com.example.robokalamassignmentapp.ui.screens.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    preferencesManager: UserPreferencesManager = UserPreferencesManager.getInstance()
) {
    var startDestination by remember { mutableStateOf("splash") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        delay(2000) // Splash screen delay
        isLoading = false
        startDestination = if (preferencesManager.isLoggedIn()) {
            "dashboard"
        } else {
            "login"
        }
    }

    if (!isLoading) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable("splash") {
                SplashScreen()
            }

            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }

            composable("dashboard") {
                DashboardScreen(
                    onPortfolioClick = { navController.navigate("portfolio") },
                    onAboutClick = { navController.navigate("about") },
                    onLogout = {
                        preferencesManager.clearLoginStatus()
                        navController.navigate("login") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }
                )
            }

            composable("portfolio") {
                PortfolioScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("about") {
                AboutScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    } else {
        SplashScreen()
    }
}
