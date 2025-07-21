package com.joshayoung.notemark.domain

interface NoteMarkRepository {
    suspend fun register(username: String, email: String, password: String): Result
    suspend fun login(email: String, password: String): Result
}