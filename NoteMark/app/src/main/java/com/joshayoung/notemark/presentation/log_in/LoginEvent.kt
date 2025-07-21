package com.joshayoung.notemark.presentation.log_in

sealed interface LoginEvent {
    data object Success: LoginEvent
}
