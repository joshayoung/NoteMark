package com.joshayoung.notemark.domain.repository

import com.joshayoung.notemark.domain.models.Result

interface NoteMarkRepository {
    suspend fun register(username: String, email: String, password: String): Result
    suspend fun login(email: String, password: String): Result
}