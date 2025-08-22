package com.joshayoung.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.state.isCheckingSession }
        enableEdgeToEdge(
            //statusBarStyle = SystemBarStyle.dark(0)
        )

        setContent {
            NoteMarkTheme {
                val navController = rememberNavController()
                val navigator = koinInject<Navigator>()

                Surface(modifier = Modifier.fillMaxSize()) {
                    // NOTE: There is a flash without this:
                    if (!viewModel.state.isCheckingSession)
                    {
                        NoteMarkNavigation(
                            navigator = navigator,
                            viewModel = viewModel,
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