package com.todoist_android.data.requests

data class UpdateUserRequest (
    var id: String? = null,
    var username: String? = null,
    var password: String? = null,
    var email: String? = null,
    var display_name: String? = null,
    var profile_photo: String? = null,

)