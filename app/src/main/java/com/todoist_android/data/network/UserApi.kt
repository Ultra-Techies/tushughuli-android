package com.todoist_android.data.network

import com.todoist_android.data.responses.UserDeleteResponse
import com.todoist_android.data.responses.UserResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApi {

    @GET("/user")
    suspend fun getUser(
        @Query("id") id: String,
    ) : UserResponse

    @PUT("/user/1")
    suspend fun editUser(
        @Query("id") id: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("email") email: String,
        @Query("display_name") display_name: String,
        @Query("profile_photo") profile_photo: String
    ) : UserResponse

    @DELETE("/user")
    suspend fun deleteUser(
        @Query("id") id: String,
    ) : UserDeleteResponse
}