package com.example.matchifyandroid.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchifyandroid.Models.MissionListItem
import com.example.matchifyandroid.ui.theme.MatchiFyAndroidTheme

@Composable
fun MissionDetailsScreen(
    mission: MissionListItem
) {
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MissionDetailsTopBar(
                isFavorite = isFavorite,
                onFavoriteToggle = { isFavorite = !isFavorite }
            )
        },
        bottomBar = {
            ApplyBottomBar(
                onApplyClick = {
                    println("Apply tapped for mission: ${mission.title}")
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Titre de la mission
            Text(
                text = mission.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Durée + Budget
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF2F80ED),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = mission.duration,
                        fontSize = 14.sp,
                        color = Color(0xFF2F80ED)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFF27AE60),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = mission.budget,
                        fontSize = 14.sp,
                        color = Color(0xFF27AE60)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Required skills
            if (mission.skills.isNotEmpty()) {
                Text(
                    text = "Required skills",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mission.skills.forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFFE4F0FF)
                        ) {
                            Text(
                                text = skill,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF2F80ED),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Description
            Text(
                text = "Description",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = mission.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF7A7D8E),
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun MissionDetailsTopBar(
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Mission Details",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Surface(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterEnd),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFE74C3C) else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ApplyBottomBar(
    onApplyClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Button(
            onClick = onApplyClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F80ED))
        ) {
            Text(
                text = "Apply to this mission",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

// 🔹 Preview
@Preview(showBackground = true)
@Composable
fun MissionDetailsScreenPreview() {
    val sampleMission = MissionListItem(
        title = "Social Media Campaign for Fashion Brand",
        description = "Create and manage a 3-month social media campaign for a new fashion collection, including content creation, influencer outreach and weekly reporting.",
        duration = "3 months",
        budget = "€2,500",
        skills = listOf("Marketing", "Social Media", "Content Creation")
    )

    MatchiFyAndroidTheme {
        MissionDetailsScreen(mission = sampleMission)
    }
}