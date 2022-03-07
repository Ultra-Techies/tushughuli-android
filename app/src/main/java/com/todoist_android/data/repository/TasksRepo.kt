package com.todoist_android.data.repository

import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import javax.inject.Inject

class TasksRepo@Inject constructor(
private val taskApi: TaskApi
): BaseRepo()
{
    suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall{
        taskApi.addTasks(taskRequest)
    }

    suspend fun getTasks(id: String) = safeApiCall { taskApi.getTasks(id) }
}