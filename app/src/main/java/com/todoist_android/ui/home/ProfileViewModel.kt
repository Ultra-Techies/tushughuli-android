package com.todoist_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserRepo
import com.todoist_android.data.responses.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepo
) : ViewModel() {

    private val _user: MutableLiveData<APIResource<UserResponse>> = MutableLiveData()
    val user: LiveData<APIResource<UserResponse>>
        get() = _user

    fun getUser(id: String) = viewModelScope.launch {
        _user.value = APIResource.Loading
        _user.value = repository.getUser(id)
    }

}