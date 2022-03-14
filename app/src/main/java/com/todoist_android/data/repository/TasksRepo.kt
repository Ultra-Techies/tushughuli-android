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
//        taskApi.addTasks(taskRequest,4)
//        val taskRequest = AddTaskRequest(
//            description=description,
//            title=title,
//            due_date = due_date,
//            reminder = reminder
//        )
        taskApi.addTasks(taskRequest,4)
    }

    suspend fun editTasks(id: Int,editTasksRequest: EditTaskRequest) = safeApiCall {
//        taskApi.editTasks(editTasksRequest, editTasksRequest.id!!)
        taskApi.editTasks(editTasksRequest,id)

    }

//    suspend fun deleteTasks(deleteTaskRequest: TodoModel) = safeApiCall {
//        taskApi.deleteTasks(deleteTaskRequest,deleteTaskRequest.id!!)
//    }
    suspend fun deleteTasks(id: Int) = safeApiCall { taskApi.deleteTasks(id) }

    suspend fun getTasks(id: Int) = safeApiCall { taskApi.getTasks(4) }
}