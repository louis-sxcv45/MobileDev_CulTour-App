package com.example.cultourapp.model.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
)
