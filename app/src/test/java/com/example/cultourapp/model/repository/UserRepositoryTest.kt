package com.example.cultourapp.model.repository

import com.example.cultourapp.model.ApiService
import com.example.cultourapp.model.pref.UserPreferences
import com.example.cultourapp.model.response.LoginResponse
import com.example.cultourapp.model.response.RegisterResponse
import com.example.cultourapp.model.response.UserData
import com.example.cultourapp.model.pref.UserModel
import com.example.cultourapp.model.response.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.RobolectricTestRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userRepository = UserRepository(apiService, userPreferences)
    }

    @Test
    fun registerUser_shouldReturnSuccessfulResponse() {
        val userData = mapOf("email" to "test@example.com", "password" to "password123")
        val mockCall = mock(Call::class.java) as Call<RegisterResponse>
        val response = RegisterResponse(success = true, data = null, message = "Registration successful")

        `when`(apiService.registerUser(userData)).thenReturn(mockCall)

        doAnswer { invocation ->
            val callback: Callback<RegisterResponse> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(any())

        var actualResponse: RegisterResponse? = null
        userRepository.registerUser(userData) { actualResponse = it }

        assertNotNull(actualResponse)
        assertTrue(actualResponse?.success == true)
        assertEquals("Registration successful", actualResponse?.message)
    }

    @Test
    fun loginUser_shouldSaveUserSessionAndReturnSuccessfulResponse() {
        val userData = mapOf("email" to "test@example.com", "password" to "password123")
        val mockCall = mock(Call::class.java) as Call<LoginResponse>
        val response = LoginResponse(
            success = true,
            data = UserData(
                expiresIn = "3600",
                user = User(email = "test@example.com", displayName = "Test User"),
                token = "dummy_token",
                refreshToken = "dummy_refresh_token"
            ),
            message = "Login successful"
        )


        `when`(apiService.loginUser(userData)).thenReturn(mockCall)

        doAnswer { invocation ->
            val callback: Callback<LoginResponse> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(any())

        var actualResponse: LoginResponse? = null
        userRepository.loginUser(userData) { actualResponse = it }

        assertNotNull(actualResponse)
        assertTrue(actualResponse?.success == true)
        assertEquals("Login successful", actualResponse?.message)

        verify(userPreferences).saveUserSession(response.data!!.user, response.data!!.token)
    }

    @Test
    fun saveUserSession_shouldSaveUserDataCorrectly() {
        // Membuat objek User dan UserData yang benar
        val user = User(email = "test@example.com", displayName = "Test User")
        val userData = UserData(
            expiresIn = "3600",
            user = user,
            token = "dummy_token",
            refreshToken = "dummy_refresh_token"
        )

        // Panggil metode saveUserSession pada userRepository
        userRepository.saveUserSession(userData)

        // Verifikasi bahwa saveUserSession di userPreferences dipanggil dengan parameter yang benar
        verify(userPreferences).saveUserSession(user, "dummy_token")
    }

    @Test
    fun getSession_shouldReturnCorrectUserSession() {
        val userModel = UserModel("test@example.com", "dummy_token")
        `when`(userPreferences.getSession()).thenReturn(userModel)

        val session = userRepository.getSession()

        assertEquals("test@example.com", session.email)
        assertEquals("dummy_token", session.token)
    }

    @Test
    fun clearUserSession_shouldClearUserDataCorrectly() {
        userRepository.clearUserSession()

        verify(userPreferences).clearSession()
    }
}