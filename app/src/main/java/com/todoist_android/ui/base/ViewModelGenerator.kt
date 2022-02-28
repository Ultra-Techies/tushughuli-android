package com.todoist_android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todoist_android.data.network.repository.BaseRepo
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.ui.auth.AuthenticationViewModel

//Responsible for giving us all the view models that are required by our app
class ViewModelGenerator(
    private val repository: BaseRepo
    ): ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(repository as AuthRepo) as T

                //region: whenever we want a new viewmodel instance we will define it here

                //endregion

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
}