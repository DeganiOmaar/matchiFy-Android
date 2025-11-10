package com.example.matchifyandroid.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.matchifyandroid.Controllers.MissionsListController
import com.example.matchifyandroid.Models.MissionListItem
import com.example.matchifyandroid.R
import com.example.matchifyandroid.Routes.Routes
import com.example.matchifyandroid.ui.theme.MatchiFyAndroidTheme

@Composable
fun MissionsListScreen(
    navController: NavHostController
) {
    val controller = remember { MissionsListController() }
    val missions by remember { derivedStateOf { controller.filteredMissions } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
    ) {
        TopBarMissions(
            onAvatarClick = { /* plus tard: profil */ },
            onAddClick = { navController.navigate(Routes.MISSION_FORM) }
        )

        SearchBar(
            searchText = controller.searchText,
            onTextChange = { controller.searchText = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(missions, key = { it.id }) { mission ->
                MissionCard(
                    mission = mission,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("mission", mission)

                        navController.navigate(Routes.MISSION_DETAILS)
                    }
                )
            }
        }
    }
}

@Composable
private fun TopBarMissions(
    onAvatarClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .clickable { onAvatarClick() },
            shape = CircleShape,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "Missions",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        )

        Surface(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterEnd),
            shape = CircleShape,
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clickable { onAddClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add mission",
                    tint = Color(0xFF2F80ED)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchText: String,
    onTextChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth(),
                shape = RoundedCornerShape(34.dp),
            placeholder = { Text("Search missions") },
            singleLine = true
        )
    }
}

@Composable
private fun MissionCard(
    mission: MissionListItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF7F9FF)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = mission.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "⋯",
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = mission.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF8C8FA3),
                        fontSize = 13.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    mission.skills.take(3).forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFE3EEFF)
                        ) {
                            Text(
                                text = skill,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF2F80ED),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF2F80ED),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = mission.duration,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF2F80ED),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            tint = Color(0xFF27AE60),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = mission.budget,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF27AE60),
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// 🔹 Preview
@Preview(showBackground = true)
@Composable
fun MissionsListScreenPreview() {
    MatchiFyAndroidTheme {
        val navController = rememberNavController()
        MissionsListScreen(navController = navController)
    }
}