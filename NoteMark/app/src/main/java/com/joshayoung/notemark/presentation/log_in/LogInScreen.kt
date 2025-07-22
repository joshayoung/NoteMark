package com.joshayoung.notemark.presentation.log_in

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.presentation.components.NoteMarkButton
import com.joshayoung.notemark.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.presentation.components.TextFieldType
import com.joshayoung.notemark.ui.theme.EyeIcon
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onDontHaveAccount: () -> Unit
) {

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is LoginEvent.Success -> {
                onLoginSuccess()
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
fun LoginScreen(
    state: LoginState,
    onDontHaveAccount: () -> Unit,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Log In", modifier = Modifier,
            style = MaterialTheme.typography.headlineLarge

        )
        Text("Capture your thoughts and ideas.", modifier = Modifier
        )
        NoteMarkTextField(state = state.username, label = "Username", hint = "john.doe@example.com")
        NoteMarkTextField(
            modifier = Modifier,
            state = state.password,
            label = "Password",
            icon = EyeIcon,
            hint = "Password",
            type = TextFieldType.Password
        )
        NoteMarkButton(text = "Log In", isEnabled = true) {
            onAction(LoginAction.OnLoginClick)
        }
        Text("Don't have an account?", modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onDontHaveAccount()
            }
            , textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    NoteMarkTheme {
        LoginScreen(state = LoginState(),
            onDontHaveAccount = {},
            onAction = {})
    }
}
