package com.todoist_android.data.network

import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIAuthentication {

    //Refer to: https://github.com/Ultra-Techies/backend/blob/main/endpoints/endpoints.md

    @FormUrlEncoded
    @GET("/auth")
    suspend fun login( //suspend because we will use coroutines for our network calls
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @FormUrlEncoded
    @POST("/signup")
    suspend fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : SignupResponse
}