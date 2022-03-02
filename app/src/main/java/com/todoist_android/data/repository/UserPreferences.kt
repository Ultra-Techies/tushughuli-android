package com.todoist_android.data.repository

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences (context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "todo_data_store"
    )

    val todoToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[TOKEN]
        }

    suspend fun saveToken(todoToken: String) {
        dataStore.edit {
            it[TOKEN] = todoToken
        }
    }

    companion object {
        private val TOKEN = preferencesKey<String>("token")
    }

}