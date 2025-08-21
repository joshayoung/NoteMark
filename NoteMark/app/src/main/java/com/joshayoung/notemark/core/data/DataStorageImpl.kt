package com.joshayoung.notemark.core.data

import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.core.domain.DataStorage
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.joshayoung.notemark.note.domain.models.SyncInterval
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private object AuthPreferenceValues {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val USERNAME = stringPreferencesKey("username")
}

private object SettingValues {
    val SYNC_INTERVAL = stringPreferencesKey("sync_interval")
    val SYNC_USER = stringPreferencesKey("sync_user")
}

class DataStorageImpl(
    private val dataStore: DataStore<Preferences>
) : DataStorage {

    override val values: Flow<String?> = dataStore.data
        .map { preferences ->
            val value = preferences[AuthPreferenceValues.REFRESH_TOKEN]
            value
        }

    override val username: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[AuthPreferenceValues.USERNAME] ?: ""
        }

    override fun getAuthData(): Flow<LoginResponse> = dataStore.data
        .map { preferences ->
            val t = LoginResponse(
                accessToken = preferences[AuthPreferenceValues.ACCESS_TOKEN] ?: "",
                refreshToken = preferences[AuthPreferenceValues.REFRESH_TOKEN] ?: "",
                username = preferences[AuthPreferenceValues.USERNAME] ?: ""
            )
            t
        }

    override suspend fun saveAuthData(settings: LoginResponse?) {
        if (settings == null) {
            dataStore.edit { preferences ->
                preferences.remove(AuthPreferenceValues.ACCESS_TOKEN)
                preferences.remove(AuthPreferenceValues.REFRESH_TOKEN)
                preferences.remove(AuthPreferenceValues.USERNAME)
            }

            return;
        }

        dataStore.edit { preferences ->
            preferences[AuthPreferenceValues.ACCESS_TOKEN] = settings.accessToken
            preferences[AuthPreferenceValues.REFRESH_TOKEN] = settings.refreshToken
            preferences[AuthPreferenceValues.USERNAME] = settings.username
        }
    }

    override suspend fun saveSyncInterval(interval: SyncInterval) {
        dataStore.edit { preferences ->
            preferences[SettingValues.SYNC_INTERVAL] = interval.text
        }
    }

    override suspend fun saveUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[SettingValues.SYNC_USER] = id
        }
    }

    override suspend fun getUserid(): String? {
        val value = dataStore.data.first()

        return value[SettingValues.SYNC_USER]
    }

    override suspend fun getSyncInterval(): SyncInterval {
        val value = dataStore.data.first()
        val interval = value[SettingValues.SYNC_INTERVAL] ?: ""

        return try {
            SyncInterval.findByTextValue(interval)
        } catch (e: Exception) {
            SyncInterval.MANUAL
        }
    }
}