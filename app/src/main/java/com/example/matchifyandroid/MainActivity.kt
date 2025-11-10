package com.example.matchifyandroid

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.matchifyandroid.navigation.AppNavigator
import com.example.matchifyandroid.ui.theme.MatchiFyAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Vérifie si l'application a été ouverte via un lien (deep link)
        val data: Uri? = intent?.data
        var token: String? = null

        // ✅ Deux cas : soit "matchify://new-password?token=XYZ"
        // soit "matchify://new-password/XYZ"
        data?.let {
            token = it.getQueryParameter("token")
            if (token == null && it.pathSegments.size > 1) {
                token = it.pathSegments[1] // récupère /new-password/{token}
            }
        }

        setContent {
            MatchiFyAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // ✅ Passe le token à AppNavigator (sera null si pas de lien)
                    AppNavigator(token)
                }
            }
        }
    }
}
