package com.joshayoung.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joshayoung.notemark.presentation.GettingStartedScreen
import com.joshayoung.notemark.presentation.registration.RegistrationScreenRoot
import com.joshayoung.notemark.presentation.log_in.LoginScreen
import com.joshayoung.notemark.presentation.log_in.LoginScreenRoot
import com.joshayoung.notemark.presentation.note_landing.NoteLandingScreenRoot
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplash = true

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplash }

        lifecycleScope.launch {
            delay(3000) // Wait 3 seconds
            keepSplash = false // Allow splash screen to be dismissed
        }

        setContent {
            NoteMarkTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "getting_started",
                        Modifier.padding(innerPadding)
                    ) {
                        composable("getting_started") {
                            GettingStartedScreen(
                                onCreateAccountClick = {
                                    navController.navigate("create_account")
                                },
                                onLoginClick = {
                                    navController.navigate("login")
                                }
                            )
                        }

                        composable("create_account") {
                            RegistrationScreenRoot(
                                onRegistrationSuccess = {
                                    navController.navigate("login")
                                }
                            )
                        }

                        composable("login") {
                            LoginScreenRoot(
                                onLoginSuccess = {
                                    navController.navigate("note_landing")
                                }
                            )
                        }

                        composable("note_landing") {
                            NoteLandingScreenRoot()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteMarkTheme { }
}