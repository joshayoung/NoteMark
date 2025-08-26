package com.joshayoung.notemark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
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

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .windowInsetsPadding(WindowInsets.Companion.safeDrawing),
                )
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