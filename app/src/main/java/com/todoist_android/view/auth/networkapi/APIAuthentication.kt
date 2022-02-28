package com.todoist_android.view.auth.networkapi

import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIAuthentication {

    //Refer to: https://github.com/Ultra-Techies/backend/blob/main/endpoints/endpoints.md

    @FormUrlEncoded
    @POST("/auth")
    fun login(email: String, password: String, callback: (success: Boolean) -> Unit)
}