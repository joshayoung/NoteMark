package com.joshayoung.notemark.core.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

interface Navigator {
    val startDestination: Destination
    val navigationAction: Flow<NavigationAction>

    suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit = {}
    )

    suspend fun previousBackStackEntry(key: String, id: String)

    suspend fun navigateUp()
}

class DefaultNavigator(
    override val startDestination: Destination,
) : Navigator {
    private val _navigationActions = Channel<NavigationAction>()

    override val navigationAction = _navigationActions.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        _navigationActions.send(NavigationAction.Navigate(
            destination,
            navOptions
        ))
    }

    override suspend fun previousBackStackEntry(key: String, id: String) {
        _navigationActions.send(
            NavigationAction.NavigateUpWithBackStack(key, id)
        )
    }

    override suspend fun navigateUp() {
        _navigationActions.send(NavigationAction.NavigateUp)
    }
}