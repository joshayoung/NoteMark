package com.joshayoung.notemark.core.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
//    background = Color(0xFFFFFBFE),
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFEFEFF2),
    onSurface = Color(0xFF1B1B1C),
    surfaceVariant = Color(0xFF535364),
    tertiary =  Color(0x1B1B1C1F),
    onTertiary =  Color(0xFF1B1B1C).copy(alpha = 0.5f),
    primary = Color(0xFF5977F7),
    onPrimary = Color(0xFFFFFFFF),
    error = Color(0xFFE1294B)
)

@Composable
fun NoteMarkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}