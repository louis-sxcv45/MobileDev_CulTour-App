package com.example.cultourapp.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: UserData?,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class User(

	@field:SerializedName("displayName")
	val displayName: String,

	@field:SerializedName("email")
	val email: String
)

data class UserData(

	@field:SerializedName("expiresIn")
	val expiresIn: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)
