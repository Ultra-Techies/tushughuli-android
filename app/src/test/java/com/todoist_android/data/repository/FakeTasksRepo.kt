package com.todoist_android.data.repository

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.EditTaskResponse
import com.todoist_android.data.responses.TasksResponse
import com.todoist_android.data.responses.TasksResponseItem

class FakeTasksRepo : TasksRepo {
    override suspend fun addTasks(
        id: Int,
        taskRequest: AddTaskRequest
    ): APIResource<AddTasksResponse> {
      return APIResource.Success( AddTasksResponse(
            title = "test",
            description = "test",
            status = "created",
            dueDate = "12/12/2022",
            reminder = "11/12/2022",
            id = 2
        ))
    }

    override suspend fun editTasks(
        id: Int,
        editTasksRequest: EditTaskRequest
    ): APIResource<EditTaskResponse> {
        return APIResource.Success(
            EditTaskResponse(
                description = "test",
                dueDate = "2022-03-18 13:44:25",
                id = 2,
                reminder = "2022-03-17 13:44:25 ",
                status = "created",
                title = "test"

            )
        )
    }

    override suspend fun deleteTasks(id: Int): APIResource<String> {
  return APIResource.Success(
      "Deleted successfully"
  )
    }

    override suspend fun getTasks(id: Int): APIResource<TasksResponse> {
       return APIResource.Success(
           TasksResponse()
       )
    }

}