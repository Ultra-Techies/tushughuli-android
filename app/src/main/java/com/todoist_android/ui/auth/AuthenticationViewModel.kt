package com.todoist_android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    /**
     * our viewmodel will communicate with our repository
     * we will use the repository to get the data from the server
     * and then we will use the data to update the UI
     ***/

    // use kotlin flows instead of live data
    private val _loginResponse = MutableSharedFlow<APIResource<LoginResponse>>()

    private val _signupResponse = MutableSharedFlow<APIResource<SignupResponse>>()

    val loginResponse: SharedFlow<APIResource<LoginResponse>>
        get() = _loginResponse


    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResponse.emit(authRepo.login(email, password))
    }

    val signupResponse: SharedFlow<APIResource<SignupResponse>> get() = _signupResponse

    fun signUp(username: String, name: String, email: String, photo: String, password: String) =
        viewModelScope.launch {
            _signupResponse.emit(
                authRepo.signup(
                    username = username,
                    name = name,
                    email = email,
                    photo = photo,
                    password = password
                )
            )
        }

    fun saveAuthToken(token: String) = viewModelScope.launch {
        authRepo.saveToken(token)
    }
}