package com.todoist_android.data.network

import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.EditTaskResponse
import com.todoist_android.data.responses.TasksResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    //    @GET("/tasks")
//    suspend fun getTasks(
//        @Query("id") id: String
//    ) : TasksResponse
    @GET("task/{id}")
    suspend fun getTasks(
        @Path("id") id: Int
    ): TasksResponse


//    @POST("/tasks")
//    suspend fun addTasks(@Body tasksRequest: AddTaskRequest): AddTasksResponse

    @POST("task/add/{id}")
    suspend fun addTasks(@Body taskRequest: AddTaskRequest, @Path("id") id: Int): AddTasksResponse


//    @PUT("/tasks/{id}")
//    suspend fun editTasks(@Body editTasksRequest: TodoModel, @Path("id") id: Int): TodoModel

    @PUT("task/update/{id}")
    suspend fun editTasks(
        @Body editTaskRequest: EditTaskRequest,
        @Path("id") id: Int
    ): EditTaskResponse

//    @HTTP(method = "DELETE", path = "/tasks/{id}", hasBody = true)
//    suspend fun deleteTasks(@Body deleteTaskRequest: TodoModel, @Path("id") id: Int): TodoModel

    @DELETE("task/delete/{id}")
    suspend fun deleteTasks(@Path("id") id: Int): String
}


