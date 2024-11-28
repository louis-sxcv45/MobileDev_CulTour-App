package com.example.cultourapp.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: Any? = null
)

data class User(

	@field:SerializedName("displayName")
	val displayName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Data(

	@field:SerializedName("expiresIn")
	val expiresIn: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)
