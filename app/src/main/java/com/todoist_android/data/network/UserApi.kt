package com.todoist_android.data.network

import com.todoist_android.data.responses.UserDeleteResponse
import com.todoist_android.data.responses.UserResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("/user")
    suspend fun getUser(
        @Query("id") id: String,
    ) : UserResponse

    @DELETE("/user")
    suspend fun deleteUser(
        @Query("id") id: String,
    ) : UserDeleteResponse
}