package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.repository.BaseRepo

//Communicates with our external data source
class AuthRepo (
    private val authApi: APIAuthentication
) : BaseRepo() {

    suspend fun login(email: String, password: String) = safeApiCall {
        authApi.login(email, password)
    }

    suspend fun signup(username: String, email: String, password: String) = safeApiCall {
        authApi.signup(username, email, password)
    }
}