package com.todoist_android.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.TasksRepo
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.EditTaskResponse
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
class EditTaskBottomSheetViewModelTest {
    lateinit var viewModel: EditTaskBottomSheetViewModel
    private lateinit var repo: TasksRepo

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repo = mockk()

        viewModel = EditTaskBottomSheetViewModel(repo)
    }



    @Test
    fun editTasks_returnsSuccess() = runTest {
        //give
        val testData = EditTaskResponse(
            title = "test",
            description = "test",
            status = "created",
            dueDate = "12/12/2022",
            reminder = "12/12/2022",
            id = 1,
        )

        coEvery { repo.editTasks(any(),any()) }returns APIResource.Success(testData)
    //expect
        viewModel.editResponse.test {
            viewModel.editTasks(
                1,
                editTaskRequest = EditTaskRequest(
                    description = null,
                    dueDate = "",
                    reminder = "",
                    createdTime = "",
                    status = "",
                    title = ""

                )
            )
            assertThat(awaitItem()).isEqualTo(testData)

            coVerify { repo.editTasks(any(),any()) }
            cancelAndConsumeRemainingEvents()
        }


    }

    @Test
    fun editTasks_returnsError()= runTest {
        //when
        coEvery { repo.editTasks(any(),any()) }returns APIResource.Error(isNetworkError = false,errorCode = 401,errorBody = null)

        //expect
        viewModel.errorResponse.test {
            viewModel.editTasks(1, editTaskRequest = EditTaskRequest(
                description = "",
                dueDate = "",
                reminder = "",
                createdTime = "",
                status = "",
                title = ""
            )
            )

            assertThat(awaitItem()).isEqualTo("Unauthorized request")
            coVerify { repo.editTasks(any(),any()) }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun deleteTask_returnsSuccess() = runTest {

        //
        coEvery { repo.deleteTasks(any()) } returns APIResource.Success("message deleted")

        //expect
        viewModel.deleteResponse.test {
            viewModel.deleteTasks(1)

            assertThat(awaitItem()).isEqualTo("message deleted")

            coVerify { repo.deleteTasks(any()) }

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun deleteTask_returnError()= runTest {

        coEvery { repo.deleteTasks(any()) } returns APIResource.Error(isNetworkError = false,errorCode = 404,errorBody = null)

        viewModel.deleteErrorResponse.test {
            viewModel.deleteTasks(1)

            assertThat(awaitItem()).isEqualTo("Resource not found")
            coVerify { repo.deleteTasks(any()) }
            cancelAndConsumeRemainingEvents()
        }

    }


//    @Test
//    fun `init deleteTasks`() {
//        assertThat(viewModel.deleteTaskObserver.value).isEqualTo(APIResource.Loading)
//    }
//
//    @OptIn(ExperimentalTime::class)
//    @Test
//    fun `delete tasks returns success`() = runTest {
//        val task = TodoModel(
//            title = "test",
//            description = "test",
//            status = "created",
//            due_date = "12/12/2022",
//            id = "1"
//        )
//        viewModel.deleteTaskObserver.test {
//            viewModel.deleteTasks(
//                TodoModel(
//                title = "test",
//                description = "test",
//                status = "created",
//                due_date = "12/12/2022",
//                id = "1"
//            ))
//            assertThat(awaitItem()).isEqualTo(APIResource.Loading)
//            assertThat(awaitItem()).isEqualTo(APIResource.Success(task))
//            cancelAndConsumeRemainingEvents()
//        }
//    }
}