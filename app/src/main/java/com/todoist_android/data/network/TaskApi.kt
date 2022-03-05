package com.todoist_android.data.network

import com.todoist_android.data.responses.TaskResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TaskApi {
    @GET("/tasks")
    suspend fun getTasks(
        @Query("id") id: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("due_date") dueDate: String,
        @Query("status") status: String,
    ) : TaskResponse
}