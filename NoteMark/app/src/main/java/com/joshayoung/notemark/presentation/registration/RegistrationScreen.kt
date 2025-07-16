package com.joshayoung.notemark.presentation.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

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
    Column() {
        BasicTextField(
            state = state.email
        )
        Text("Create Account")
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    NoteMarkTheme {
        RegistrationScreen(state = RegistrationState())
    }
}