package com.todoist_android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.ui.auth.AuthenticationViewModel

//Responsible for giving us all the view models we need
class ViewModelGenerator(
    private val repository: BaseRepo
    ): ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(repository as AuthRepo) as T
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
}