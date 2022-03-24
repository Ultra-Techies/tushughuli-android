package com.todoist_android.data.repository

import androidx.lifecycle.viewModelScope
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.models.UserModel
import com.todoist_android.data.network.UserApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.UpdateUserRequest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val api: UserApi,
    private val userPrefs: UserPreferences
) : BaseRepo() {

    suspend fun getUser(id: String) = safeApiCall { api.getUser(id) }

    suspend fun updateUser(updateUserRequest: UserModel) = safeApiCall {
        api.editUser(
            updateUserRequest.id!!,
            updateUserRequest.username!!,
            updateUserRequest.email!!,
            updateUserRequest.photo!!,
            updateUserRequest.password ?: "")
    }

    suspend fun deleteUser(id: String) = safeApiCall { api.deleteUser(id) }

    suspend fun deleteAllTasks(id: String) = safeApiCall { api.deleteAllTasks(id) }

    suspend fun saveNotificationState(state: Boolean) {
        userPrefs.saveNotificationState(state)
    }

}