package com.joshayoung.notemark.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import com.joshayoung.notemark.auth.presentation.log_in.LoginScreenRoot
import com.joshayoung.notemark.auth.presentation.registration.RegistrationScreenRoot
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.NavigationAction
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.note.presentation.add_note.AddEditNoteScreenRoot
import com.joshayoung.notemark.note.presentation.note_detail.NoteDetailScreenRoot
import com.joshayoung.notemark.note.presentation.note_list.NoteListScreenRoot
import com.joshayoung.notemark.note.presentation.settings.SettingsScreenRoot
import com.joshayoung.notemark.note.presentation.start.GettingStartedScreenRoot

@Composable
fun NavigationRoot(
    modifier: Modifier,
    viewModel: MainViewModel,
    navigator: Navigator,
    navController: NavHostController,
    isAuthenticated: Boolean,
) {
    ObserveAsEvents(flow = navigator.navigationActions) { action ->
        when (action) {
            is NavigationAction.Navigate ->
                navController.navigate(
                    action.destination,
                ) {
                    action.navOptions(this)
                }
            NavigationAction.NavigateUp -> navController.navigateUp()
            is NavigationAction.NavigateUpWithBackStack -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(action.key, action.id)
                navController.popBackStack()
            }
        }
    }

    SetNavigationGraph(
        modifier = modifier,
        viewModel = viewModel,
        navController = navController,
        isAuthenticated = isAuthenticated,
    )
}

@Composable
fun SetNavigationGraph(
    modifier: Modifier,
    viewModel: MainViewModel,
    navController: NavHostController,
    isAuthenticated: Boolean,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        backStackEntry
            ?.destination
            ?.parent
            ?.route
            ?.substringAfterLast(".")
    val authGraph = Destination.AuthGraph.toString()

    ObserveAsEvents(viewModel.events) { refreshToken ->
        if (refreshToken == null && currentRoute != authGraph) {
            navController.navigate(Destination.AuthGraph) {
                launchSingleTop = true
                popUpTo(Destination.NoteGraph) {
                    inclusive = true
                }
            }
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (isAuthenticated) Destination.NoteGraph else Destination.AuthGraph,
    ) {
        authGraph()
        noteGraph()
    }
}

private fun NavGraphBuilder.noteGraph() {
    navigation<Destination.NoteGraph>(
        startDestination = Destination.NoteListScreen,
    ) {
        composable<Destination.NoteListScreen> {
            NoteListScreenRoot()
        }

        composable<Destination.AddNoteScreen> {
            AddEditNoteScreenRoot()
        }

        composable<Destination.SettingsScreen> {
            SettingsScreenRoot()
        }

        composable<Destination.NoteDetailScreen> {
            NoteDetailScreenRoot()
        }
    }
}

private fun NavGraphBuilder.authGraph() {
    navigation<Destination.AuthGraph>(
        startDestination = Destination.StartScreen,
    ) {
        composable<Destination.StartScreen> {
            GettingStartedScreenRoot()
        }

        composable<Destination.RegistrationScreen> {
            RegistrationScreenRoot()
        }

        composable<Destination.LoginScreen> {
            LoginScreenRoot()
        }
    }
}
