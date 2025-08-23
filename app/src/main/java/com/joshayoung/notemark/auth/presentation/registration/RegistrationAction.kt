package com.joshayoung.notemark.auth.presentation.registration

sealed interface RegistrationAction {
    data object OnRegisterClick : RegistrationAction

    data object AlreadyAccount : RegistrationAction

    data object RegisterSuccess : RegistrationAction
}