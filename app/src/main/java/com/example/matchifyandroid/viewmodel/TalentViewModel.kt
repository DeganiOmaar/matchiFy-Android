package com.example.matchifyandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchifyandroid.network.TalentProfileResponse
import com.example.matchifyandroid.network.TalentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TalentViewModel : ViewModel() {

    private val repository = TalentRepository()

    private val _profile = MutableStateFlow<TalentProfileResponse?>(null)
    val profile: StateFlow<TalentProfileResponse?> = _profile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProfile(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getProfile(token)
            if (result != null) {
                _profile.value = result
            } else {
                _error.value = "Erreur lors du chargement du profil."
            }
            _isLoading.value = false
        }
    }

    fun updateProfile(token: String, name: String, phone: String, bio: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateProfile(token, name, phone, bio)
            if (result != null) {
                _profile.value = result
            } else {
                _error.value = "Échec de la mise à jour du profil."
            }
            _isLoading.value = false
        }
    }

    fun uploadProfileImage(token: String, file: java.io.File) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.uploadProfileImage(token, file)
            if (result != null) {
                _profile.value = result
            } else {
                _error.value = "Erreur lors de l’upload de la photo de profil."
            }
            _isLoading.value = false
        }
    }

    fun uploadBannerImage(token: String, file: java.io.File) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.uploadBannerImage(token, file)
            if (result != null) {
                _profile.value = result
            } else {
                _error.value = "Erreur lors de l’upload de la bannière."
            }
            _isLoading.value = false
        }
    }
}
