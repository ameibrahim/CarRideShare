package com.lubna.carrideshare

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("users")
    suspend fun registerUser(@Body userData: User): Response<UserResponse>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("rides")
    suspend fun createRide(@Body rideRequest: RideRequest): Response<RideResponse>

    @GET("rides")
    suspend fun getRides(): Response<List<RideResponse>>

    @POST("search")
    suspend fun searchRides(@Body searchRequest: SearchRequest): Response<SearchRidesResponse>

    @GET("rides/{rideId}")
    suspend fun getRideDetails(@Path("rideId") rideId: String): Response<RideDetailsResponse>

    @GET("rides/latest")
    suspend fun getLatestRides(): Response<RidesListResponse>

    @GET("rides/mine/{userId}/latest")
    suspend fun getMyLatestRides(
        @Path("userId") userId: String
    ): Response<RidesListResponse>

    @PUT("rides/{id}")
    suspend fun updateRideStatus(
        @Path("id") rideId: String,
        @Body statusUpdate: Map<String, String>
    ): Response<RideDetailsResponse>

    @DELETE("rides/{id}")
    suspend fun deleteRide(@Path("id") rideId: String): Response<Unit>

    @GET("bookings")
    suspend fun getBookings(): Response<List<BookingResponse>>

    @POST("bookings")
    suspend fun createBooking(
        @Body bookingRequest: BookingRequest
    ): Response<BookingResponse>

    @DELETE("bookings/{id}")
    suspend fun cancelBooking(
        @Path("id") bookingId: String
    ): Response<Unit>
}

