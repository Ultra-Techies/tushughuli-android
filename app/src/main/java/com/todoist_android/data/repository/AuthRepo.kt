package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.requests.UpdateUserRequest
import javax.inject.Inject

//Communicates with our external data source
class AuthRepo @Inject constructor  (
    private val authApi: APIAuthentication,
    private val userPrefs: UserPreferences
) : BaseRepo() {

    suspend fun login(email: String, password: String) = safeApiCall {
        authApi.login(loginRequest = LoginRequest(email, password))
    }

    suspend fun signup(username: String, name: String = username, email: String, photo: String, password: String) = safeApiCall {
        authApi.signup(userCreateRequest = UpdateUserRequest(username, name, password,email, photo))
    }

    suspend fun saveToken(token: String) {
        userPrefs.saveToken(token)
    }
}