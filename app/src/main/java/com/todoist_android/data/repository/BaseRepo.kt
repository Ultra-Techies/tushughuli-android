package com.todoist_android.data.network.repository

import android.util.Log
import android.widget.Toast
import com.todoist_android.data.network.APIResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepo {

    suspend fun <T : Any> safeApiCall(
        apiCall: suspend () -> T,
    ) : APIResource<T> {
        return withContext(Dispatchers.IO) {
            Log.d("BaseRepo", "safeApiCall: started")
            try {
                Log.d("BaseRepo", "safeApiCall: apicall.invoke()")
                val response = apiCall.invoke()

                Log.d("BaseRepo", "safeApiCall: apicall.success")
                APIResource.Success(response)
            } catch (throwable: Throwable) {
                Log.d("BaseRepo", "safeApiCall: ${throwable.message}")
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