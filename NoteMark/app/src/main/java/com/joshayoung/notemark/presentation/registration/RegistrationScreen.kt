package com.joshayoung.notemark.presentation.registration

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshayoung.notemark.R
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.presentation.components.NoteMarkButton
import com.joshayoung.notemark.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.presentation.components.TextFieldType
import com.joshayoung.notemark.presentation.registration.components.DisplayErrorList
import com.joshayoung.notemark.ui.theme.EyeIcon
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent

@Composable
fun RegistrationScreenRoot(
    onRegistrationSuccess: () -> Unit,
    onAlreadyAccountClick: () -> Unit,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var errorMessage by remember { mutableStateOf<List<String?>?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is RegistrationEvent.RegistrationSuccess -> {
                onRegistrationSuccess()
            }
            is RegistrationEvent.Error -> {
                keyboardController?.hide()
                val errors = event.error?.errors
                val reason = event.error?.reason

                errorMessage = (errors ?: emptyList()) + reason
            }
        }
    }

    RegistrationScreen(
        state = viewModel.state,
        onAlreadyAccountClick = onAlreadyAccountClick,
        errorMessage = errorMessage,
        onnAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    onAlreadyAccountClick: () -> Unit,
    errorMessage: List<String?>?,
    onnAction: (RegistrationAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    if (!isPortrait) {
        Row(modifier = Modifier
            .fillMaxSize()
        ) {
            Header(modifier = Modifier.weight(1f))
            Form(
                errorMessage,
                onAlreadyAccountClick = onAlreadyAccountClick,
                state,
                onnAction,
                        modifier = Modifier.weight(1f),
            )
        }
    } else {
        Column {
            Header()
            Form(errorMessage,
                onAlreadyAccountClick = onAlreadyAccountClick,
                state, onnAction)
        }
    }
}

@Composable
private fun Form(
    errorMessage: List<String?>?,
    onAlreadyAccountClick: () -> Unit,
    state: RegistrationState,
    onnAction: (RegistrationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DisplayErrorList(
            modifier = Modifier
                .weight(1f),
            errorMessage
        )

        NoteMarkTextField(
            state = state.username,
            label = "Username",
            hint = "John.doe",
            helperText = "Use between 3 and 20 characters for your username",
        )
        if (state.usernameError != "") {
            Text(text = state.usernameError, color = MaterialTheme.colorScheme.error)
        }
        NoteMarkTextField(state = state.email, label = "Email", hint = "john.doe@example.com")
        if (state.emailError != "") {
            Text(text = state.emailError, color = MaterialTheme.colorScheme.error)
        }
        NoteMarkTextField(
            state = state.password,
            label = "Password",
            icon = EyeIcon,
            hint = "Password",
            helperText = "Use 8+ characters with a number or symbol for better security",
            type = TextFieldType.Password
        )
        if (state.passwordError != "") {
            Text(text = state.passwordError, color = MaterialTheme.colorScheme.error)
        }
        NoteMarkTextField(
            state = state.repeatedPassword,
            label = "Repeat Password",
            icon = EyeIcon,
            hint = "Repeat Password",
            type = TextFieldType.Password
        )
        if (state.passwordEqualityError != "") {
            Text(text = state.passwordEqualityError, color = MaterialTheme.colorScheme.error)
        }
        NoteMarkButton(
            text = "Create Account",
            isEnabled = !state.isRegistering && state.canRegister
        ) {
            onnAction(RegistrationAction.OnRegisterClick)
        }
        Text(
            stringResource(R.string.have_account), modifier = Modifier
                .clickable {
                    onAlreadyAccountClick()
                }
                .fillMaxWidth(), textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            "Create Account", modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center

        )
        Text(
            "Capture your thoughts and ideas.",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center

        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    NoteMarkTheme {
        RegistrationScreen(
            state = RegistrationState(),
            onAlreadyAccountClick = {},
            errorMessage = null,
            onnAction = {}
        )
    }
}