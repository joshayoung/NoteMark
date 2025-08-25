package com.joshayoung.notemark.note.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.BackIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.design.theme.OfflineIcon
import com.joshayoung.notemark.core.design.theme.SettingsIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteMarkToolbar(
    title: String,
    goBack: () -> Unit = {},
    isOffline: Boolean = false,
    hasBackButton: Boolean = false,
    hasActions: Boolean = false,
    userAbbreviation: String? = null,
    navigateToSettings: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                if (isOffline) {
                    Icon(
                        imageVector = OfflineIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier =
                            Modifier
                                .padding(start = 10.dp)
                                .size(20.dp),
                    )
                }
            }
        },
        navigationIcon = {
            if (hasBackButton) {
                IconButton(onClick = goBack) {
                    Icon(
                        imageVector = BackIcon,
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(20.dp),
                    )
                }
            }
        },
        actions = {
            if (hasActions) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .padding(end = 4.dp),
                    ) {
                        IconButton(
                            onClick = navigateToSettings,
                            modifier =
                                Modifier
                                    .size(26.dp),
                        ) {
                            Icon(
                                imageVector = SettingsIcon,
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .size(26.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    Box(
                        modifier =
                            Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(size = 10.dp))
                                .background(color = MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = userAbbreviation ?: "NM",
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun NoteMarkToolbarPreview() {
    NoteMarkTheme {
        NoteMarkToolbar(
            title = "Home",
            goBack = {},
            isOffline = true,
            hasActions = true,
            navigateToSettings = {},
            userAbbreviation = "NM",
        )
    }
}