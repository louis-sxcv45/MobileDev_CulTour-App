package com.example.cultourapp.model.response

import com.google.gson.annotations.SerializedName

data class ChatbotResponse(

	@field:SerializedName("chatbot_response")
	val chatbotResponse: String,

	@field:SerializedName("cultural_norm")
	val culturalNorm: String,

	@field:SerializedName("weather_summary")
	val weatherSummary: String,

	@field:SerializedName("plot_url")
	val plotUrl: String
)
