package com.joshayoung.notemark.auth.presentation.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.R
import com.joshayoung.notemark.auth.presentation.registration.components.DisplayErrorList
import com.joshayoung.notemark.core.design.theme.EyeIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.ObserveAsEvents
import com.joshayoung.notemark.core.presentation.components.NoteMarkButton
import com.joshayoung.notemark.core.presentation.components.NoteMarkTextField
import com.joshayoung.notemark.core.presentation.components.TextFieldType
import com.joshayoung.notemark.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationScreenRoot(viewModel: RegistrationViewModel = koinViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var errorMessage by remember { mutableStateOf<List<String?>?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegistrationEvent.RegistrationSuccess -> {
                viewModel.onAction(RegistrationAction.RegisterSuccess)
            }
            is RegistrationEvent.Error -> {
                keyboardController?.hide()
                val errors = event.error

                // TODO: Fix this:
//                errorMessage = errors.
            }
        }
    }

    RegistrationScreen(
        state = viewModel.state,
        errorMessage = errorMessage,
        onnAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    errorMessage: List<String?>?,
    onnAction: (RegistrationAction) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(top = 60.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(horizontal = 20.dp),
            ) {
                Header()
                Form(
                    errorMessage,
                    state,
                    onnAction,
                )
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier =
                    Modifier
                        .background(color = MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier =
                        Modifier
                            .padding(top = 30.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                            .fillMaxSize(),
                ) {
                    Header(modifier = Modifier.weight(1f))
                    Form(
                        errorMessage,
                        state,
                        onnAction,
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(bottom = 30.dp, end = 10.dp),
                    )
                }
            }
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP,
        -> {
            Column(
                modifier =
                    Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(top = 30.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Header(
                        modifier =
                            Modifier
                                .widthIn(max = 540.dp),
                    )
                    Form(
                        errorMessage,
                        state,
                        onnAction,
                        modifier =
                            Modifier
                                .widthIn(max = 540.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Form(
    errorMessage: List<String?>?,
    state: RegistrationState,
    onnAction: (RegistrationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(top = 20.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DisplayErrorList(
            modifier =
                Modifier
                    .weight(1f),
            errorMessage,
        )

        NoteMarkTextField(
            state = state.username,
            label = "Username",
            hint = "John.doe",
            helperText = "Use between 3 and 20 characters for your username",
            inErrorState = state.usernameError != null,
        )
        if (state.usernameError != null) {
            Text(text = state.usernameError.asString(), color = MaterialTheme.colorScheme.error)
        }
        NoteMarkTextField(state = state.email, label = "Email", hint = "john.doe@example.com")
        if (state.emailError != null) {
            Text(text = state.emailError.asString(), color = MaterialTheme.colorScheme.error)
        }
        NoteMarkTextField(
            state = state.password,
            label = "Password",
            icon = EyeIcon,
            hint = "Password",
            helperText = "Use 8+ characters with a number or symbol for better security",
            type = TextFieldType.Password,
            inErrorState = state.passwordError != null,
        )
        if (state.passwordError != null) {
            Text(text = state.passwordError.asString(), color = MaterialTheme.colorScheme.error)
        }
        NoteMarkTextField(
            state = state.repeatedPassword,
            label = "Repeat Password",
            icon = EyeIcon,
            hint = "Repeat Password",
            type = TextFieldType.Password,
            inErrorState = state.passwordEqualityError != null,
        )
        if (state.passwordEqualityError != null) {
            Text(text = state.passwordEqualityError.asString(), color = MaterialTheme.colorScheme.error)
        }
        NoteMarkButton(
            text = "Create Account",
            isEnabled = state.canRegister && !state.isRegistering,
            isLoading = state.isRegistering,
        ) {
            onnAction(RegistrationAction.OnRegisterClick)
        }
        Text(
            stringResource(R.string.have_account),
            style = MaterialTheme.typography.titleSmall,
            modifier =
                Modifier
                    .clickable {
                        onnAction(RegistrationAction.AlreadyAccount)
                    }.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .padding(top = 40.dp),
    ) {
        Text(
            "Create Account",
            modifier =
                Modifier
                    .fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            "Capture your thoughts and ideas.",
            modifier =
                Modifier
                    .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
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
fun RegistrationScreenPreview() {
    NoteMarkTheme {
        RegistrationScreen(
            state = RegistrationState(),
            errorMessage = null,
            onnAction = {},
        )
    }
}