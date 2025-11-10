package com.example.matchifyandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }

    val backgroundColor = Color(0xFF7FB2DC)
    val buttonColor = Color(0xFF1B224D)

    val isLoading by viewModel.isLoading.collectAsState()
    val success by viewModel.authSuccess.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // ✅ Quand succès → affiche message + navigue vers NewPasswordScreen
    LaunchedEffect(success, errorMessage) {
        when {
            success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "✅ Lien de réinitialisation envoyé à $email",
                        withDismissAction = true
                    )
                }

                // 🧭 Navigation automatique vers NewPasswordScreen
                navController.navigate("newPassword") {
                    popUpTo("resetPassword") { inclusive = true }
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

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Réinitialiser le mot de passe",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = buttonColor,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = buttonColor,
                        unfocusedBorderColor = buttonColor.copy(alpha = 0.6f),
                        cursorColor = buttonColor
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 🔵 Bouton principal
                Button(
                    onClick = {
                        if (email.isNotBlank()) {
                            viewModel.forgotPassword(email)
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("⚠️ Veuillez entrer votre email.")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Envoyer le lien", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Retour", color = buttonColor, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPasswordScreenPreview() {
    val navController = rememberNavController()
    ResetPasswordScreen(navController)
}
