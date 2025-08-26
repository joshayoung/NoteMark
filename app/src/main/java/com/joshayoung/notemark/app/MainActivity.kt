package com.joshayoung.notemark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.navigation.Navigator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.state.isCheckingSession }
        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.dark(0)
        )

        setContent {
            NoteMarkTheme {
                val navController = rememberNavController()
                val navigator = koinInject<Navigator>()

                if (!viewModel.state.isCheckingSession) {
                    NavigationRoot(
                        navigator = navigator,
                        viewModel = viewModel,
                        navController = navController,
                        isAuthenticated = viewModel.state.isAuthenticated,
                    )
                }
            }
        }
    }
}