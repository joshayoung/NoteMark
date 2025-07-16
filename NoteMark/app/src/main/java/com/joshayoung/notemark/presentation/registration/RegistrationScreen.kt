package com.joshayoung.notemark.presentation.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.presentation.components.NoteMarkButton
import com.joshayoung.notemark.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.presentation.components.TextFieldType
import com.joshayoung.notemark.ui.theme.EyeIcon
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent

@Composable
fun RegistrationScreenRoot(
    viewModel: RegistrationViewModel = koinViewModel()
) {
    RegistrationScreen(
        state = viewModel.state
    )
}

@Composable
fun RegistrationScreen(
    state: RegistrationState
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
        NoteMarkTextField(state = state.username, label = "Username", hint = "John.doe")
        NoteMarkTextField(state = state.email, label = "Email", hint = "john.doe@example.com")
        NoteMarkTextField(state = state.password, label = "Password", icon = EyeIcon, hint = "Password", type = TextFieldType.Password)
        NoteMarkTextField(state = state.repeatedPassword, label = "Repeat Password", icon = EyeIcon, hint = "Password", type = TextFieldType.Password)
        Text("Use 8+ characters with a number or symbol for better security",
            modifier = Modifier,
            style = MaterialTheme.typography.bodySmall

            )
        NoteMarkButton(text = "Create Account", isEnabled = false) {

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
            state = RegistrationState()
        )
    }
}