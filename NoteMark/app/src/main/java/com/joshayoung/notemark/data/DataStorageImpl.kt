package com.joshayoung.notemark.data

import android.util.Log
import com.joshayoung.notemark.domain.LoginResponse
import com.joshayoung.notemark.domain.DataStorage
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private object AuthPreferenceValues {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val USERNAME = stringPreferencesKey("username")
}

class DataStorageImpl(
    private val dataStore: DataStore<Preferences>
) : DataStorage {

    override val values: Flow<String> = dataStore.data
        .map { preferences ->
            val value = preferences[AuthPreferenceValues.REFRESH_TOKEN] ?: "Default Value"
            value
        }

    override fun getAuthData(): Flow<LoginResponse> = dataStore.data
        .map { preferences ->
            LoginResponse(
                accessToken = preferences[AuthPreferenceValues.ACCESS_TOKEN],
                refreshToken = preferences[AuthPreferenceValues.REFRESH_TOKEN],
                username = preferences[AuthPreferenceValues.USERNAME]
            )
        }

    override suspend fun saveAuthData(settings: LoginResponse?) {
        if (settings == null) {
            dataStore.edit { preferences ->
                preferences[AuthPreferenceValues.ACCESS_TOKEN] = "unset"
                preferences[AuthPreferenceValues.REFRESH_TOKEN] = "unset"
                preferences[AuthPreferenceValues.USERNAME] = "unset"
            }

            return;
        }
        dataStore.edit { preferences ->
            preferences[AuthPreferenceValues.ACCESS_TOKEN] = settings.accessToken ?: ""
            preferences[AuthPreferenceValues.REFRESH_TOKEN] = settings.refreshToken ?: ""
            preferences[AuthPreferenceValues.USERNAME] = settings.username ?: ""
        }
    }
}