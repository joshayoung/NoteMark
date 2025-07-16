package com.joshayoung.notemark.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.joshayoung.notemark.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.ui.theme.EyeClosedIcon
import com.joshayoung.notemark.ui.theme.EyeIcon
import com.joshayoung.notemark.ui.theme.NoteMarkTheme

@Composable
fun NoteMarkTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null,
    hint: String,
    label: String,
    type: TextFieldType = TextFieldType.Regular
) {
    Column(modifier = modifier) {
        Text(text = label,
            modifier = Modifier
            .padding(bottom = 7.dp),
            fontWeight = FontWeight.SemiBold
            )
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            when(type) {
                TextFieldType.Regular -> {
                    RegularTextField(
                        state = state,
                        keyboardType = keyboardType,
                        icon = icon,
                        hint = hint
                    )
                }
                TextFieldType.Password ->  {
                    PasswordTextField(
                        state = state,
                        keyboardType = keyboardType,
                        hint = hint
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
    hint: String
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    var isVisible by remember {
        mutableStateOf(false)
    }

    BasicSecureTextField(
        state = state,
        textObfuscationMode = if(isVisible) {
            TextObfuscationMode.Visible
        } else TextObfuscationMode.Hidden,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        decorator = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    if (state.text.isEmpty() && !isFocused)
                    {
                        Text(text = hint, modifier = Modifier
                        )
                    }
                    innerBox()
                }
                IconButton(
                    onClick = {
                        isVisible = !isVisible
                    }) {
                    Icon(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    imageVector = if (isVisible) {
                        EyeClosedIcon
                    } else {
                        EyeIcon
                    },
                    contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colorScheme.surface)
    )
}

@Composable
private fun RegularTextField(
    state: TextFieldState,
    keyboardType: KeyboardType,
    icon: ImageVector? = null,
    hint: String
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        decorator = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (state.text.isEmpty() && !isFocused)
                    {
                        Text(text = hint)
                    }
                    innerBox()
                }
                if (icon != null) {
                    Icon(modifier = Modifier
                        .padding(start = 10.dp),
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    )
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
                hint = "Enter Username"
            )
            NoteMarkTextField(
                modifier = Modifier,
                state = TextFieldState(),
                label = "Password",
                hint = "Enter Password",
                type = TextFieldType.Password
            )
        }
    }
}