package com.todoist_android.data.network

import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.EditTaskResponse
import com.todoist_android.data.responses.TasksResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @HTTP(method = "GET", path = "/api/task/{id}", hasBody = false) //todolist backend passes user id to get tasks
    suspend fun getTasks(
        @Path("id")id: Int
    ) : TasksResponse

    @POST("api/task/add/{id}") //todolist backend passes user id to add task
    suspend fun addTasks(
        @Body taskRequest: AddTaskRequest,
        @Path("id") id: Int): AddTasksResponse

//    @PUT("/api/task/update/{id}")
//    suspend fun editTasks(@Body editTasksRequest: TodoModel, @Path("id") id: String): TodoModel

//    @HTTP(method = "DELETE", path = "/api/task/delete/{id}", hasBody = true)
//    suspend fun deleteTasks(@Body deleteTaskRequest: TodoModel, @Path("id")id: String): TodoModel

    @PUT("api/task/update/{id}")
    suspend fun editTasks(
        @Body editTaskRequest: EditTaskRequest,
        @Path("id") id: Int
    ): EditTaskResponse

    @DELETE("api/task/delete/{id}")
    suspend fun deleteTasks(@Path("id") id: Int): String

}


