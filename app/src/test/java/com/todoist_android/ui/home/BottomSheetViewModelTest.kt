package com.todoist_android.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
        repo = mockk()

        viewModel = BottomSheetViewModel(repo);
    }


    @Test()
    fun addTasks_returnsSuccess() = runTest {
        // give
        val testData = AddTasksResponse(
            title = "test",
            description = "test",
            status = "created",
            dueDate = "12/12/2022",
            reminder = "11/12/2022",
            id = 2
        );

        // when
        coEvery { repo.addTasks(any(), any()) } returns APIResource.Success(testData);

        // expect
        viewModel.addTasksResponse.test {
            viewModel.addTasks(
                1,
                taskRequest = AddTaskRequest(
                    id = null,
                    title = null,
                    description = null,
                    dueDate = null,
                    reminder = null,
                    status = null,
                    createdTime = null
                )
            );
            assertThat(awaitItem()).isEqualTo(
                testData
            )
            coVerify { repo.addTasks(any(), any()) }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun addTasks_returnsError() = runTest {
        // when
        coEvery { repo.addTasks(any(), any()) } returns
                APIResource.Error(
                    isNetworkError = false, errorCode = 401,
                    errorBody = null
                )
        // expect
        viewModel.errorResponse.test {
            viewModel.addTasks(
                1,
                taskRequest = AddTaskRequest(
                    id = null,
                    title = null,
                    description = null,
                    dueDate = null,
                    reminder = null,
                    status = null,
                    createdTime = null
                )
            );

            assertThat(awaitItem()).isEqualTo("Unauthorized request")
            coVerify { repo.addTasks(any(), any()) }
            cancelAndConsumeRemainingEvents()
        }
    }

}