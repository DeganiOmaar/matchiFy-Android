package com.example.matchifyandroid.Routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.matchifyandroid.Models.MissionListItem
import com.example.matchifyandroid.Screens.MissionDetailsScreen
import com.example.matchifyandroid.Screens.MissionFormScreen
import com.example.matchifyandroid.Screens.MissionsListScreen

object Routes {
    const val MISSIONS_LIST = "missions_list"
    const val MISSION_FORM = "mission_form"
    const val MISSION_DETAILS = "mission_details"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.MISSIONS_LIST
    ) {
        composable(Routes.MISSIONS_LIST) {
            MissionsListScreen(navController = navController)
        }

        composable(Routes.MISSION_FORM) {
            MissionFormScreen()
        }

        composable(Routes.MISSION_DETAILS) {
            val mission =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<MissionListItem>("mission")

            if (mission != null) {
                MissionDetailsScreen(mission = mission)
            }
        }
    }
}