package com.example.matchifyandroid.network

import retrofit2.Response

class AuthRepository {

    private val api = RetrofitInstance.api

    // 🔹 Login
    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    // 🔹 Signup
    suspend fun signup(name: String, email: String, password: String, role: String) =
        api.signup(SignupRequest(name, email, password, role))

    // ✅ Étape 1 : Envoyer un email pour reset password
    suspend fun forgotPassword(email: String): Response<ForgotPasswordResponse> {
        return api.forgotPassword(ForgotPasswordRequest(email))
    }

    // ✅ Étape 2 : Mettre à jour le mot de passe avec token
    suspend fun resetPassword(token: String, newPassword: String): Response<ResetPasswordResponse> {
        return api.resetPassword(ResetPasswordRequest(token, newPassword))
    }
}
