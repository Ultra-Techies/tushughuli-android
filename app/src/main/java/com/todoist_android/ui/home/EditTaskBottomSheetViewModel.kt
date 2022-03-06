package com.todoist_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.repository.TasksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class EditTaskBottomSheetViewModel@Inject constructor(private val tasksRepo: TasksRepo) :ViewModel(){

    fun editTasks(editTasksRequest: TodoModel) = flow{
        val response= tasksRepo.editTasks(editTasksRequest)
        emit(response)
    }.shareIn(viewModelScope, SharingStarted.Lazily)

}