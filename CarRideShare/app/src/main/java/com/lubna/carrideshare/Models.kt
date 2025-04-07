package com.lubna.carrideshare

data class User(val email: String, val fullName: String, val phoneNumber: String, val password: String)
data class UserResponse(val message: String) // Adjust based on the API response

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: String, val email: String, val name: String) // Adjust fields based on your API
