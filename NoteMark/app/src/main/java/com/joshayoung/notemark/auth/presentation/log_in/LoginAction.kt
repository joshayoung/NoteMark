package com.joshayoung.notemark.auth.presentation.log_in

sealed interface LoginAction {
    data object OnLoginClick: LoginAction
    data object DontHaveAccount: LoginAction
    data object LoginSuccess: LoginAction
}