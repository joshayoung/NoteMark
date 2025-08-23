package com.joshayoung.notemark.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.EyeClosedIcon
import com.joshayoung.notemark.core.design.theme.EyeIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import java.nio.file.WatchEvent

@Composable
fun NoteMarkTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null,
    hint: String,
    helperText: String = "",
    label: String,
    type: TextFieldType = TextFieldType.Regular,
    inErrorState: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            modifier =
                Modifier
                    .padding(bottom = 7.dp),
            fontWeight = FontWeight.SemiBold,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            when (type) {
                TextFieldType.Regular -> {
                    RegularTextField(
                        state = state,
                        keyboardType = keyboardType,
                        helperText = helperText,
                        icon = icon,
                        hint = hint,
                        inErrorState = inErrorState
                    )
                }
                TextFieldType.Password -> {
                    PasswordTextField(
                        state = state,
                        helperText = helperText,
                        keyboardType = keyboardType,
                        hint = hint,
                        inErrorState = inErrorState
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordTextField(
    state: TextFieldState,
    keyboardType: KeyboardType,
    hint: String,
    helperText: String = "",
    inErrorState: Boolean
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    var isVisible by remember {
        mutableStateOf(false)
    }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    if (inErrorState) {
                        BorderStroke(1.dp, Color.Red)
                    } else {
                        BorderStroke(1.dp, Color.Transparent)
                    }
                )
        ) {
            BasicSecureTextField(
                state = state,
                textObfuscationMode =
                    if (isVisible) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.Hidden
                    },
                textStyle =
                    LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                decorator = { innerBox ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .padding(start = 10.dp)
                                    .weight(1f),
                        ) {
                            if (state.text.isEmpty() && !isFocused) {
                                Text(text = hint)
                            }
                            innerBox()
                        }
                        IconButton(
                            onClick = {
                                isVisible = !isVisible
                            },
                        ) {
                            Icon(
                                modifier =
                                    Modifier
                                        .padding(start = 10.dp),
                                imageVector =
                                    if (isVisible) {
                                        EyeClosedIcon
                                    } else {
                                        EyeIcon
                                    },
                                contentDescription = null,
                            )
                        }
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = keyboardType,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocused = it.isFocused
                        }.clip(RoundedCornerShape(4.dp))
                        .background(if (!isFocused) MaterialTheme.colorScheme.surface else Color.White)
                        .border(2.dp, if (isFocused) Color.Blue else Color.Transparent),
            )
        }
        if (helperText != "") {
            AnimatedVisibility(
                visible = isFocused,
            ) {
                Text(text = helperText)
            }
        }
    }
}

@Composable
private fun RegularTextField(
    state: TextFieldState,
    keyboardType: KeyboardType,
    icon: ImageVector? = null,
    helperText: String = "",
    hint: String,
    inErrorState: Boolean
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    if (inErrorState) {
                        BorderStroke(1.dp, Color.Red)
                    } else {
                        BorderStroke(1.dp, Color.Transparent)
                    }
                )
        ) {
            BasicTextField(
                state = state,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.bodyLarge,
                cursorBrush =
                    if (inErrorState) {
                        SolidColor(MaterialTheme.colorScheme.error)
                    } else {
                        SolidColor(MaterialTheme.colorScheme.primary)
                    },
                decorator = { innerBox ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f),
                        ) {
                            if (state.text.isEmpty() && !isFocused) {
                                Text(text = hint)
                            }
                            innerBox()
                        }
                        if (icon != null) {
                            Icon(
                                modifier =
                                    Modifier
                                        .padding(start = 10.dp),
                                imageVector = icon,
                                contentDescription = null,
                            )
                        }
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = keyboardType,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocused = it.isFocused
                        }.clip(RoundedCornerShape(4.dp))
                        .background(if (!isFocused) MaterialTheme.colorScheme.surface else Color.White)
                        .border(2.dp, if (isFocused) Color.Blue else Color.Transparent)
                        .padding(12.dp),
            )
        }
        if (helperText != "") {
            AnimatedVisibility(
                visible = isFocused,
            ) {
                Text(text = helperText)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteMarkTextFieldPreview() {
    NoteMarkTheme {
        Column {
            NoteMarkTextField(
                modifier = Modifier,
                state = TextFieldState(),
                label = "Username",
                helperText = "Enter a valid username",
                hint = "Enter Username",
            )
            NoteMarkTextField(
                modifier = Modifier,
                state = TextFieldState(),
                label = "Username Error",
                helperText = "Enter a valid username",
                hint = "Enter Username",
                inErrorState = true
            )
            NoteMarkTextField(
                modifier = Modifier,
                state = TextFieldState(),
                label = "Password",
                helperText = "Enter a valid password",
                hint = "Enter Password",
                type = TextFieldType.Password,
            )
        }
    }
}