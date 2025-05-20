package com.lubna.carrideshare

import java.math.BigDecimal

data class User(val email: String, val fullName: String, val phoneNumber: String, val password: String)
data class UserResponse(val message: String) // Adjust based on the API response

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: String, val email: String, val fullName: String) // Adjust fields based on your API

data class RideRequest(
    val createdById: String,
    val startLocation: String,
    val name: String,
    val endLocation: String,
    val taxiCalled: Boolean,
    val taxiArrivalTime: String,
    val cost: BigDecimal,
    val totalSeats: Int,
    val availableSeats: Int
)

data class RideResponse(
    val id: String,
    val status: String,
    val name: String,
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

data class BookingResponse (
    val id: String,
    val status: String,
)

data class RidesListResponse(
    val rides: List<RideItem>
)

data class RideItem(
    val id: String,
    val name: String,
    val taxiArrivalTime: String
)

data class RideDetailsResponse(
    val id: String,
    val name: String,
    val createdById: String,
    val startLocation: String,
    val endLocation: String,
    val taxiCalled: Boolean,
    val taxiArrivalTime: String,
    val cost: String,
    val totalSeats: Int,
    val availableSeats: Int,
    val status: String,
    val createdAt: String,   // You can parse as Date if needed with adapters
    val updatedAt: String,
    val createdBy: CreatedBy,
    val bookings: List<Booking>
)

data class CreatedBy(
    val fullName: String,
    val email: String
)

data class Booking(
    val id: String,
    val rideId: String,
    val userId: String,
    val costPerPassenger: String,
    val createdAt: String,
    val user: User
)

data class SearchRequest(
    val search: String
)

data class SearchRidesResponse(
    val results: List<RideResponse>
)

data class BookingRequest(
    val rideId: String,
    val userId: String,
    val costPerPassenger: String
)
