package com.todoist_android.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "todo_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val applicationContext = context.applicationContext

    val todoToken: Flow<String?>
        get() = applicationContext.dataStore.data.map { preferences ->
            preferences[TOKEN]
        }

    val notificationState: Flow<String?>
        get() = applicationContext.dataStore.data.map { preferences ->
            preferences[NOTIFICATION_STATE]
        }

    suspend fun saveToken(todoToken: String) {
        applicationContext.dataStore.edit {
            it[TOKEN] = todoToken
        }
    }

    suspend fun clearToken() {
        applicationContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveNotificationState(state: Boolean) {
        applicationContext.dataStore.edit {
            it[NOTIFICATION_STATE] = state.toString()
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val NOTIFICATION_STATE = stringPreferencesKey("notification_state")
    }

}