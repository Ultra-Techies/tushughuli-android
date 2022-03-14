package com.todoist_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskBottomSheetViewModel@Inject constructor(private val tasksRepo: TasksRepo) :ViewModel(){

    private val _editTaskObserver = MutableStateFlow<APIResource<TodoModel>>(APIResource.Loading)
    val editTasksObserver =_editTaskObserver.asStateFlow()

    private val _deleteTaskObserver = MutableStateFlow<APIResource<TodoModel>>(APIResource.Loading)
    val deleteTaskObserver = _deleteTaskObserver.asStateFlow()

//    fun editTasks(editTasksRequest:TodoModel) = flow{
//        val response= tasksRepo.editTasks(editTasksRequest)
//        emit(response)
//    }.shareIn(viewModelScope, SharingStarted.Lazily)

    fun editTasks(editTasksRequest:TodoModel){
        viewModelScope.launch {
            _editTaskObserver.emit(APIResource.Loading)
            val response = tasksRepo.editTasks(editTasksRequest)
            _editTaskObserver.emit(response)
        }
    }

//    fun deleteTasks(deleteTaskRequest: TodoModel) = flow{
//        val response = tasksRepo.deleteTasks(deleteTaskRequest)
//        emit(response)
//    }.shareIn(viewModelScope, SharingStarted.Lazily)

    fun deleteTasks(deleteTasksRequest:TodoModel){
        viewModelScope.launch {
            _deleteTaskObserver.emit(APIResource.Loading)
            val response = tasksRepo.deleteTasks(deleteTasksRequest)
            _deleteTaskObserver.emit(response)
        }
    }

}