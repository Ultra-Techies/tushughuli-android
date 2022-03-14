package com.todoist_android.data.requests

data class AddTaskRequest(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var dueDate: String? = null,
    var status: String? = null,
    var reminder: String? = null
)
