package com.joshayoung.notemark.presentation.create_account

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.ui.theme.NoteMarkTheme

@Composable
fun CreateAccountScreen() {
    Text("Create Account")
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    NoteMarkTheme {
        CreateAccountScreen()
    }
}