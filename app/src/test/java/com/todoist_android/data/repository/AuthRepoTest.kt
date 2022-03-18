package com.todoist_android.data.repository

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.todoist_android.MainCoroutineRule
import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.requests.LoginRequest
import com.todoist_android.data.responses.LoginResponse
import com.todoist_android.data.responses.SignupResponse
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
class AuthRepoTest {

    lateinit var authRepo: AuthRepo
    lateinit var userPreferences: UserPreferences

    lateinit var authApi: APIAuthentication

    @Before
    fun setUp(){
        authApi = mockk()
        userPreferences = mockk()
        authRepo = AuthRepoImpl(authApi,userPreferences)
    }
    @get:Rule
    var mainCoroutineRule =MainCoroutineRule()

    @Test
    fun `login, returns success`() = runTest {
        val response = LoginResponse(
            email = null,
            id = null,
            name = null,
            photo = null,
            username = null
        )

        coEvery { authApi.login(any()) } returns response

        val apiResponse = authRepo.login(email = "", password = "")

        assertThat(apiResponse).isEqualTo(APIResource.Success(response))

        coVerify { authApi.login(any()) }

    }

    @Test
    fun`login, return error`() = runTest {
        coEvery { authApi.login(any()) } throws Exception("Something went wrong")

        val apiResponse = authRepo.login(email = "ket@gmail.com", password = "1234")

        assertThat(apiResponse).isEqualTo(APIResource.Error(true,null,null))

        coVerify { authApi.login(any()) }
    }

    @Test
    fun `signup, returns success`() = runTest {
    val response = SignupResponse(
        email = "",
        id = 1,
        name = "",
        photo = "",
        username = ""

    )
        coEvery { authApi.signup(any()) }returns response

        val apiResponse = authRepo.signup(
            username = "",
            email = "",
            password = "",
            photo = "",
            name = ""
        )

        assertThat(apiResponse).isEqualTo(APIResource.Success(response))

        coVerify {
            authApi.signup(any())
        }

    }

    @Test
    fun `signup, returns Error`() = runTest {
        coEvery { authApi.signup(any()) } throws Exception("Something went wrong")

        val apiResponse = authRepo.signup(
            username = "",
            email = "",
            password = "",
            photo = "",
            name = ""
        )

        assertThat(apiResponse).isEqualTo(APIResource.Error(isNetworkError = true,null,null))

        coVerify { authApi.signup(any()) }
    }
}