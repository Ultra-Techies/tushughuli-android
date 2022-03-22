package com.todoist_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.EditTaskResponse
import com.todoist_android.ui.parseErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskBottomSheetViewModel@Inject constructor(private val tasksRepo: TasksRepo) :ViewModel(){

    private val _editResponse= MutableSharedFlow<EditTaskResponse>()
    val editResponse =_editResponse.asSharedFlow()

    private val _errorResponse = MutableSharedFlow<String>()
    val errorResponse = _errorResponse.asSharedFlow()

    private val _deleteResponse = MutableSharedFlow<String>()
    val deleteResponse = _deleteResponse.asSharedFlow()

    private val _deleteErrorResponse = MutableSharedFlow<String>()
    val deleteErrorResponse = _deleteErrorResponse.asSharedFlow()

    fun editTasks(id: Int,editTaskRequest: EditTaskRequest){
        viewModelScope.launch {
          val editResponse =  tasksRepo.editTasks(id,editTaskRequest)
            when(editResponse){
                is APIResource.Success -> {
                    _editResponse.emit(editResponse.value)
                }
                is APIResource.Loading ->{

                }
                is APIResource.Error->{
                    _errorResponse.emit(parseErrors(editResponse))
                }
            }
        }
    }


    fun deleteTasks(id: Int){
        viewModelScope.launch {
          val  deleteResponse = tasksRepo.deleteTasks(id)
            when(deleteResponse){
                is APIResource.Success -> {
                    _deleteResponse.emit(deleteResponse.value)
                }
                is APIResource.Loading ->{

                }
                is APIResource.Error->{
                    _deleteErrorResponse.emit(parseErrors(deleteResponse))
                }
            }
        }
    }

}