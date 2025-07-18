package com.joshayoung.notemark.presentation.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        errorMessage = errorMessage,
        onnAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    errorMessage: List<String?>?,
    onnAction: (RegistrationAction) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Create Account", modifier = Modifier
            .fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center

        )
        Text("Capture your thoughts and ideas.",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center

            )

        DisplayErrorList(errorMessage)

        NoteMarkTextField(state = state.username, label = "Username", hint = "John.doe")
        Text("Use between 3 and 20 characters for your username",
            modifier = Modifier,
            style = MaterialTheme.typography.bodySmall
        )
        NoteMarkTextField(state = state.email, label = "Email", hint = "john.doe@example.com")
        NoteMarkTextField(state = state.password, label = "Password", icon = EyeIcon, hint = "Password", type = TextFieldType.Password)
        NoteMarkTextField(state = state.repeatedPassword, label = "Repeat Password", icon = EyeIcon, hint = "Password", type = TextFieldType.Password)
        Text("Use 8+ characters with a number or symbol for better security",
            modifier = Modifier,
            style = MaterialTheme.typography.bodySmall
        )
        NoteMarkButton(text = "Create Account",
            isEnabled = !state.isRegistering && state.canRegister
        ) {
            onnAction(RegistrationAction.OnRegisterClick)
        }
        Text("Already have an account?", modifier = Modifier
            .fillMaxWidth()
            , textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    NoteMarkTheme {
        RegistrationScreen(
            state = RegistrationState(),
            errorMessage = null,
            onnAction = {}
        )
    }
}