package com.joshayoung.notemark.presentation.log_in

sealed interface LoginAction {
    data object OnLoginClick: LoginAction
}