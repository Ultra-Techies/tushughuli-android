package com.todoist_android.data.repository

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import javax.inject.Inject

class TasksRepo@Inject constructor(
private val taskApi: TaskApi
): BaseRepo()
{
    suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall{
        //taskApi.addTasks(taskRequest, taskRequest.id!!) //todolist backend requires id
        taskApi.addTasks(taskRequest, taskRequest.id!!) //mock API
    }

    suspend fun editTasks(editTasksRequest: TodoModel) = safeApiCall {
        taskApi.editTasks(editTasksRequest, editTasksRequest.id!!)
    }

    suspend fun deleteTasks(deleteTaskRequest: TodoModel) = safeApiCall {
        taskApi.deleteTasks(deleteTaskRequest,deleteTaskRequest.id!!)
    }

    suspend fun getTasks(id: String) = safeApiCall {
        taskApi.getTasks(id)
    }
}