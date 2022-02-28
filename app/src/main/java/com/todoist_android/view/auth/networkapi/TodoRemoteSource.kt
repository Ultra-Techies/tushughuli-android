package com.todoist_android.view.auth.networkapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoRemoteSource {

    //TODO: rename base url
    companion object {
        const val BASE_URL = "<url_goes_here>/API/v1/"
    }

    //create retrofit client
    fun <API> createRetrofitClient(api: Class<API>): API {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(api)

    }
}