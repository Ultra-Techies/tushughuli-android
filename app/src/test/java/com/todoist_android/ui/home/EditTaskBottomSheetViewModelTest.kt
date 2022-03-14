package com.todoist_android.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.FakeTasksRepo
import com.todoist_android.data.repository.TasksRepo
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.ExperimentalTime

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
        repo = FakeTasksRepo()
        viewModel = EditTaskBottomSheetViewModel(repo)
    }

    @Test
    fun `init editTasks loading`() {
        assertThat(viewModel.editTasksObserver.value).isEqualTo(APIResource.Loading)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun editTasks_returnsSuccess() = runTest {
        val a = TodoModel(
            title = "test",
            description = "test",
            status = "created",
            due_date = "12/12/2022",
            id = "1"
        )
        viewModel.editTasksObserver.test {
            viewModel.editTasks(
                TodoModel(
                    title = "testing",
                    description = "test 2",
                    status = "progress",
                    due_date = "12/12/2022",
                    id = "1"
            ))
            assertThat(awaitItem()).isEqualTo(APIResource.Loading)
            assertThat(awaitItem()).isEqualTo(APIResource.Success(a))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init deleteTasks`() {
        assertThat(viewModel.deleteTaskObserver.value).isEqualTo(APIResource.Loading)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `delete tasks returns success`() = runTest {
        val task = TodoModel(
            title = "test",
            description = "test",
            status = "created",
            due_date = "12/12/2022",
            id = "1"
        )
        viewModel.deleteTaskObserver.test {
            viewModel.deleteTasks(
                TodoModel(
                title = "test",
                description = "test",
                status = "created",
                due_date = "12/12/2022",
                id = "1"
            ))
            assertThat(awaitItem()).isEqualTo(APIResource.Loading)
            assertThat(awaitItem()).isEqualTo(APIResource.Success(task))
            cancelAndConsumeRemainingEvents()
        }
    }
}