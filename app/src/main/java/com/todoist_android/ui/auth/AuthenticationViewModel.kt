package com.todoist_android.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import com.todoist_android.data.responses.User
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
    private val _signUpResponse : MutableLiveData<APIResource<SignupResponse>> = MutableLiveData()

    val loginResponse: LiveData<APIResource<LoginResponse>>
        get() = _loginResponse

    val signUpResponse: LiveData<APIResource<SignupResponse>>
        get() = _signUpResponse

    fun login(email: String, password: String)  = viewModelScope.launch {
        _loginResponse.value = authRepo.login(email, password)
    }

    fun signup(username: String, email: String, password: String) = viewModelScope.launch {
        _signUpResponse.value = authRepo.signup(username, email, password)
    }
}