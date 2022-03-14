package com.todoist_android.data.requests

import java.lang.ref.PhantomReference

data class LoginRequest(
    val email:String?= null,
    val password:String? = null,
    val id : Int? = null,
    val username:String? = null,
    val name: String?=null,
    val photo: String? =null
)
