package com.todoist_android.data.network

import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.requests.SignUpRequest
import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.requests.UpdateUserRequest
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIAuthentication {

    //Refer to: https://github.com/Ultra-Techies/backend/blob/main/endpoints/endpoints.md

    @POST("/api/user/auth")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse
    //@FormUrlEncoded
    @POST("/api/signup")
    @Body userCreateRequest: UpdateUserRequest
    ) : SignupResponse


    @POST("user")
    suspend fun signup(
        @Body signUpRequest: SignUpRequest
    ): SignupResponse


}