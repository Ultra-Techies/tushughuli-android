package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.repository.BaseRepo
import javax.inject.Inject

//Communicates with our external data source
class AuthRepo @Inject constructor  (
    private val authApi: APIAuthentication
) : BaseRepo() {

    suspend fun login(email: String, password: String) = safeApiCall {
        authApi.login(email, password)
    }
}