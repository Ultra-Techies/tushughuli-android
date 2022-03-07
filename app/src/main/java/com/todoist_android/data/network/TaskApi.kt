package com.todoist_android.data.network

import com.todoist_android.data.responses.TasksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TaskApi {
    @GET("/tasks")
    suspend fun getTasks(
        @Query("id") id: String
    ) : TasksResponse
}