package com.joshayoung.notemark.core

sealed class Screen(val route: String) {
    object Start: Screen(START)
    object Register: Screen(REGISTER)
    object Login: Screen(LOGIN)
    object Landing: Screen(NOTES_LANDING)
    object AddNote: Screen(ADD_NOTE)
    object Settings: Screen(SETTINGS)

    companion object {
        const val LOGIN = "login"
        const val START = "start"
        const val REGISTER = "register"
        const val SETTINGS = "settings"
        const val ADD_NOTE = "add_note"
        const val NOTES_LANDING = "notes_landing"

        val authRoutes = listOf(
            LOGIN,
            START,
            REGISTER
        )
    }
}
