package com.joshayoung.notemark.core.design.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.joshayoung.notemark.R

val CopyIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.copy)

val EyeIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.eye)

val EyeClosedIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.eye_off)

val PlusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.plus)
