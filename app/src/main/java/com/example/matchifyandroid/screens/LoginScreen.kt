package com.example.matchifyandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.R
import com.example.matchifyandroid.components.SocialIcon
import com.example.matchifyandroid.data.UserPreferences
import com.example.matchifyandroid.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    role: String? = null,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var savedRole by remember { mutableStateOf(role ?: "") }

    // 🔁 Lecture du rôle sauvegardé
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userPreferences.userRole.collectLatest {
                if (it != null) savedRole = it
            }
        }
    }

    val backgroundColor = Color(0xFF7FB2DC)
    val buttonColor = Color(0xFF1B224D)

    val isLoading by viewModel.isLoading.collectAsState()
    val authSuccess by viewModel.authSuccess.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    // ✅ Snackbar host
    val snackbarHostState = remember { SnackbarHostState() }

    // ✅ Gère la navigation et les snackbars
    LaunchedEffect(authSuccess, errorMessage) {
        when {
            authSuccess -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "✅ Connexion réussie !",
                        withDismissAction = true
                    )
                }

                // ⏳ petite pause pour que la Snackbar s’affiche avant la redirection
                kotlinx.coroutines.delay(500)

                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }

            errorMessage != null -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "❌ ${errorMessage ?: "Erreur de connexion"}",
                        withDismissAction = true
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Login as ${savedRole.ifEmpty { "User" }}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B224D)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ✉️ Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔒 Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    // 🧩 Lien "Mot de passe oublié ?"
                    TextButton(
                        onClick = { navController.navigate("resetPassword") },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            "Forgot password?",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔵 Bouton "Sign in"
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                viewModel.login(email, password)
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("⚠️ Remplissez tous les champs.")
                                }
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("Sign in", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("- Or sign in with -", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialIcon(R.drawable.google)
                        Spacer(modifier = Modifier.width(24.dp))
                        SocialIcon(R.drawable.linkedin)
                        Spacer(modifier = Modifier.width(24.dp))
                        SocialIcon(R.drawable.github)
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Don’t have an account? ", color = Color.Gray)
                        TextButton(onClick = { navController.navigate("signup/$savedRole") }) {
                            Text("Sign up", color = buttonColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController, "Talent")
}
