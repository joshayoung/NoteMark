package com.joshayoung.notemark.presentation.registration.components

import android.icu.number.Scale.none
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DisplayErrorList(
    modifier: Modifier,
    errorMessage: List<String?>?) {
    LazyColumn(modifier = modifier) {
        errorMessage?.let { nonNullItems ->
            items(nonNullItems) { item ->
                if (item != null)
                {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .background(color = MaterialTheme.colorScheme.error)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DisplayErrorListPreview() {
    DisplayErrorList(
        modifier = Modifier,
        errorMessage = listOf(
        "The email format was invalid",
        null,
        "The password was too long. Please make it shorter and try again.",
        "The verification password did not match the password.")
    )
}
