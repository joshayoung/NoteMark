package com.joshayoung.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.joshayoung.notemark.auth.presentation.log_in.LoginScreenRoot
import com.joshayoung.notemark.auth.presentation.registration.RegistrationScreenRoot
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.NavigationAction
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.note.presentation.add_note.AddNoteScreenRoot
import com.joshayoung.notemark.note.presentation.note_detail.NoteDetailScreenRoot
import com.joshayoung.notemark.note.presentation.note_list.NoteListScreenRoot
import com.joshayoung.notemark.note.presentation.settings.SettingsScreenRoot
import com.joshayoung.notemark.note.presentation.start.GettingStartedScreenRoot
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    @Composable
    fun MyNavigation(
        modifier: Modifier,
        navigator: Navigator,
        navController: NavHostController,
        isAuthenticated: Boolean
    ) {
        ObserveAsEvents(viewModel.authData) { refreshToken ->
            if (refreshToken == "unset" && AuthService.notInAuthRoutes(navController.currentDestination?.route)) {
                navController.navigate(Destination.StartScreen)
            }
        }

        ObserveAsEvents(flow = navigator.navigationAction) { action ->
            when(action) {
                is NavigationAction.Navigate -> navController.navigate(
                    action.destination
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

        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = if (isAuthenticated) Destination.NoteGraph  else Destination.AuthGraph
        ) {
            authGraph()
            noteGraph()
        }
    }

    private fun NavGraphBuilder.noteGraph() {
        navigation<Destination.NoteGraph>(
            startDestination = Destination.NoteList
        ) {
            composable<Destination.NoteList> {
                NoteListScreenRoot()
            }

            composable<Destination.AddNote> {
                AddNoteScreenRoot()
            }

            composable<Destination.Settings> {
                SettingsScreenRoot()
            }

            composable<Destination.NoteDetail>() {
                NoteDetailScreenRoot()
            }
        }
    }

    private fun NavGraphBuilder.authGraph() {
        navigation<Destination.AuthGraph>(
            startDestination = Destination.StartScreen
        ) {
            composable<Destination.StartScreen> {
                GettingStartedScreenRoot()
            }

            composable<Destination.Registration> {
                RegistrationScreenRoot()
            }

            composable<Destination.Login> {
                LoginScreenRoot()
            }
        }
    }

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.state.isCheckingSession }
        enableEdgeToEdge()

        setContent {
            NoteMarkTheme {
                val navController = rememberNavController()
                val navigator = koinInject<Navigator>()
                Surface(modifier = Modifier.fillMaxSize()) {
                    // NOTE: There is a flash without this:
                    if (!viewModel.state.isCheckingSession)
                    {
                        MyNavigation(
                            navigator = navigator,
                            modifier = Modifier.fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing),
                            navController = navController,
                            isAuthenticated = viewModel.state.isAuthenticated
                        )
                    }
                }
            }
        }
    }
}