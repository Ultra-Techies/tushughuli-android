package com.todoist_android.data.repository

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.DeleteTaskRequest
import javax.inject.Inject

class TasksRepo@Inject constructor(
private val authApi: APIAuthentication
): BaseRepo()
{
    suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall{
        authApi.addTasks(taskRequest)
    }

    suspend fun editTasks(editTasksRequest: TodoModel) = safeApiCall {
        authApi.editTasks(editTasksRequest)
    }

    suspend fun deleteTasks(deleteTaskRequest: DeleteTaskRequest) = safeApiCall {
        authApi.deleteTasks(deleteTaskRequest)
    }
}