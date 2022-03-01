package com.todoist_android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.data.responses.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    /**
     * our viewmodel will communicate with our repository
     * we will use the repository to get the data from the server
     * and then we will use the data to update the UI
     ***/

    // use kotlin flows instead of live data
    private val _loginResponse = MutableSharedFlow<APIResource<LoginResponse>>()

    val loginResponse: SharedFlow<APIResource<LoginResponse>>
        get() = _loginResponse

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResponse.emit(authRepo.login(email, password))
    }
}