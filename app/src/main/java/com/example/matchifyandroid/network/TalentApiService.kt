package com.example.matchifyandroid.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/* ✅ Structure renvoyée par ton TalentController */
data class TalentProfileResponse(
    val id: String,
    val name: String?,
    val email: String,
    val role: String,
    val phone: String?,
    val bio: String?,                  // 🧠 "À propos"
    val location: String?,             // 📍 Ville
    val profileImage: String?,         // 🧑‍🎤 Photo de profil
    val bannerImage: String?,          // 🖼️ Bannière
    val followers: Int?,               // 👥 Abonnés
    val following: Int?,               // 👣 Suivis
    val portfolioImages: List<String>? // 🎨 Portfolio images
)

/* ✅ Requête PATCH pour /talent/update */
data class UpdateTalentRequest(
    val name: String?,
    val phone: String?,
    val bio: String?, // ⚠️ même nom que ton champ dans UpdateTalentDto côté backend
    val location: String? = null
)

interface TalentApiService {

    // 🔹 GET /talent/me → récupère le profil du talent connecté
    @GET("talent/me")
    suspend fun getTalentProfile(
        @Header("Authorization") token: String
    ): Response<TalentProfileResponse>

    // 🔹 PATCH /talent/update → met à jour le profil (nom, téléphone, bio, etc.)
    @PATCH("talent/update")
    suspend fun updateTalentProfile(
        @Header("Authorization") token: String,
        @Body updateRequest: UpdateTalentRequest
    ): Response<TalentProfileResponse>

    // 🔹 POST /talent/upload-profile → upload de la photo de profil
    @Multipart
    @POST("talent/upload-profile")
    suspend fun uploadProfileImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<TalentProfileResponse>

    // 🔹 POST /talent/upload-banner → upload de la bannière
    @Multipart
    @POST("talent/upload-banner")
    suspend fun uploadBannerImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<TalentProfileResponse>
}
