package com.todoist_android.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserRepo
import com.todoist_android.data.responses.SignupResponse
import com.todoist_android.data.responses.UserDeleteResponse
import com.todoist_android.data.responses.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: UserRepo) : ViewModel() {
    private val _user: MutableLiveData<APIResource<UserResponse>> = MutableLiveData()
    private val _userDelete: MutableLiveData<APIResource<UserDeleteResponse>> = MutableLiveData()
    private  val _editUserResponse = MutableSharedFlow<APIResource<UserResponse>>()

    val user: LiveData<APIResource<UserResponse>>
        get() = _user

    val userDelete: LiveData<APIResource<UserDeleteResponse>>
        get() = _userDelete

    fun getUser(id: String) = viewModelScope.launch {
        _user.value = APIResource.Loading
        _user.value = repository.getUser(id)
    }

    fun deleteUser(id: String) = viewModelScope.launch {
        _userDelete.value = APIResource.Loading
        _userDelete.value = repository.deleteUser(id)
    }

    val editUserResponse: SharedFlow<APIResource<UserResponse>> get() = _editUserResponse

    fun updateUser(
        id: String,
        username: String,
        password: String,
        email:String,
        display_name: String,
        profile_photo: String ) = viewModelScope.launch {
            _editUserResponse.emit(
                repository.editUser(
                    id,
                    username,
                    password,
                    email,
                    display_name,
                    profile_photo
                )
            )
    }
}