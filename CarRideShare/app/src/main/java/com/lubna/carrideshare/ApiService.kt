package com.lubna.carrideshare

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("users")
    suspend fun registerUser(@Body userData: User): Response<UserResponse>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("rides")
    suspend fun createRide(@Body rideRequest: RideRequest): Response<RideResponse>

    @GET("rides")
    suspend fun getRides(): Response<List<RideResponse>>
}

