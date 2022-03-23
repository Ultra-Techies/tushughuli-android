package com.todoist_android.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserRepo
import com.todoist_android.data.responses.UserResponse
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.ExperimentalTime

import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalTime::class)
@RunWith(JUnit4::class)
class ProfileViewModelTest {
    private lateinit var repository: UserRepo
    lateinit var viewModel: ProfileViewModel

    @get:Rule
    val instantiationException = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repository = mockk()
        viewModel = ProfileViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getUser() {
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
}