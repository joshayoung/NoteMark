package com.joshayoung.notemark.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import com.joshayoung.notemark.ui.theme.PlusIcon
import org.koin.androidx.compose.koinViewModel


@Composable
fun NoteMarkScaffold(
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NoteMark", modifier = Modifier,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(size = 10.dp))
                    .background(brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "pl".uppercase(), modifier = Modifier,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
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
            floatingActionButton = {
                Text("FAB")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("my content")
            }
        }
    }
}