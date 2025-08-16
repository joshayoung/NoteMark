package com.joshayoung.notemark.core.domain

import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.note.domain.models.SyncInterval
import kotlinx.coroutines.flow.Flow

interface DataStorage {
    fun getAuthData(): Flow<LoginResponse>
    suspend fun saveAuthData(settings: LoginResponse?)
    val values: Flow<String>
    val username: Flow<String>

    suspend fun saveSyncInterval(interval: SyncInterval)

    suspend fun getSyncInterval(): SyncInterval

    suspend fun saveUserId(id: String)

    suspend fun getUserid(): String?
}