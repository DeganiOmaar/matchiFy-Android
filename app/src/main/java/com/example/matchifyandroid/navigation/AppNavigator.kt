package com.example.matchifyandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.screens.*

@Composable
fun AppNavigator(token: String? = null) {
    val navController: NavHostController = rememberNavController()

    // ✅ S’il y a un token, on va directement sur NewPasswordScreen avec token
    val startDestination = if (token != null) "newPassword/$token" else "splash"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("roleSelection") { RoleSelectionScreen(navController) }
        composable("login/talent") { LoginScreen(navController, "Talent") }
        composable("login/recruiter") { LoginScreen(navController, "Recruiter") }
        composable("signup/{role}") { backStack ->
            val role = backStack.arguments?.getString("role") ?: "Talent"
            SignupScreen(navController, role)
        }
        composable("resetPassword") { ResetPasswordScreen(navController) }

        // ✅ New password (avec ou sans token)
        composable("newPassword") { NewPasswordScreen(navController, token = null) }
        composable("newPassword/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            NewPasswordScreen(navController, token)
        }
    }
}
