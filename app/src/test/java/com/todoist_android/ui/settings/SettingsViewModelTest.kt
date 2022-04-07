package com.todoist_android.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.models.UserModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserRepo
import com.todoist_android.data.responses.UserDeleteResponse
import com.todoist_android.data.responses.UserResponse
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RunWith(JUnit4::class)
class SettingsViewModelTest {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var repository: UserRepo

    @get:Rule
    val instantiationException = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repository = mockk()
        viewModel = SettingsViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getUser() = runTest {
        val testUserResponse = UserResponse(
            id = 1,
            username = "malcolmmaima",
            name = "Malcolm Maima",
            email = "malcolm@email.com",
            photo = "https://placeimg.com/640/480/any.jpg"
        )
        //when
        coEvery { repository.getUser("1") } returns APIResource.Success(testUserResponse);

        //expect
        viewModel.user.observeForever {
            assertEquals(testUserResponse, it)
        }
    }

    @Test
    fun getUserError() = runTest{
        //when
        coEvery { repository.deleteUser("1") } returns APIResource.Error(isNetworkError = false, errorCode = 500,
            errorBody = null);

        //expect
        viewModel.userDelete.observeForever {
            assertNull(it)
        }
    }

    @Test
    fun userDelete() = runTest {
        val deletedUserResponse = UserDeleteResponse(
            message = "deleted successfully!")

        //when
        coEvery { repository.deleteUser("1") } returns APIResource.Success(deletedUserResponse);

        //expect
        viewModel.userDelete.observeForever {
            assertEquals(deletedUserResponse, it)
        }
    }

//    @ExperimentalTime
//    @Test
//    fun getEditUserResponse() = runTest {
//        val updateUserTestData = UserModel(
//            id = "1",
//            username = "malcolmmaima",
//            email = "malcolm@email.com",
//            photo = "https://placeimg.com/640/480/any.jpg"
//        )
//
//        // when
//        coEvery { repository.updateUser(any()) } returns APIResource.Success(updateUserTestData);
//
//        val updateUserTestRequest = UserModel(
//            id = "1",
//            username = "malcolmmaima",
//            email = "malcolm@email.com",
//            photo = "https://placeimg.com/640/480/any.jpg",
//            password = "12345"
//        )
//        //Expect
//        viewModel.editUserResponse.test(timeout = 5.seconds) {
//            viewModel.updateUser(updateUserTestRequest)
//            assertThat(awaitItem()).isEqualTo(
//                updateUserTestData
//            )
//            coVerify { repository.updateUser(any()) }
//            cancelAndConsumeRemainingEvents()
//        }
//    }

    @Test
    fun updateUser() {
    }

    @Test
    fun saveNotificationState() {
    }
}