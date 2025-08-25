package com.joshayoung.notemark.note.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DashedDivider(color: Color) {
    Canvas(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp),
    ) {
        val width = size.width
        val y = size.height / 2

        val dashLength = 18f
        val gapLength = 9f
        val thickness = 2f

        var x = 0f
        while (x < width) {
            drawLine(
                start = Offset(x, y),
                color = color,
                end = Offset(x + dashLength, y),
                strokeWidth = thickness,
            )
            x += dashLength + gapLength
        }
    }
}
