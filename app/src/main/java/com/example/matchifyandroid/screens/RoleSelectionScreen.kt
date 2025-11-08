package com.example.matchifyandroid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.R
import com.example.matchifyandroid.data.UserPreferences
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun RoleSelectionScreen(navController: NavController) {
    // 🎨 Couleurs
    val backgroundColor = Color(0xFF7FB2DC)  // bleu clair
    val cardColor = Color.White
    val textColor = Color(0xFF1B224D)        // bleu foncé

    // 🧠 Initialisation du DataStore
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Select your role",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 🧑‍🎨 Talent
        RoleCard(
            title = "I’m a Talent",
            subtitle = "I offer my skills and expertise.",
            image = R.drawable.ic_talent,
            backgroundColor = cardColor,
            textColor = textColor
        ) {
            coroutineScope.launch {
                userPreferences.saveUserRole("talent")
                navController.navigate("login/talent")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🧑‍💼 Recruteur
        RoleCard(
            title = "I’m a Recruiter",
            subtitle = "I’m looking for creative and skilled talents.",
            image = R.drawable.ic_recruiter,
            backgroundColor = cardColor,
            textColor = textColor
        ) {
            coroutineScope.launch {
                userPreferences.saveUserRole("recruiter")
                navController.navigate("login/recruiter")
            }
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    image: Int,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor.copy(alpha = 0.8f))
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RoleSelectionPreview() {
    val navController = rememberNavController()
    RoleSelectionScreen(navController)
}
