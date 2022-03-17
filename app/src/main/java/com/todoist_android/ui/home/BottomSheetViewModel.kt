package com.todoist_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import com.todoist_android.ui.parseErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val tasksRepo: TasksRepo) : ViewModel() {

    private val _addTaskResponse = MutableSharedFlow<AddTasksResponse>()
    val addTasksResponse = _addTaskResponse.asSharedFlow()

    private val _errorResponse = MutableSharedFlow<String>()
    val errorResponse =_errorResponse.asSharedFlow()

    fun addTasks(id: Int,taskRequest: AddTaskRequest) {
    viewModelScope.launch {
        val taskResponse = tasksRepo.addTasks(id,taskRequest)
        when(taskResponse){
            is APIResource.Success ->{
                _addTaskResponse.emit(taskResponse.value)
            }
            is APIResource.Loading ->{

            }
            is APIResource.Error ->{
                _errorResponse.emit(parseErrors(taskResponse))
            }
        }
    }
    }

}