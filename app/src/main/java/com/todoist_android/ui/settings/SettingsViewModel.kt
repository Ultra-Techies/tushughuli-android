package com.todoist_android.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.models.UserModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserRepo
import com.todoist_android.data.requests.UpdateUserRequest
import com.todoist_android.data.responses.UserDeleteResponse
import com.todoist_android.data.responses.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: UserRepo) : ViewModel() {
    private val _user: MutableLiveData<APIResource<UserResponse>> = MutableLiveData()
    private val _userDelete: MutableLiveData<APIResource<UserDeleteResponse>> = MutableLiveData()
    private  val _editUserResponse = MutableSharedFlow<APIResource<UserModel>>()
    private val _userDeleteTasks: MutableLiveData<APIResource<UserDeleteResponse>> = MutableLiveData()

    val user: LiveData<APIResource<UserResponse>>
        get() = _user

    val userDelete: LiveData<APIResource<UserDeleteResponse>>
        get() = _userDelete

    val userDeleteTasks: LiveData<APIResource<UserDeleteResponse>>
        get() = _userDeleteTasks

    fun getUser(id: String) = viewModelScope.launch {
        _user.value = APIResource.Loading
        _user.value = repository.getUser(id)
    }

    fun deleteUser(id: String) = viewModelScope.launch {
        _userDelete.value = APIResource.Loading
        _userDelete.value = repository.deleteUser(id)
    }

    fun deleteAllTasks(id: String) = viewModelScope.launch {
        _userDeleteTasks.value = APIResource.Loading
        _userDeleteTasks.value = repository.deleteAllTasks(id)
    }

    val editUserResponse: SharedFlow<APIResource<UserModel>> get() = _editUserResponse

    fun updateUser(updateUserRequest: UserModel) = flow{
        val response= repository.updateUser(updateUserRequest)
        emit(response)
    }.shareIn(viewModelScope, SharingStarted.Lazily)

    fun saveNotificationState(state: Boolean) = viewModelScope.launch {
        repository.saveNotificationState(state)
    }
}