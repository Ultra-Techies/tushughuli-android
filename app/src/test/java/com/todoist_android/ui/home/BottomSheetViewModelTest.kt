package com.todoist_android.ui.home

import android.annotation.SuppressLint
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.FakeTasksRepo
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.ExperimentalTime

@RunWith(JUnit4::class)
class BottomSheetViewModelTest {
    lateinit var viewModel: BottomSheetViewModel
    private lateinit var repo: TasksRepo

    @get:Rule
    val instantiationException = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repo = FakeTasksRepo()
        viewModel = BottomSheetViewModel(repo);
    }

    @Test()
    fun `init method loading`() {
        assertThat(viewModel.addTaskObserver.value).isEqualTo(APIResource.Loading)
    }


    @OptIn(ExperimentalTime::class)
    @Test()
    fun addTasks_returnsSuccess() = runTest {

        val a = AddTasksResponse(
            title = "test",
            description = "test",
            status = "created",
            due_date = "12/12/2022",
            id = 1
        )
        viewModel.addTaskObserver.test {
            viewModel.addTasks(AddTaskRequest())
            assertThat(awaitItem()).isEqualTo(APIResource.Loading)
            assertThat(awaitItem()).isEqualTo(APIResource.Success(a))
            cancelAndConsumeRemainingEvents()
        }
    }

}