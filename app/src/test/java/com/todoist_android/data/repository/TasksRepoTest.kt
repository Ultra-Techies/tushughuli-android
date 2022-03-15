package com.todoist_android.data.repository

import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.network.TaskApi
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



@RunWith(JUnit4::class)
class TasksRepoTest {

    lateinit var tasksRepo: TasksRepo
    lateinit var taskApi: TaskApi

    @Before
    fun setUp() {
        taskApi = mockk()
        tasksRepo = TasksRepoImpl(taskApi)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `add task, return success`() = runTest {
        // given
        val resp = AddTasksResponse(
            description = "",
            due_date = "",
            id = 0,
            status = "",
            title = ""
        )

        // when
        coEvery { taskApi.addTasks(any()) } returns resp

        // expect
        val apiResponse = tasksRepo.addTasks(AddTaskRequest())

        assertThat(apiResponse).isEqualTo(APIResource.Success(resp))
        coVerify {
            taskApi.addTasks(any())
        }
    }

    @Test
    fun `add task, return fail`() = runTest {
        // when
        coEvery { taskApi.addTasks(any()) } throws Exception("Something went wrong")

        // expect
        val apiResponse = tasksRepo.addTasks(AddTaskRequest())

        assertThat(apiResponse).isEqualTo(APIResource.Error(true, null, null))
        coVerify {
            taskApi.addTasks(any())
        }
    }

    @Test
    fun `edit task, return success`() = runTest{
        val editTasksRequest = TodoModel(
            title = "now",
            description = "now now",
            due_date = "2022-04-11 14:10:00",
            id = "2",
            status = "progress"

        )
        coEvery { taskApi.editTasks(any(),any()) }returns editTasksRequest

        val apiResponse =tasksRepo.editTasks(TodoModel(
            title = null,
            description = null,
            due_date = null,
            id = "2",
            status = null
        ))
        assertThat(apiResponse).isEqualTo(APIResource.Success(editTasksRequest))

        coVerify {
            taskApi.editTasks(any(),any())
        }
    }

    @Test
    fun `edit task, returns Error`()= runTest{

        coEvery { taskApi.editTasks(any(),any()) }throws Exception("Something went wrong")

        val apiResponse = tasksRepo.editTasks(
            TodoModel(
                title = null,
                description = null,
                due_date = null,
                id = "2",
                status = null
            )
        )
        assertThat(apiResponse).isEqualTo(APIResource.Error(true,null,null))

        coVerify { taskApi.editTasks(any(),any()) }

    }

    @Test
    fun`deleteTasks, returns Success`() = runTest {
    val deleteTaskRequest = TodoModel(
        title = null,
        description = null,
        due_date = null,
        id = "2",
        status = null

    )
        coEvery { taskApi.deleteTasks(any(),any()) }returns deleteTaskRequest

        val apiResponse = tasksRepo.deleteTasks(
            TodoModel(
                title = null,
                description = null,
                due_date = null,
                id = "2",
                status = null

            )
        )
        assertThat(apiResponse).isEqualTo(APIResource.Success(deleteTaskRequest))

        coVerify { taskApi.deleteTasks(any(),any()) }

    }

    @Test
    fun `deleteTasks, returns error`()= runTest {

        coEvery { taskApi.deleteTasks(any(),any()) }throws Exception("Something went wrong")

        val apiResponse = tasksRepo.deleteTasks(
            TodoModel(
                title = null,
                description = null,
                due_date = null,
                id = "2",
                status = null

            )
        )
        assertThat(apiResponse).isEqualTo(APIResource.Error(true,null,null))

        coVerify {
           taskApi.deleteTasks(any(),any())
        }


    }



}