package com.todoist_android.data.network

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.models.UserModel
import com.todoist_android.data.requests.UpdateUserRequest
import com.todoist_android.data.responses.UserDeleteResponse
import com.todoist_android.data.responses.UserResponse
import retrofit2.http.*

interface UserApi {

    @GET("/api/user/{id}")
    suspend fun getUser(
        @Path("id") id: String,
    ) : UserResponse

    @FormUrlEncoded
    @PUT("/api/user/{id}")
    suspend fun editUser(
        //@Body updateUserRequest: UserModel,
        @Path("id") id: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("photo") photo: String,
        @Field("password") password: String
    ): UserModel


    @DELETE("/user/1")
    suspend fun deleteUser(
        @Query("id") id: String,
    ) : UserDeleteResponse
}