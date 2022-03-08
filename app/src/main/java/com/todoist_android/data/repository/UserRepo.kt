package com.todoist_android.data.repository

import com.todoist_android.data.network.UserApi
import com.todoist_android.data.network.repository.BaseRepo
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val api: UserApi
) : BaseRepo() {

    suspend fun getUser(id: String) = safeApiCall { api.getUser(id) }

    suspend fun deleteUser(id: String) = safeApiCall { api.deleteUser(id) }

}