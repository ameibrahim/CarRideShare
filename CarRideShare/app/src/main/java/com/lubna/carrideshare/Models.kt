package com.lubna.carrideshare

data class User(val email: String, val fullName: String, val phoneNumber: String, val password: String)
data class UserResponse(val message: String) // Adjust based on the API response

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: String, val email: String, val name: String) // Adjust fields based on your API

data class RideRequest(
    val createdById: String,
    val startLocation: String,
    val endLocation: String,
    val taxiCalled: Boolean,
    val taxiArrivalTime: String,
    val cost: String,
    val totalSeats: String,
    val availableSeats: String
)

data class RideResponse(
    val id: String,
    val status: String,
    val createdById: String,
    val startLocation: String,
    val endLocation: String,
    val taxiCalled: Boolean,
    val taxiArrivalTime: String,
    val cost: String,
    val totalSeats: Int,
    val availableSeats: Int,
    val createdAt: String,
    val updatedAt: String
)