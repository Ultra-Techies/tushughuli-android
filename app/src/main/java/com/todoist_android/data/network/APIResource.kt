package com.todoist_android.data.network

import okhttp3.ResponseBody

//Handle API Success and Error responses
sealed class APIResource<out T : Any> {
    data class Success<out T : Any>(val data: Any?) : APIResource<T>()

    data class Error(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : APIResource<Nothing>()
}