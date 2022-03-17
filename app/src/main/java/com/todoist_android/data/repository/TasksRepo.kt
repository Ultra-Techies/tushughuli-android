package com.todoist_android.data.repository


import com.todoist_android.data.network.APIResource
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.EditTaskResponse
import com.todoist_android.data.responses.TasksResponse
import javax.inject.Inject


interface TasksRepo {
    suspend fun addTasks(id: Int, taskRequest: AddTaskRequest): APIResource<AddTasksResponse>
    suspend fun editTasks(id: Int, editTasksRequest: EditTaskRequest): APIResource<EditTaskResponse>
    suspend fun deleteTasks(id: Int): APIResource<String>
    suspend fun getTasks(id: Int): APIResource<TasksResponse>
}

class TasksRepoImpl @Inject constructor(
    private val taskApi: TaskApi
) : TasksRepo, BaseRepo() {

    override suspend fun addTasks(id: Int, taskRequest: AddTaskRequest) = safeApiCall {
        taskApi.addTasks(taskRequest, id)
    }

    override suspend fun editTasks(id: Int, editTasksRequest: EditTaskRequest) = safeApiCall {
        taskApi.editTasks(editTasksRequest, id)
    }

    override suspend fun deleteTasks(id: Int) = safeApiCall {
        taskApi.deleteTasks(id)
    }


    override suspend fun getTasks(id: Int) = safeApiCall {
        taskApi.getTasks(id)
    }

}