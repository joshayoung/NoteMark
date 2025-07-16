package com.joshayoung.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joshayoung.notemark.presentation.GettingStartedScreen
import com.joshayoung.notemark.presentation.registration.RegistrationScreenRoot
import com.joshayoung.notemark.presentation.log_in.LoginScreen
import com.joshayoung.notemark.ui.theme.NoteMarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                                }
                            )
                        }

                        composable("create_account") {
                            RegistrationScreenRoot()
                        }

                        composable("login") {
                            LoginScreen()
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
    NoteMarkTheme {
    }
}