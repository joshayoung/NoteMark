package com.joshayoung.notemark.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.joshayoung.notemark.core.domain.NoteDataStorage

private object SettingValues {
    val SYNC_INTERVAL = stringPreferencesKey("sync_interval")
    val SYNC_USER = stringPreferencesKey("sync_user")
}

class NoteDataStorageImpl(
    private val dataStore: DataStore<Preferences>,
) : NoteDataStorage {
//    override suspend fun saveSyncInterval(interval: SyncInterval) {
//        dataStore.edit { preferences ->
//            preferences[SettingValues.SYNC_INTERVAL] = interval.text
//        }
//    }
//
//    override suspend fun saveUserId(id: String) {
//        dataStore.edit { preferences ->
//            preferences[SettingValues.SYNC_USER] = id
//        }
//    }
//
//    override suspend fun getUserid(): String? {
//        val value = dataStore.data.first()
//
//        return value[SettingValues.SYNC_USER]
//    }
//
//    override suspend fun getSyncInterval(): SyncInterval {
//        val value = dataStore.data.first()
//        val interval = value[SettingValues.SYNC_INTERVAL] ?: ""
//
//        return try {
//            SyncInterval.findByTextValue(interval)
//        } catch (e: Exception) {
//            SyncInterval.MANUAL
//        }
//    }
}