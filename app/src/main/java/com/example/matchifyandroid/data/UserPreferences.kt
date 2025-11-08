package com.example.matchifyandroid.data
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val ROLE_KEY = stringPreferencesKey("user_role")
    }

    // ✅ Sauvegarder le rôle
    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { prefs ->
            prefs[ROLE_KEY] = role
        }
    }

    // ✅ Lire le rôle sauvegardé
    val userRole: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[ROLE_KEY] }
}
