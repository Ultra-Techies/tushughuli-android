package com.todoist_android.data.requests

data class AddTaskRequest(
    var id: String?,
    var title: String?,
    var description: String?,
    var due_date: String?,
    var status: String?
)
