@file:OptIn(ExperimentalMaterial3Api::class)

package com.joshayoung.notemark.note.presentation.settings

import android.R
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.LogoutIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.design.theme.RightArrowIcon
import com.joshayoung.notemark.core.design.theme.TimeIcon
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.core.presentation.components.NoteMarkToolbar
import com.joshayoung.notemark.note.domain.models.SyncInterval
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent


@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun SettingsScreen(
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
        Modifier
            .padding(innerPadding)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconButton(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp),
                onClick = {
                },

                ) {
                Icon(
                    imageVector = TimeIcon,
                    contentDescription = null,
                )
            }
                Text(text = "Sync Interval")
            }

            Row(modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var expanded by remember { mutableStateOf(false) }

                Text(text = state.interval.text, modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    }
                )
                Box(
                    modifier = Modifier
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(RightArrowIcon, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(SyncInterval.MANUAL.text) },
                            onClick = {
                                expanded = false
                                onAction(SettingsAction.SetSyncInterval(SyncInterval.MANUAL))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(SyncInterval.FIFTEEN.text) },
                            onClick = {
                                expanded = false
                                onAction(SettingsAction.SetSyncInterval(SyncInterval.FIFTEEN))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(SyncInterval.THIRTY.text) },
                            onClick = {
                                expanded = false
                                onAction(SettingsAction.SetSyncInterval(SyncInterval.THIRTY))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(SyncInterval.HOUR.text) },
                            onClick = {
                                expanded = false
                                onAction(SettingsAction.SetSyncInterval(SyncInterval.HOUR))
                            }
                        )
                    }
                }
            }

        }

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
            state = SettingsState(),
            onAction = {}
        )
    }
}