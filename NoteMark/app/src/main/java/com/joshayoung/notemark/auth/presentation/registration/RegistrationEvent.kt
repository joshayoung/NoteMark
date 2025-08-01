package com.joshayoung.notemark.auth.presentation.registration

interface RegistrationEvent {
    data object RegistrationSuccess: RegistrationEvent
    data class Error(val error: com.joshayoung.notemark.core.data.Error?) : RegistrationEvent
}