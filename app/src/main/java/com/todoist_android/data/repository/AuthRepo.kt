package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.requests.SignUpRequest
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignIn
import javax.inject.Inject

//Communicates with our external data source
class AuthRepo @Inject constructor  (
    private val authApi: APIAuthentication,
    private val userPrefs: UserPreferences
) : BaseRepo() {

    suspend fun login(email: String, password: String) = safeApiCall {
//      val loginRequest = LoginRequest(
//          email = email,
//          id = id,
//          photo =photo,
//          username = username,
//          name = name
//
//      )
        authApi.login(4)
    }

    suspend fun signup(username: String, email: String, password: String, photo: String, name: String) = safeApiCall {
        val signUpRequest = SignUpRequest(
            username = username,
            email = email,
            password = password,
            photo = photo,
            name = name,
        )
        authApi.signup(signUpRequest)
    }

    suspend fun saveToken(token: String) {
        userPrefs.saveToken(token)
    }
}