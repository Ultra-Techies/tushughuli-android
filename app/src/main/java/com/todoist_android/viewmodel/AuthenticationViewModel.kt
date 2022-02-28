package com.todoist_android.viewmodel

import androidx.lifecycle.ViewModel
import com.todoist_android.view.auth.repository.AuthRepo

class AuthenticationViewModel(
    private val authRepo: AuthRepo
) : ViewModel() {

    /**
     * our viewmodel will communicate with our repository
     * we will use the repository to get the data from the server
     * and then we will use the data to update the UI
     ***/

}