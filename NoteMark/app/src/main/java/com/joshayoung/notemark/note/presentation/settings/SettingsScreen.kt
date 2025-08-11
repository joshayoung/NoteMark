package com.joshayoung.notemark.note.presentation.settings

import android.R
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.LogoutIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.core.presentation.components.NoteMarkToolbar
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent


@Composable
fun SettingsScreenRoot(
    modifier: Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreen(
        modifier = modifier,
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun SettingsScreen(
    modifier: Modifier,
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    NoteMarkScaffold(
        topAppBar = {
            NoteMarkToolbar(
                goBack = {
                    onAction(SettingsAction.NavigateBack)
                },
                title = "Settings",
                hasBackButton = true
            )
        }
    ) { innerPadding ->
    Column(
        modifier
            .padding(innerPadding)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp),
                onClick = {
                },

                ) {
                Icon(
                    imageVector = LogoutIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
            Text(
                "Log Out",
                modifier = Modifier
                    .clickable {
                        onAction(SettingsAction.OnLogoutClick)
                    },
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    NoteMarkTheme {
        SettingsScreen(
            modifier = Modifier,
            state = SettingsState(),
            onAction = {}
        )
    }
}