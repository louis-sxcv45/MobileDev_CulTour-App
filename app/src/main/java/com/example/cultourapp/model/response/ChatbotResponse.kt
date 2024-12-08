package com.example.cultourapp.model.response

import com.google.gson.annotations.SerializedName

data class ChatbotResponse(

	@field:SerializedName("chatbot_response")
	val chatbotResponse: String? = null,

	@field:SerializedName("cultural_norm")
	val culturalNorm: String? = null,

	@field:SerializedName("weather_summary")
	val weatherSummary: String? = null,

	@field:SerializedName("plot_url")
	val plotUrl: String? = null
)
