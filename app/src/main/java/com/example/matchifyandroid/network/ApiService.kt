package com.example.matchifyandroid.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// DTOs pour signup / login
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val access_token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val role: String
)

// ✅ DTO pour "mot de passe oublié"
data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String
)

// ✅ DTO pour "réinitialisation du mot de passe"
data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String
)

interface ApiService {

    // 🔹 Signup
    @POST("auth/signup")
    suspend fun signup(@Body body: SignupRequest): Response<AuthResponse>

    // 🔹 Login
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>
}