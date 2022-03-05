package com.todoist_android.data.repository

import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.network.repository.BaseRepo
import javax.inject.Inject

class TaskRepo @Inject constructor(
    private val api: TaskApi
) : BaseRepo() {

    suspend fun getTasks(
        id: String,
        title: String,
        description: String,
        due_date: String,
        status: String
    ) = safeApiCall { api.getTasks(id,title,description, due_date, status) }

}