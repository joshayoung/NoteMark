package com.joshayoung.notemark.presentation.registration

interface RegistrationEvent {
    data object RegistrationSuccess: RegistrationEvent
    data class Error(val error: com.joshayoung.notemark.data.Error?) : RegistrationEvent
}