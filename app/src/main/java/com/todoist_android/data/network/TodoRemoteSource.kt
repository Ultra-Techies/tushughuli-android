package com.todoist_android.data.network

import com.todoist_android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoRemoteSource {

    //TODO: rename base url
    companion object {
        //I'm using mockapi.io to create a dummy API for testing purposes
        const val BASE_URL = "https://621ce943768a4e1020b93731.mockapi.io/api/v1/"
    }

    //create retrofit client
    fun <API> createRetrofitClient(api: Class<API>): API {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().also { client ->
                if(BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(api)

    }
}

