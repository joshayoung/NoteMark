package com.joshayoung.notemark.core.presentation.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

@Composable
fun NoteMarkScaffold(
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview
@Composable
fun NoteMarkScaffoldPreview() {
    NoteMarkTheme {
        NoteMarkScaffold(
            topAppBar = {
                NoteMarkToolbar(
                    title = "Home",
                    navigateToSettings = {}
                )
            },
            floatingActionButton = {
                Text("FAB")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                ,verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("my content")
            }
        }
    }
}