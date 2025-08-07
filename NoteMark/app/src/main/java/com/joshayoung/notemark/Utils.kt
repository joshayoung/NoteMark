package com.joshayoung.notemark

import androidx.navigation.NavBackStackEntry
import com.joshayoung.notemark.core.Screen

internal data class DataStorageWithBackstack(
    val refreshToken: String,
    val navBackStackEntry: NavBackStackEntry
)

internal fun isNotInAuthGraph(backStack: NavBackStackEntry) : Boolean
{
    val route = backStack.destination.route

    return !Screen.authRoutes.contains(route)
}
