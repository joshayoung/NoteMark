package com.joshayoung.notemark.presentation.registration

sealed interface RegistrationAction {
    data object OnRegisterClick: RegistrationAction
}