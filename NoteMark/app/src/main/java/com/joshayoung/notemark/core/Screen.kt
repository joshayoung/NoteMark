package com.joshayoung.notemark.core

sealed class Screen(val route: String) {
    object Start: Screen(START)
    object Register: Screen(REGISTER)
    object Login: Screen(LOGIN)
    object Landing: Screen("notes_landing")
    object AddNote: Screen("add_note")

    companion object {
        const val LOGIN = "login"
        const val START = "start"
        const val REGISTER = "register"
        val authRoutes = listOf(
            LOGIN,
            START,
            REGISTER
        )
    }
}
