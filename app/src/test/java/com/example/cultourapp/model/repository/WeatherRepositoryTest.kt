package com.example.cultourapp.model.repository

import com.example.cultourapp.model.WeatherApiService
import com.example.cultourapp.model.response.ChatbotResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.RobolectricTestRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class WeatherRepositoryTest {

    @Mock
    private lateinit var weatherApiService: WeatherApiService

    @Mock
    private lateinit var call: Call<ChatbotResponse>

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherRepository = WeatherRepository(weatherApiService)
    }

    @Test
    fun `getChatbot should return successful response`() {
        val data = mapOf("key" to "value")
        val response = ChatbotResponse(
            chatbotResponse = "Sample Response",
            culturalNorm = "Sample Cultural Norm",
            plotUrl = "http://example.com/plot",
            weatherSummary = "Clear skies"
        )
        val responseCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<ChatbotResponse>>

        `when`(weatherApiService.getChatbot(data)).thenReturn(call)

        weatherRepository.getChatbot(data) { result ->
            assertTrue(result.isSuccess)
            assertEquals(response, result.getOrNull())
        }

        verify(call).enqueue(responseCaptor.capture())
        responseCaptor.value.onResponse(call, Response.success(response))
    }

    @Test
    fun `getChatbot should return failure response`() {
        val data = mapOf("key" to "value")
        val errorResponseJson = """{"error":"Error message"}"""
        val errorResponseBody = okhttp3.ResponseBody.create(null, errorResponseJson)
        val responseCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<ChatbotResponse>>

        `when`(weatherApiService.getChatbot(data)).thenReturn(call)

        weatherRepository.getChatbot(data) { result ->
            assertTrue(result.isFailure)
            assertEquals("Error message", result.exceptionOrNull()?.message)
        }

        verify(call).enqueue(responseCaptor.capture())
        responseCaptor.value.onResponse(call, Response.error(400, errorResponseBody))
    }

    @Test
    fun `getChatbot should handle failure`() {
        val data = mapOf("key" to "value")
        val throwable = Throwable("Network error")
        val responseCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<ChatbotResponse>>

        `when`(weatherApiService.getChatbot(data)).thenReturn(call)

        weatherRepository.getChatbot(data) { result ->
            assertTrue(result.isFailure)
            assertEquals(throwable, result.exceptionOrNull())
        }

        verify(call).enqueue(responseCaptor.capture())
        responseCaptor.value.onFailure(call, throwable)
    }
}