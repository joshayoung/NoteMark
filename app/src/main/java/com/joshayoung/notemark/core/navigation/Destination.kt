package com.joshayoung.notemark.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object AuthGraph : Destination

    @Serializable
    data object NoteGraph : Destination

    @Serializable
    data object StartScreen : Destination

    @Serializable
    data object RegistrationScreen : Destination

    @Serializable
    data object LoginScreen : Destination

    @Serializable
    data object NoteListScreen : Destination

    @Serializable
    data class AddNoteScreen(
        val id: Long?,
    ) : Destination

    @Serializable
    data object SettingsScreen : Destination

    @Serializable
    data class NoteDetailScreen(
        val id: Long,
    ) : Destination
}