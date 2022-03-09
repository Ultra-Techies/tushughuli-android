package com.todoist_android.data.network

import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import retrofit2.http.*

interface APIAuthentication {

    //Refer to: https://github.com/Ultra-Techies/backend/blob/main/endpoints/endpoints.md

    @GET("/auth")
    suspend fun login( //suspend because we will use coroutines for our network calls
        @Query("email") email: String,
        @Query("password") password: String
    ) : LoginResponse

    @FormUrlEncoded
    @POST("/signup")
    suspend fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : SignupResponse

}