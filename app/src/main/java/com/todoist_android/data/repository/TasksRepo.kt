package com.todoist_android.data.repository

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.TasksResponse
import javax.inject.Inject

interface TasksRepo {
    suspend fun addTasks(taskRequest: AddTaskRequest): APIResource<AddTasksResponse>
    suspend fun editTasks(editTasksRequest: TodoModel): APIResource<TodoModel>
    suspend fun deleteTasks(editTasksRequest: TodoModel): APIResource<TodoModel>
    suspend fun getTasks(id: String): APIResource<TasksResponse>
}

class TasksRepoImpl @Inject constructor(
    private val taskApi: TaskApi
) : TasksRepo, BaseRepo() {
    override suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall {
        taskApi.addTasks(taskRequest)
    }

    override suspend fun editTasks(editTasksRequest: TodoModel) = safeApiCall {
        taskApi.editTasks(editTasksRequest, editTasksRequest.id!!)
    }

    override suspend fun deleteTasks(deleteTaskRequest: TodoModel) = safeApiCall {
        taskApi.deleteTasks(deleteTaskRequest, deleteTaskRequest.id!!)
    }

    override suspend fun getTasks(id: String) = safeApiCall { taskApi.getTasks(id) }
}