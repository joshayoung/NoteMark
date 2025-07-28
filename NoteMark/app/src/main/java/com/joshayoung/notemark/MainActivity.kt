package com.joshayoung.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joshayoung.notemark.presentation.GettingStartedScreen
import com.joshayoung.notemark.presentation.add_note.AddNoteScreenRoot
import com.joshayoung.notemark.presentation.registration.RegistrationScreenRoot
import com.joshayoung.notemark.presentation.log_in.LoginScreenRoot
import com.joshayoung.notemark.presentation.note_landing.NoteLandingScreenRoot
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    @Composable
    fun MyNavigation(
        navController: NavHostController,
        isAuthenticated: Boolean,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) Screen.Landing.route else Screen.Start.route,
            modifier = modifier
        ) {
            composable(Screen.Start.route) {
                GettingStartedScreen(
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
                    onLogoutClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Start.route) {
                                inclusive = true
                            }
                        }
                    },
                    onAddNoteClick = {
                        navController.navigate(Screen.AddNote.route)
                    },
                    redirectToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screen.AddNote.route) {
                AddNoteScreenRoot()
            }
        }
    }

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
                            navController = navController,
                            isAuthenticated = viewModel.state.isAuthenticated
                        )
                    }
                }
            }
        }
    }
}