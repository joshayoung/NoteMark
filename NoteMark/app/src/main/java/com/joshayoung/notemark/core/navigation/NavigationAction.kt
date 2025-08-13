package com.joshayoung.notemark.core.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigationAction {
    data class Navigate(
        val destination: Destination,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ): NavigationAction

    data object NavigateUp: NavigationAction
    data class NavigateUpWithBackStack(var key: String, var id: String): NavigationAction
}