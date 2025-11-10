package com.example.matchifyandroid.network

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TalentRepository {

    private val api: TalentApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // ⚠️ Mets ici l’URL de ton backend NestJS
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TalentApiService::class.java)
    }

    suspend fun getProfile(token: String): TalentProfileResponse? {
        return try {
            val response = api.getTalentProfile("Bearer $token")
            if (response.isSuccessful) response.body() else {
                Log.e("TalentRepository", "Erreur API getProfile: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("TalentRepository", "Exception: ${e.message}")
            null
        }
    }

    suspend fun updateProfile(token: String, name: String, phone: String, bio: String): TalentProfileResponse? {
        return try {
            val request = UpdateTalentRequest(name, phone, bio)
            val response = api.updateTalentProfile("Bearer $token", request)
            if (response.isSuccessful) response.body() else {
                Log.e("TalentRepository", "Erreur update: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("TalentRepository", "Exception update: ${e.message}")
            null
        }
    }

    suspend fun uploadProfileImage(token: String, file: java.io.File): TalentProfileResponse? {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = api.uploadProfileImage("Bearer $token", body)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("TalentRepository", "Erreur upload profile: ${e.message}")
            null
        }
    }

    suspend fun uploadBannerImage(token: String, file: java.io.File): TalentProfileResponse? {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = api.uploadBannerImage("Bearer $token", body)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("TalentRepository", "Erreur upload banner: ${e.message}")
            null
        }
    }
}
