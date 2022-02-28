package com.todoist_android.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.data.responses.LoginResponse
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authRepo: AuthRepo
) : ViewModel() {

    /**
     * our viewmodel will communicate with our repository
     * we will use the repository to get the data from the server
     * and then we will use the data to update the UI
     ***/

    private val _loginResponse : MutableLiveData<APIResource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<APIResource<LoginResponse>>
        get() = _loginResponse

    fun login(email: String, password: String)  = viewModelScope.launch {
        _loginResponse.value = authRepo.login(email, password)
    }
}