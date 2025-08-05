package com.joshayoung.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.note.presentation.start.GettingStartedScreen
import com.joshayoung.notemark.note.presentation.add_note.AddNoteScreenRoot
import com.joshayoung.notemark.auth.presentation.log_in.LoginScreenRoot
import com.joshayoung.notemark.note.presentation.note_landing.NoteLandingScreenRoot
import com.joshayoung.notemark.auth.presentation.registration.RegistrationScreenRoot
import com.joshayoung.notemark.core.Screen
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    @Composable
    fun MyNavigation(
        navController: NavHostController,
        isAuthenticated: Boolean,
        modifier: Modifier
    ) {
        ObserveAsEvents(viewModel.authData) { refreshToken ->
            val currentDestination = navController.currentDestination
            if (
                currentDestination?.route.toString() != Screen.Start.route &&
                currentDestination?.route.toString() != Screen.Login.route &&
                currentDestination?.route.toString() != Screen.Register.route &&
                refreshToken == "unset") {
                navController.navigate(Screen.Login.route)
            }
        }
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) Screen.Landing.route else Screen.Start.route,
            modifier = modifier
        ) {
            composable(Screen.Start.route) {
                GettingStartedScreen(
                    modifier = modifier,
                    onCreateAccountClick = {
                        navController.navigate(Screen.Register.route)
                    },
                    onLoginClick = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegistrationScreenRoot(
                    modifier = modifier,
                    onRegistrationSuccess = {
                        navController.navigate(Screen.Login.route)
                    },
                    onAlreadyAccountClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Start.route)
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreenRoot(
                    modifier = modifier,
                    onLoginSuccess = {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Start.route) {
                                inclusive = true
                            }
                        }
                    },
                    onDontHaveAccount = {
                        navController.navigate(Screen.Register.route) {
                            popUpTo(Screen.Start.route)
                        }
                    }
                )
            }

            composable(Screen.Landing.route) {
                NoteLandingScreenRoot(
                    modifier = modifier,
                    onAddNoteClick = {
                        navController.navigate(Screen.AddNote.route)
                    },
                    onNavigateToEdit = { id ->
                        navController.navigate(
                            Screen.AddNote.route + "?noteId=${id}"
                        )
                    }
                )
            }

            composable(
                Screen.AddNote.route + "?noteId={noteId}",
                arguments = listOf(
                    navArgument(name = "noteId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
                ) {
                AddNoteScreenRoot(
                    modifier = modifier,
                    redirectBack = {
                        navController.navigateUp()
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.state.isCheckingSession }

        setContent {
            NoteMarkTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    // NOTE: There is a flash without this:
                    if (!viewModel.state.isCheckingSession)
                    {
                        MyNavigation(
                            modifier = Modifier.fillMaxSize().statusBarsPadding(),
                            navController = navController,
                            isAuthenticated = viewModel.state.isAuthenticated
                        )
                    }
                }
            }
        }
    }
}