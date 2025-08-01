package com.joshayoung.notemark.core

sealed class Screen(val route: String) {
    object Start: Screen("start")
    object Register: Screen("register")
    object Login: Screen("login")
    object Landing: Screen("notes_landing")
    object AddNote: Screen("add_note")
}
