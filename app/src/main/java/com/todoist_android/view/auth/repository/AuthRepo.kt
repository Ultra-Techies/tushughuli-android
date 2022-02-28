package com.todoist_android.view.auth.repository

import com.todoist_android.view.auth.networkapi.APIAuthentication

//Communicates with our external data source
class AuthRepo (
    private val authApi: APIAuthentication
) : BaseRepo() {

    suspend fun login(email: String, password: String) = safeApiCall {
        authApi.login(email, password)
    }
}