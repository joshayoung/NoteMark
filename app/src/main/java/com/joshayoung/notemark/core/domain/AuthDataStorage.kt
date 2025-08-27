package com.joshayoung.notemark.core.domain

import kotlinx.coroutines.flow.Flow

interface AuthDataStorage {
//    fun getAuthData(): Flow<LoginResponse>
//
//    suspend fun saveAuthData(settings: LoginResponse?)
//
//    val values: Flow<UserPreferences>
//
    val username: Flow<String>
}