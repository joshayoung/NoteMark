package com.joshayoung.notemark.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme

@Composable
fun NoteMarkButton(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            disabledContentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12f),
        modifier =
        modifier
            .fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp),
//                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = if(isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
        } else {
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteMarkButtonPreview() {
    NoteMarkTheme {
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center) {
            NoteMarkButton(
                text = "Create Account",
                onClick = {},
                isEnabled = true
            )
            NoteMarkButton(
                text = "Login In",
                onClick = {}
            )
            NoteMarkButton(
                text = "Login In",
                isEnabled = true,
                isLoading = true,
                onClick = {}
            )
            NoteMarkButton(
                text = "Disabled and Loading",
                isEnabled = false,
                isLoading = true,
                onClick = {}
            )
        }
    }
}