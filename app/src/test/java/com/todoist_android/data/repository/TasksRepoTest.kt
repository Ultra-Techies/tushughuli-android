package com.todoist_android.data.repository

import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.network.TaskApi
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.AddTasksResponse
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
        val response = AddTasksResponse(
            id = 1,
            title = "",
            description = "",
            reminder = "", dueDate = "",
            status = ""

        )
        // when
        coEvery { taskApi.addTasks(any(),1) } returns response

        // expect
        val apiResponse = tasksRepo.addTasks(id = 1,AddTaskRequest())

        assertThat(apiResponse).isEqualTo(APIResource.Success(response))
        coVerify {
            taskApi.addTasks(any(), 1)
        }
    }




    @Test
    fun `add task, return fail`() = runTest {
        // when
        coEvery { taskApi.addTasks(any(),1) } throws Exception("Something went wrong")

        // expect
        val apiResponse = tasksRepo.addTasks(id = 1,AddTaskRequest())

        assertThat(apiResponse).isEqualTo(APIResource.Error(true, null, null))
        coVerify {
            taskApi.addTasks(any(),1)
        }
    }

    @Test
    fun `edit task, return success`() = runTest{
        val editTasksRequest = EditTaskResponse(
            title = "now",
            description = "now now",
            dueDate = "2022-04-11 14:10:00",
            id = 2,
            reminder = "2022-04-11 14:10:00",
            status = "progress"

        )
        coEvery { taskApi.editTasks(any(),any()) }returns editTasksRequest

        val apiResponse =tasksRepo.editTasks(id = 1, EditTaskRequest(
            title = "go home",
            description = "use a car",
            dueDate = "2022-04-11 14:10;00",
            status = "done",
            createdTime = "2022-04-10 10:00:00",
            reminder = "2022-04-10 10:00:00"
        )
        )
        assertThat(apiResponse).isEqualTo(APIResource.Success(editTasksRequest))

        coVerify {
            taskApi.editTasks(any(),any())
        }
    }

    @Test
    fun `edit task, returns Error`()= runTest{

        coEvery { taskApi.editTasks(any(),any()) }throws Exception("Something went wrong")

        val apiResponse = tasksRepo.editTasks(id = 1,
            EditTaskRequest(
                description = "",
                dueDate = "",
                reminder = "",
                createdTime = "",
                status = "",
                title = ""

            )
        )
        assertThat(apiResponse).isEqualTo(APIResource.Error(true,null,null))

        coVerify { taskApi.editTasks(any(),any()) }

    }

    @Test
    fun`deleteTasks, returns Success`() = runTest {

        coEvery { taskApi.deleteTasks(any()) }returns String()

        val apiResponse = tasksRepo.deleteTasks(id = 1
        )
        assertThat(apiResponse).isEqualTo(APIResource.Success(String()))

        coVerify { taskApi.deleteTasks(any()) }

    }

    @Test
    fun `deleteTasks, returns error`()= runTest {

        coEvery { taskApi.deleteTasks(any()) }throws Exception("Something went wrong")

        val apiResponse = tasksRepo.deleteTasks(id=1)
        assertThat(apiResponse).isEqualTo(APIResource.Error(true,null,null))

        coVerify {
           taskApi.deleteTasks(any())
        }


    }



}