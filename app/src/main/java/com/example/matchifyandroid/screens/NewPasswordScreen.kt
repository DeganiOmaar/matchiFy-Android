package com.example.matchifyandroid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordScreen(
    navController: NavController,
    token: String?,
    viewModel: AuthViewModel = viewModel()
) {
    var localToken by remember { mutableStateOf(token ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val clipboardManager = LocalClipboardManager.current
    val isLoading by viewModel.isLoading.collectAsState()
    val success by viewModel.authSuccess.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ✅ Gérer succès et erreurs
    LaunchedEffect(success, errorMessage) {
        when {
            success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "✅ Mot de passe mis à jour avec succès !",
                        withDismissAction = true
                    )
                }
                kotlinx.coroutines.delay(800)
                navController.navigate("login/talent") {
                    popUpTo("newPassword") { inclusive = true }
                }
            }

            errorMessage != null -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "❌ ${errorMessage}",
                        withDismissAction = true
                    )
                }
            }
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF7FB2DC), Color(0xFF1B224D))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Réinitialiser le mot de passe",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B224D)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔹 Champ Token
                    OutlinedTextField(
                        value = localToken,
                        onValueChange = { localToken = it },
                        label = { Text("Code reçu par email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1B224D),
                            unfocusedBorderColor = Color(0xFF1B224D).copy(alpha = 0.5f)
                        ),
                        trailingIcon = {
                            TextButton(onClick = {
                                val clipboardText = clipboardManager.getText()?.text ?: ""
                                if (clipboardText.isNotEmpty()) {
                                    localToken = clipboardText
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("📋 Token collé avec succès !")
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("⚠️ Aucun texte dans le presse-papiers.")
                                    }
                                }
                            }) {
                                Text("Coller", color = Color(0xFF1B224D))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 Nouveau mot de passe
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nouveau mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1B224D),
                            unfocusedBorderColor = Color(0xFF1B224D).copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 Confirmation
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmer le mot de passe") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1B224D),
                            unfocusedBorderColor = Color(0xFF1B224D).copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // 🔵 Bouton principal
                    Button(
                        onClick = {
                            when {
                                localToken.isBlank() -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("⚠️ Veuillez coller le token reçu par email.")
                                }

                                newPassword.isBlank() || confirmPassword.isBlank() -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("⚠️ Remplissez tous les champs.")
                                }

                                newPassword != confirmPassword -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("⚠️ Les mots de passe ne correspondent pas.")
                                }

                                else -> viewModel.updatePassword(localToken, newPassword)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1B224D),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = "Mettre à jour",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(
                            "Retour",
                            color = Color(0xFF1B224D),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewPasswordScreenPreview() {
    val navController = rememberNavController()
    NewPasswordScreen(navController, token = null)
}
