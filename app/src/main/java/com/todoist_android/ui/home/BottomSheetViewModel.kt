package com.todoist_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val tasksRepo: TasksRepo) : ViewModel() {

    private val _addTaskObserver =
        MutableStateFlow<APIResource<AddTasksResponse>>(APIResource.Loading);
    val addTaskObserver = _addTaskObserver.asStateFlow()

    fun addTasks(id: Int, taskRequest: AddTaskRequest) {
        viewModelScope.launch {
            _addTaskObserver.emit(APIResource.Loading)
            val response = tasksRepo.addTasks(id, taskRequest)
            _addTaskObserver.emit(response);
        }
    }


}