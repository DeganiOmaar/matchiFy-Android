package com.example.matchifyandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchifyandroid.data.UserPreferences
import com.example.matchifyandroid.network.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val userPrefs = UserPreferences(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _authSuccess = MutableStateFlow(false)
    val authSuccess = _authSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // 🔹 LOGIN
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _authSuccess.value = false
                _error.value = null

                val response = repository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()
                    val token = body?.access_token ?: ""

                    // ✅ Sauvegarder le token après connexion
                    if (token.isNotEmpty()) {
                        userPrefs.saveToken(token)
                    }

                    _authSuccess.value = true
                } else {
                    _error.value = response.errorBody()?.string() ?: "Email ou mot de passe invalide"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur réseau"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 🔹 SIGNUP
    fun signup(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _authSuccess.value = false
                _error.value = null

                val response = repository.signup(name, email, password, role)
                if (response.isSuccessful && response.body() != null) {
                    _authSuccess.value = true
                } else {
                    _error.value = response.errorBody()?.string() ?: "Erreur lors de l’inscription"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur réseau"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _authSuccess.value = false
                _error.value = null

                val response = repository.forgotPassword(email)
                if (response.isSuccessful && response.body()?.success == true) {
                    _authSuccess.value = true
                } else {
                    _error.value = response.body()?.message ?: "Erreur serveur"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur réseau"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePassword(token: String, newPassword: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _authSuccess.value = false
                _error.value = null

                val response = repository.resetPassword(token, newPassword)
                if (response.isSuccessful && response.body()?.success == true) {
                    _authSuccess.value = true
                } else {
                    _error.value = response.body()?.message ?: "Erreur serveur"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur réseau"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
