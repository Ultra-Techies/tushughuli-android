package com.todoist_android.data.network

import com.todoist_android.data.requests.UpdateUserRequest
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import retrofit2.http.*

interface APIAuthentication {

    //Refer to: https://github.com/Ultra-Techies/backend/blob/main/endpoints/endpoints.md

    @GET("/api/auth")
    suspend fun login( //suspend because we will use coroutines for our network calls
        @Query("email") email: String,
        @Query("password") password: String
    ) : LoginResponse

    //@FormUrlEncoded
    @POST("/api/signup")
    suspend fun signup(
        @Body userCreateRequest: UpdateUserRequest
    ) : SignupResponse

}