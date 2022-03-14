package com.todoist_android.data.repository

import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.data.responses.TasksResponse
import com.todoist_android.data.responses.TasksResponseItem

class FakeTasksRepo : TasksRepo {
    override suspend fun addTasks(taskRequest: AddTaskRequest): APIResource<AddTasksResponse> {
        return APIResource.Success(
            AddTasksResponse(
                title = "test",
                description = "test",
                status = "created",
                due_date = "12/12/2022",
                id = 1
            )
        );
    }

    override suspend fun editTasks(editTasksRequest: TodoModel): APIResource<TodoModel> {
        return APIResource.Success(
            TodoModel(
                title = "test",
                description = "test",
                status = "created",
                due_date = "12/12/2022",
                id = "1"
            )
        );
    }

    override suspend fun deleteTasks(editTasksRequest: TodoModel): APIResource<TodoModel> {
        return APIResource.Success(
            TodoModel(
                title = "test",
                description = "test",
                status = "created",
                due_date = "12/12/2022",
                id = "1"
            )
        );
    }

    override suspend fun getTasks(id: String): APIResource<TasksResponse> {
        return APIResource.Success( TasksResponse() )
    }

}