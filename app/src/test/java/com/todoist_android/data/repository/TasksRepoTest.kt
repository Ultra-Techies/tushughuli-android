package com.todoist_android.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class TasksRepoTest {

    lateinit var tasksRepo: TasksRepo

    @Mock
    lateinit var taskApi: TaskApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        tasksRepo = TasksRepoImpl(taskApi)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addTasks() = runTest {
        val testResponse = AddTasksResponse(
            status = "created",
            description = "sanple desc",
            title = "Sample title",
            id = 1,
            due_date = "12/12/2022"
        )

        Mockito.`when`(taskApi.addTasks(AddTaskRequest())).thenReturn(
            testResponse
        )

        val res = tasksRepo.addTasks(AddTaskRequest())
        Truth.assertThat(res).isEqualTo(APIResource.Success(testResponse))
    }

    @Test
    fun addTasks_throwsException() = runTest {
        given(taskApi.addTasks(AddTaskRequest())).willAnswer {
            throw Exception("Ooops")
        }

        val res = tasksRepo.addTasks(AddTaskRequest(id = "2"))
        Truth.assertThat(res).isEqualTo(APIResource.Error(true, null, null))
    }
}