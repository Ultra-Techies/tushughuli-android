package com.todoist_android.data.repository


import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import javax.inject.Inject

class TasksRepo@Inject constructor(
private val taskApi: TaskApi
): BaseRepo()
{
    suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall{
        //taskApi.addTasks(taskRequest, taskRequest.id!!) //todolist backend requires id
        taskApi.addTasks(taskRequest, taskRequest.id!!) //mock API
    suspend fun addTasks(id: Int,taskRequest: AddTaskRequest) = safeApiCall{
        taskApi.addTasks(taskRequest,id)
    }

    suspend fun editTasks(id: Int,editTasksRequest: EditTaskRequest) = safeApiCall {
        taskApi.editTasks(editTasksRequest,id)

    }


    suspend fun deleteTasks(id: Int) = safeApiCall { taskApi.deleteTasks(id) }

    suspend fun getTasks(id: String) = safeApiCall {
        taskApi.getTasks(id)
    }
}