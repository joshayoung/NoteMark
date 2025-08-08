package com.joshayoung.notemark.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.BackIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteMarkToolbar(
    title: String,
    goBack: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = title
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(
                    imageVector = BackIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    )

}

@Preview
@Composable
fun NoteMarkToolbarPreview() {
    NoteMarkTheme {
        NoteMarkToolbar(
            title = "Home",
            goBack = {}
        )
    }
}