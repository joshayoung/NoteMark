package com.joshayoung.notemark.auth.presentation.log_in

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.core.presentation.components.NoteMarkButton
import com.joshayoung.notemark.core.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.core.presentation.components.TextFieldType
import com.joshayoung.notemark.core.design.theme.EyeIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is LoginEvent.Success -> {
                viewModel.onAction(LoginAction.LoginSuccess)
            }

            LoginEvent.Failure -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    "Invalid Login Credentials",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when(deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            LoginScreenPortrait(
                state = viewModel.state,
                onAction = { action ->
                    viewModel.onAction(action)
                }
            )
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            LoginScreenLandscape(
                state = viewModel.state,
                onAction = { action ->
                    viewModel.onAction(action)
                }
            )
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {

        }
    }
}

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
    ) {
        Text("Log In", modifier = Modifier,
            style = MaterialTheme.typography.titleLarge
        )
        Text("Capture your thoughts and ideas.", modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LoginScreenPortrait(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(top =10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            LoginHeader(
                modifier = Modifier
                    .padding(top = 40.dp)
            )
            LoginContent(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
fun LoginScreenLandscape(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Row(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LoginHeader(
                modifier = Modifier
                    .padding(40.dp)
            )
            LoginContent(
                state = state,
                onAction = onAction,
                modifier = Modifier
                    .size(400.dp)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
    NoteMarkTextField(state = state.username, label = "Username", hint = "john.doe@example.com")
    NoteMarkTextField(
        modifier = Modifier,
        state = state.password,
        label = "Password",
        icon = EyeIcon,
        hint = "Password",
        type = TextFieldType.Password
    )
    NoteMarkButton(text = "Log In", isEnabled = state.formFilled,
        isLoading = state.isLoggingIn
    ) {
        onAction(LoginAction.OnLoginClick)
    }
    Text(
        "Don't have an account?", modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onAction(LoginAction.DontHaveAccount)
        }, textAlign = TextAlign.Center
    )
}
}

@Composable
@Preview(
    showBackground = true,
    widthDp = 840, heightDp = 360)
@Preview(showBackground = true)
fun LoginScreenPreview() {
    NoteMarkTheme {
        LoginScreenLandscape(state = LoginState(),
            onAction = {}
        )
        LoginScreenPortrait(state = LoginState(),
            onAction = {}
        )
    }
}
