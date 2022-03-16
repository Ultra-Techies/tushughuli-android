package com.todoist_android.data.repository


import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.TasksResponse
import javax.inject.Inject


interface TasksRepo {
    suspend fun addTasks(taskRequest: AddTaskRequest): APIResource<AddTasksResponse>
    suspend fun editTasks(editTasksRequest: TodoModel): APIResource<TodoModel>
    suspend fun deleteTasks(editTasksRequest: TodoModel): APIResource<TodoModel>
    suspend fun getTasks(id: String): APIResource<TasksResponse>
}
class TasksRepo@Inject constructor(
private val taskApi: TaskApi
): BaseRepo()
{

    suspend fun addTasks(id: Int,taskRequest: AddTaskRequest) = safeApiCall {
        taskApi.addTasks(taskRequest, id)

class TasksRepoImpl @Inject constructor(
    private val taskApi: TaskApi
) : TasksRepo, BaseRepo() {
    override suspend fun addTasks(taskRequest: AddTaskRequest) = safeApiCall {
        taskApi.addTasks(taskRequest)
    }

    suspend fun editTasks(id: Int,editTasksRequest: EditTaskRequest) = safeApiCall {
        taskApi.editTasks(editTasksRequest,id)
    }

    suspend fun deleteTasks(id: Int) = safeApiCall { taskApi.deleteTasks(id) }

    suspend fun getTasks(id: Int) = safeApiCall {
        taskApi.getTasks(id)
    }
}