package com.todoist_android.view.auth.repository

import com.todoist_android.view.auth.networkapi.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.invoke.CallSite

abstract class BaseRepo {

    suspend fun <T : Any> safeApiCall(
        apiCall: suspend () -> T,
    ) : APIResource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall.invoke()
                APIResource.Success(response)
            } catch (throwable: Throwable) {
                when(throwable){
                    is HttpException -> {
                        APIResource.Error(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        APIResource.Error(true, null, null)
                    }
                }
            }
        }
    }
}