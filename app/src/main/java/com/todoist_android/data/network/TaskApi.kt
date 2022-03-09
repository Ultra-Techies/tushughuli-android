package com.todoist_android.data.network

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.TasksResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    @GET("/tasks")
    suspend fun getTasks(
        @Query("id") id: String
    ) : TasksResponse

    @POST("/tasks")
    suspend fun addTasks(@Body tasksRequest: AddTaskRequest): AddTasksResponse

    @PUT("/tasks/{id}")
    suspend fun editTasks(@Body editTasksRequest: TodoModel, @Path("id") id: String): TodoModel

    @HTTP(method = "DELETE", path = "/tasks/{id}", hasBody = true)
    suspend fun deleteTasks(@Body deleteTaskRequest: TodoModel, @Path("id")id: String): TodoModel
}