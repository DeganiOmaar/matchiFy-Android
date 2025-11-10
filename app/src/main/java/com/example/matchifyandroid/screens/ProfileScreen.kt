package com.example.matchifyandroid.screens

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.matchifyandroid.data.UserPreferences
import com.example.matchifyandroid.network.TalentProfileResponse
import com.example.matchifyandroid.viewmodel.TalentViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: TalentViewModel = viewModel()) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    var token by remember { mutableStateOf<String?>(null) }

    // ✅ Charger le token utilisateur depuis SharedPreferences
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            token = userPreferences.userToken.first()
        }
    }

    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val primary = Color(0xFF1B224D)
    val lightBlue = Color(0xFF7FB2DC)

    // ✅ Charger le profil une fois que le token est prêt
    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            viewModel.loadProfile(token!!)
        }
    }

    // ✅ Si token pas encore récupéré → loader
    if (token == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator(color = primary)
        }
        return
    }

    // ✅ Sélecteurs d’images (profile & bannière)
    val profilePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val file = File(getRealPathFromUri(context, it))
                viewModel.uploadProfileImage(token!!, file)
            }
        }

    val bannerPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val file = File(getRealPathFromUri(context, it))
                viewModel.uploadBannerImage(token!!, file)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primary)
            )
        },
        containerColor = Color(0xFFF2F4F8)
    ) { paddingValues ->
        when {
            isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = primary)
            }

            error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(error ?: "Erreur inconnue", color = Color.Red)
            }

            profile != null -> {
                ProfileContent(
                    profile = profile!!,
                    primary = primary,
                    lightBlue = lightBlue,
                    paddingValues = paddingValues,
                    onEditProfile = { profilePickerLauncher.launch("image/*") },
                    onEditBanner = { bannerPickerLauncher.launch("image/*") },
                    onSaveInfo = { name, phone, bio ->
                        viewModel.updateProfile(token!!, name, phone, bio)
                    }
                )
            }

            else -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Aucune donnée à afficher", color = Color.Gray)
            }
        }
    }
}

/* ✅ Fonction utilitaire pour récupérer le vrai chemin du fichier */
fun getRealPathFromUri(context: Context, uri: Uri): String {
    var path = ""
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            path = it.getString(columnIndex)
        }
    }
    return path
}

/* ✅ Contenu du profil */
@Composable
private fun ProfileContent(
    profile: TalentProfileResponse,
    primary: Color,
    lightBlue: Color,
    paddingValues: PaddingValues,
    onEditProfile: () -> Unit,
    onEditBanner: () -> Unit,
    onSaveInfo: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(profile.name ?: "") }
    var phone by remember { mutableStateOf(profile.phone ?: "") }
    var bio by remember { mutableStateOf(profile.bio ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF3F6FA))
    ) {
        // 🖼️ Bannière
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(primary),
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = profile.bannerImage ?: "https://via.placeholder.com/600x200.png?text=Banner",
                contentDescription = "Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = onEditBanner, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Modifier Bannière", tint = Color.White)
            }
        }

        // 👤 Photo de profil
        Box(
            modifier = Modifier
                .offset(y = (-50).dp)
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = profile.profileImage ?: "https://via.placeholder.com/150.png?text=Profile",
                contentDescription = "Photo de profil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = onEditProfile, modifier = Modifier.align(Alignment.BottomEnd)) {
                Icon(Icons.Default.Edit, contentDescription = "Modifier photo", tint = lightBlue)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🧾 Formulaire d’édition
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom complet") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(4.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Téléphone") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(4.dp)
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("À propos de moi") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onSaveInfo(name, phone, bio) },
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Text("Sauvegarder", color = Color.White)
            }
        }
    }
}

/* ✅ Aperçu Compose */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    val fakeProfile = TalentProfileResponse(
        id = "1",
        name = "Yassmine",
        email = "yassmine@gmail.com",
        role = "talent",
        phone = "06 12 34 56 78",
        bio = "Développeuse mobile passionnée 🎨",
        profileImage = "https://via.placeholder.com/150.png?text=Profile",
        bannerImage = "https://via.placeholder.com/600x200.png?text=Banner",
        location = "Casablanca",
        followers = 123,
        following = 45,
        portfolioImages = emptyList()
    )

    ProfileContent(
        profile = fakeProfile,
        primary = Color(0xFF1B224D),
        lightBlue = Color(0xFF7FB2DC),
        paddingValues = PaddingValues(0.dp),
        onEditProfile = {},
        onEditBanner = {},
        onSaveInfo = { _, _, _ -> }
    )
}
