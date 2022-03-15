package com.todoist_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.AddTaskRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val tasksRepo: TasksRepo) : ViewModel() {

    fun addTasks(id:Int,taskRequest: AddTaskRequest) = flow {
        val response = tasksRepo.addTasks(id,taskRequest)
        emit(response)
    }.shareIn(viewModelScope, SharingStarted.Lazily)


}