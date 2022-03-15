package com.todoist_android.data.network

import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.requests.UpdateUserRequest
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import retrofit2.http.*

interface APIAuthentication {

    //Refer to: https://github.com/Ultra-Techies/backend/blob/main/endpoints/endpoints.md

    @POST("/api/user/auth")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse

    //@FormUrlEncoded
    @POST("/api/signup")
    suspend fun signup(
        @Body userCreateRequest: UpdateUserRequest
    ) : SignupResponse

}