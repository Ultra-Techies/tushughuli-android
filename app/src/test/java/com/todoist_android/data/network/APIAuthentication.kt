//package com.todoist_android.data.network
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.google.common.truth.Truth.assertThat
//import com.google.gson.Gson
//import com.todoist_android.data.requests.SignUpRequest
//import com.todoist_android.ui.BASE_URL
//import kotlinx.coroutines.runBlocking
//import okhttp3.mockwebserver.MockResponse
//import okhttp3.mockwebserver.MockWebServer
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//@RunWith( JUnit4::class)
//class APIAuthenticationTestsUsingMockWebServers {
//
//    var server = MockWebServer()
//
//    private lateinit var apiAuthentication: APIAuthentication
//
//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(server.url("http://192.168.0.106:8080/"))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Rule
//    @JvmField
//    val instantExecutor = InstantTaskExecutorRule()
//
//    @Before
//    fun setUp() {
//        server.start()
//        apiAuthentication  = retrofit.create(APIAuthentication::class.java)
//    }
//
//
//    private val testJson = """{
//        "username":"pam",
//        "name":"Maridfam",
//        "email":"pam@gmail.com",
//        "photo":"https://pixabay.com/photos/chafdin-security-metal-iron-3481377/",
//        "password":"1234
//    }"""
//    private val expectedResponse =
//      """  {
//            "id": 26,
//            "username": "pam",
//            "name": "Maridfam",
//            "email": "pam@gmail.com",
//            "photo": "https://pixabay.com/photos/chafdin-security-metal-iron-3481377/"
//        }"""
//
//
//    @Test
//    fun `getSignUp returns a user response object`() = runBlocking {
//        server.enqueue(
//            MockResponse()
//                .setBody(testJson)
//                .setResponseCode(200)
//        )
//        val test = apiAuthentication.signup(signUpRequest = SignUpRequest(
//            email = "",
//            name = "",
//            password = "",
//            photo = "",
//            username = ""
//
//
//        ))
//        assertThat(Gson().toJson(test)).isEqualTo(testJson)
//        assertThat("api/user").isEqualTo(server.takeRequest().path)
//    }
//
//
//    @After
//    fun tearDown() {
//        server.shutdown()
//    }
//
//}