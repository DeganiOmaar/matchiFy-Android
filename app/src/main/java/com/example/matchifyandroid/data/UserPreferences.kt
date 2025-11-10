package com.example.matchifyandroid.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")



class UserPreferences(private val context: Context) {

    companion object {
        private val ROLE_KEY = stringPreferencesKey("user_role")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val REMEMBER_KEY = booleanPreferencesKey("remember_me")
        private val TOKEN_KEY = stringPreferencesKey("user_token")
    }

    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { it[ROLE_KEY] = role }
    }

    val userRole: Flow<String?> = context.dataStore.data.map { it[ROLE_KEY] }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { it[EMAIL_KEY] = email }
    }

    suspend fun getEmail(): String? {
        return context.dataStore.data.map { it[EMAIL_KEY] }.firstOrNull()
    }

    suspend fun clearEmail() {
        context.dataStore.edit { it.remove(EMAIL_KEY) }
    }

    // ✅ Remember Me
    suspend fun setRememberMe(enabled: Boolean) {
        context.dataStore.edit { it[REMEMBER_KEY] = enabled }
    }

    suspend fun isRemembered(): Boolean {
        return context.dataStore.data.map { it[REMEMBER_KEY] ?: false }.firstOrNull() ?: false
    }

    suspend fun clearRememberMe() {
        context.dataStore.edit { it[REMEMBER_KEY] = false }
    }
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }
    val userToken: Flow<String?> = context.dataStore.data.map { prefs -> prefs[TOKEN_KEY] }
    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }


    }

}
