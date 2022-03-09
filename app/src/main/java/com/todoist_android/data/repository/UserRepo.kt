package com.todoist_android.data.repository

import com.todoist_android.data.network.UserApi
import com.todoist_android.data.network.repository.BaseRepo
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val api: UserApi,
    private val userPrefs: UserPreferences
) : BaseRepo() {

    suspend fun getUser(id: String) = safeApiCall { api.getUser(id) }

    suspend fun editUser(
        id: String,
        username: String,
        password: String,
        email: String,
        display_name: String,
        profile_photo: String
    ) = safeApiCall { api.editUser(id, username, password, email, display_name,profile_photo) }

    suspend fun deleteUser(id: String) = safeApiCall { api.deleteUser(id) }

    suspend fun saveNotificationState(state: Boolean) {
        userPrefs.saveNotificationState(state)
    }

}