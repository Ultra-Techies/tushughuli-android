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
    //@HTTP(method = "GET", path = "/api/task/{id}", hasBody = false) //todolist backend passes user id to get tasks
    @HTTP(method = "GET", path = "/api/tasks", hasBody = false) //mockapi
    suspend fun getTasks(
        @Query("id") id: String
        //@Path("id")id: String
    ) : TasksResponse

    //@POST("/api/tasks/add/{id}") todolist backend passes user id to add task
    //suspend fun addTasks(@Body tasksRequest: AddTaskRequest, @Path("id") id: String): AddTasksResponse //todolist backend

    @POST("/api/tasks") //mockapi
    suspend fun addTasks(@Body tasksRequest: AddTaskRequest): AddTasksResponse //mockapi

    @PUT("/tasks/{id}")
    suspend fun editTasks(@Body editTasksRequest: TodoModel, @Path("id") id: String): TodoModel

    @HTTP(method = "DELETE", path = "/tasks/{id}", hasBody = true)
    suspend fun deleteTasks(@Body deleteTaskRequest: TodoModel, @Path("id")id: String): TodoModel
}