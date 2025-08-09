package com.joshayoung.notemark

import androidx.navigation.NavBackStackEntry
import com.joshayoung.notemark.core.navigation.Destination

internal data class DataStorageWithBackstack(
    val refreshToken: String,
    val navBackStackEntry: NavBackStackEntry
)

internal fun isNotInAuthGraph(backStack: NavBackStackEntry) : Boolean
{
    val route = backStack.destination.route

    val authRoutes = listOf(
        Destination.Login.toString(),
        Destination.StartScreen.toString(),
        Destination.Registration.toString()
    )

    return !authRoutes.contains(route)
}
