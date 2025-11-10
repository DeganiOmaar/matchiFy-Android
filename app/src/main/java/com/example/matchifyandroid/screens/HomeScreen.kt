package com.example.matchifyandroid.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.data.UserPreferences
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    val items = listOf(
        NavItem("home_tab", "Home", Icons.Default.Home),
        NavItem("publier_tab", "Publier", Icons.Default.AddCircle),
        NavItem("notifications_tab", "Notifications", Icons.Default.Notifications),
        NavItem("profil_tab", "Profil", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, items)
        },
        containerColor = Color(0xFFF3F6FA)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home_tab",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home_tab") { HomeContent(navController) }
            composable("publier_tab") { PublierContent() }
            composable("notifications_tab") { NotificationContent() }
            composable("profil_tab") {
                // ✅ Appel sans token (il est récupéré automatiquement)
                ProfileScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<NavItem>) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color(0xFF1B224D)) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) Color(0xFF7FB2DC) else Color.White
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (currentRoute == item.route) Color(0xFF7FB2DC) else Color.White
                    )
                }
            )
        }
    }
}

data class NavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun HomeContent(navController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Bienvenue sur la page d’accueil 🎉",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B224D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        userPreferences.clearAll() // 🔹 Supprime le token et les prefs
                        navController.navigate("login/talent") {
                            popUpTo("home_tab") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B224D),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Se déconnecter", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun PublierContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Page Publier 📝",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B224D)
        )
    }
}

@Composable
fun NotificationContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Page Notifications 🔔",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B224D)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
