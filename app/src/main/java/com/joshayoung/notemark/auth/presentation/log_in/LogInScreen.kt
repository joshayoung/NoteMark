package com.joshayoung.notemark.auth.presentation.log_in

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.EyeIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.core.presentation.components.NoteMarkButton
import com.joshayoung.notemark.core.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.core.presentation.components.TextFieldType
import com.joshayoung.notemark.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(viewModel: LoginViewModel = koinViewModel()) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.Success -> {
                Toast
                    .makeText(
                        context,
                        "You are Logged In!",
                        Toast.LENGTH_LONG,
                    ).show()
                viewModel.onAction(LoginAction.LoginSuccess)
            }

            LoginEvent.Failure -> {
                keyboardController?.hide()
                Toast
                    .makeText(
                        context,
                        "Invalid Login Credentials",
                        Toast.LENGTH_LONG,
                    ).show()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val loginModifier =
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            Column(
                modifier =
                    Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(top = 40.dp),
            ) {
                Column(
                    modifier =
                        loginModifier
                            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                ) {
                    LoginHeader(
                        modifier =
                            Modifier
                                .padding(top = 40.dp),
                    )
                    LoginContent(
                        state = state,
                        onAction = { action ->
                            onAction(action)
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                    )
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier =
                    Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(top = 20.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(Color.White)
                            .fillMaxSize()
                            .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    LoginHeader(
                        modifier =
                            Modifier
                                .weight(1f),
                    )
                    LoginContent(
                        state = state,
                        onAction = { action ->
                            onAction(action)
                        },
                        modifier =
                            Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                    )
                }
            }
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP,
        -> {
            Column(
                modifier =
                    Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(top = 20.dp),
            ) {
                Column(
                    modifier =
                        loginModifier
                            .verticalScroll(rememberScrollState())
                            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                            .padding(vertical = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LoginHeader(
                        alignment = Alignment.CenterHorizontally,
                        modifier =
                            Modifier
                                .widthIn(max = 540.dp),
                    )
                    LoginContent(
                        modifier =
                            Modifier
                                .widthIn(max = 540.dp),
                        state = state,
                        onAction = { action ->
                            onAction(action)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment,
    ) {
        Text(
            "Log In",
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            "Capture your thoughts and ideas.",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        NoteMarkTextField(state = state.username, label = "Username", hint = "john.doe@example.com")
        NoteMarkTextField(
            modifier = Modifier,
            state = state.password,
            label = "Password",
            icon = EyeIcon,
            hint = "Password",
            type = TextFieldType.Password,
        )
        NoteMarkButton(
            text = "Log In",
            isEnabled = state.formFilled,
            isLoading = state.isLoggingIn,
        ) {
            onAction(LoginAction.OnLoginClick)
        }
        Text(
            "Don't have an account?",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clickable {
                        onAction(LoginAction.DontHaveAccount)
                    },
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(
    showBackground = true,
    widthDp = 840,
    heightDp = 360,
)
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280,
)
fun LoginScreenPreview() {
    NoteMarkTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
        )
    }
}
