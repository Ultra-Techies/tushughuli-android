package com.todoist_android.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.todoist_android.ui.BASE_URL
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith( JUnit4::class)
class APIAuthenticationTestsUsingMockWebServers {

    var server = MockWebServer()

    private lateinit var apiAuthentication: APIAuthentication

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(server.url(BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        server.start()
        apiAuthentication  = retrofit.create(APIAuthentication::class.java)
    }


    private val testJson = """{
        "username":"flo",
        "email":"xyz@gmail.com",
        "password":111
    }"""

    private val expectedResponse = "{\"created\":true,\"id\":123,\"username_valid\":true}"

    @Test
    fun `getSignUp returns a user response object`() = runBlocking {
        server.enqueue(
            MockResponse()
                .setBody(testJson)
                .setResponseCode(200)
        )
        val test = apiAuthentication.signup("flo","xyz@gmail.com","111")
        assertThat(Gson().toJson(test)).isEqualTo(expectedResponse)
        assertThat("/signup").isEqualTo(server.takeRequest().path)
    }


    @After
    fun tearDown() {
        server.shutdown()
    }

}