package com.joshayoung.notemark.presentation.log_in

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.ui.theme.NoteMarkTheme

@Composable
fun LoginScreen() {
    Column {
        Text("Test")
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    NoteMarkTheme {
        LoginScreen()
    }
}
