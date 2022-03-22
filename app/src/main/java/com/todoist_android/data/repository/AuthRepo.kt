package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.requests.SignUpRequest
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.requests.UpdateUserRequest
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Communicates with our external data source
interface AuthRepo{

    suspend fun login(email: String,password: String):APIResource<LoginResponse>
    suspend fun signup(username: String, email: String, password: String, photo: String, name: String):APIResource<SignupResponse>
    suspend fun saveToken(token: String)

}
class AuthRepoImpl @Inject constructor  (
    private val authApi: APIAuthentication,
    private val userPrefs: UserPreferences
) : AuthRepo,BaseRepo() {

   override suspend fun login(email: String, password: String) = safeApiCall {
        authApi.login(loginRequest = LoginRequest(email, password))
    }

    override suspend fun signup(username: String, email: String, password: String, photo: String, name: String) = safeApiCall{
        val signUpRequest = SignUpRequest(
            username = username,
            email = email,
            password = password,
            photo = photo,
            name = name,
        )
        authApi.signup(signUpRequest)
    }

override suspend fun saveToken(token: String) {
        userPrefs.saveToken(token)
    }
}