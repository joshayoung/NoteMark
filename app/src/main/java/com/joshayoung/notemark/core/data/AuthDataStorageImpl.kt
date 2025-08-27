package com.joshayoung.notemark.core.data

import UserPreferences
import androidx.datastore.core.DataStore
import com.joshayoung.notemark.core.domain.AuthDataStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthDataStorageImpl(
    private val dataStore: DataStore<UserPreferences>,
) : AuthDataStorage {
//    override fun getAuthData(): Flow<LoginResponse> =
//        dataStore.data
//            .map { preferences ->
//                LoginResponse(
//                    accessToken = preferences.accessToken ?: "'",
//                    refreshToken = preferences.refreshToken ?: "",
//                    username = preferences.username ?: "",
//                )
//            }
//
//    override suspend fun saveAuthData(settings: LoginResponse?) {
//        if (settings == null) {
//            dataStore.updateData { preferences ->
//                UserPreferences()
//            }
//
//            return
//        }
//
//        dataStore.updateData {
//            UserPreferences(
//                accessToken = settings.accessToken,
//                refreshToken = settings.refreshToken,
//                username = settings.username,
//            )
//        }
//    }
//
//    override val values: Flow<UserPreferences> =
//        dataStore.data
//            .map { it }
//
    override val username: Flow<String> =
        dataStore.data.map { preferences -> preferences.username ?: "" }
}