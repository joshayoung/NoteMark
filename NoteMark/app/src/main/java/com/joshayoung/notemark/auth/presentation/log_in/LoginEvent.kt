package com.joshayoung.notemark.auth.presentation.log_in

sealed interface LoginEvent {
    data object Success: LoginEvent
    data object Failure: LoginEvent
}
