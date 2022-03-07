package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import javax.inject.Inject

class TasksRepo@Inject constructor(
private val authApi: APIAuthentication
): BaseRepo()
{
    suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall{
        authApi.addTasks(taskRequest)
    }
}