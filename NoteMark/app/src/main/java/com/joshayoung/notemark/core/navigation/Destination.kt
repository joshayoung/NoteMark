package com.joshayoung.notemark.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object AuthGraph: Destination

    @Serializable
    data object NoteGraph: Destination

    @Serializable
    data object StartScreen: Destination

    @Serializable
    data object Registration: Destination

    @Serializable
    data object Login: Destination

    @Serializable
    data object NoteList: Destination

    @Serializable
    data class AddNote(val id: Int?): Destination

    @Serializable
    data object Settings: Destination

    @Serializable
    data object NoteDetail: Destination
}