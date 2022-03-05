package com.todoist_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TaskRepo
import com.todoist_android.data.repository.UserRepo
import com.todoist_android.data.responses.TaskResponse
import com.todoist_android.data.responses.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepo
) : ViewModel() {

    private val _task: MutableLiveData<APIResource<TaskResponse>> = MutableLiveData()
    val task: LiveData<APIResource<TaskResponse>>
        get() = _task

    fun getTasks(id: String) = viewModelScope.launch {
        _task.value = APIResource.Loading
        _task.value = repository.getTasks(id)
    }

}