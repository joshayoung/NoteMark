package com.joshayoung.notemark.note.presentation.start

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.notemark.R
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.components.NoteMarkButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun GettingStartedScreenRoot(viewModel: GettingStartedViewModel = koinViewModel()) {
    GettingStartedScreen(
        onAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@Composable
fun GettingStartedScreen(onAction: (StartAction) -> Unit) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    if (isPortrait) {
        PortraitOrientation(
            onAction = onAction,
        )
    } else {
        LandscapeOrientation(
            onAction = onAction,
        )
    }
}

@Composable
fun LandscapeOrientation(onAction: (StartAction) -> Unit) {
    Row(
        modifier =
            Modifier
                .background(Color(0xFFE0EAFF))
                .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painterResource(id = R.drawable.greeting),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxHeight(),
        )
        NoteIntroCard(
            onAction = onAction,
            modifier =
                Modifier
                    .width(400.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
        )
    }
}

@Composable
private fun PortraitOrientation(onAction: (StartAction) -> Unit) {
    Column(
        modifier =
            Modifier
                .background(Color(0xFFE0EAFF)),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f),
        ) {
            Image(
                painterResource(id = R.drawable.greeting),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
        }
        NoteIntroCard(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            onAction = onAction,
        )
    }
}

@Composable
private fun NoteIntroCard(
    modifier: Modifier = Modifier,
    onAction: (StartAction) -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Column(
            modifier =
                modifier
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier,
                text = "Your Own Collection of Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier =
                    Modifier
                        .padding(bottom = 20.dp),
                style = MaterialTheme.typography.bodySmall,
                text = "Capture your thoughts and ideas.",
            )
            NoteMarkButton(
                text = "Get Started",
                onClick = {
                    onAction(StartAction.CreateAccount)
                },
                isEnabled = true,
            )
            OutlinedButton(
                shape = RoundedCornerShape(12f),
                modifier =
                    Modifier
                        .fillMaxWidth(),
                onClick = {
                    onAction(StartAction.Login)
                },
            ) {
                Text("Log in")
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    widthDp = 840,
    heightDp = 360,
)
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280,
)
@Composable
private fun GettingStartedScreenPreview() {
    NoteMarkTheme {
        GettingStartedScreen(
            onAction = {},
        )
    }
}