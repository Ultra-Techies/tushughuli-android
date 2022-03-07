package com.todoist_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.responses.TasksResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TasksRepo
) : ViewModel() {

    private val _task: MutableLiveData<APIResource<TasksResponse>> = MutableLiveData()
    val task: LiveData<APIResource<TasksResponse>>
        get() = _task

    fun getTasks(id: String) = viewModelScope.launch {
        _task.value = APIResource.Loading
        _task.value = repository.getTasks(id)
    }

}