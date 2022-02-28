package com.todoist_android.view.auth.networkapi

import okhttp3.ResponseBody

//Handle API Success and Error responses
sealed class APIResource<out T : Any> {
    data class Success<out T : Any>(val data: T) : APIResource<T>()

    data class Error(
        val isNetworkError: Boolean,
        val errorClass: Int,
        val errorBody: ResponseBody?
    )
}