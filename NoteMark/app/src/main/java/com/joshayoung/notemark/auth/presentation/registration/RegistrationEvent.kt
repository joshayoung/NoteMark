package com.joshayoung.notemark.auth.presentation.registration

interface RegistrationEvent {
    data object RegistrationSuccess: RegistrationEvent
    data class Error(val error: String) : RegistrationEvent
}