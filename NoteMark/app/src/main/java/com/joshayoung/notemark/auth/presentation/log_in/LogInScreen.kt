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
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.core.presentation.components.NoteMarkButton
import com.joshayoung.notemark.core.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.core.presentation.components.TextFieldType
import com.joshayoung.notemark.core.design.theme.EyeIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onDontHaveAccount: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is LoginEvent.Success -> {
                onLoginSuccess()
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

    LoginScreen(
        state = viewModel.state,
        onDontHaveAccount = onDontHaveAccount,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
    ) {
        Text("Log In", modifier = Modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        Text("Capture your thoughts and ideas.", modifier = Modifier
        )
    }
}

@Composable
fun LoginScreen(
    state: LoginState,
    onDontHaveAccount: () -> Unit,
    onAction: (LoginAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
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
                    onAction = onAction,
                    onDontHaveAccount = onDontHaveAccount
                )
            }
        }
    } else {
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
                onDontHaveAccount = onDontHaveAccount,
                modifier = Modifier
                    .size(400.dp)
                    .padding(20.dp)
            )
        }
    }
    }
}

@Composable
fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onDontHaveAccount: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Column(
        modifier = modifier
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
            onDontHaveAccount()
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
        LoginScreen(state = LoginState(),
            onDontHaveAccount = {},
            onAction = {})
    }
}
