package com.joshayoung.notemark.note.domain.models

enum class SyncInterval(val text: String) {
    MANUAL("Manual"),
    FIFTEEN("15 minutes"),
    THIRTY("30 minutes"),
    HOUR("1 hour");

    companion object {
        fun findByTextValue(text: String): SyncInterval {
            return entries.find { it.text == text } ?: MANUAL
        }
    }
}