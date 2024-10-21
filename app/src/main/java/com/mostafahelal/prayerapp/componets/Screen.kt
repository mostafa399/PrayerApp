package com.mostafahelal.prayerapp.components

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")

    object Qibla : Screen(
        route = "qibla/{latitude}/{longitude}",
        navArguments = listOf(
            navArgument("latitude") {
                type = NavType.StringType
            },
            navArgument("longitude") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(latitude: Double, longitude: Double): String {
            return "qibla/$latitude/$longitude"
        }
    }
}
