package com.example.matchifyandroid.Controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.matchifyandroid.Models.MissionListItem

class MissionsListController {

    var searchText by mutableStateOf("")

    var missions by mutableStateOf(
        listOf(
            MissionListItem(
                title = "Social Media Campaign for Fashion Brand",
                description = "Create and manage a 3-month social media campaign for a new fashion collection, including content creation, influencer outreach and weekly reporting.",
                duration = "3 months",
                budget = "€2,500",
                skills = listOf("Marketing", "Social Media", "Content Creation")
            ),
            MissionListItem(
                title = "Music Video Editing for Indie Artist",
                description = "Edit a complete music video from raw footage. The style should be dynamic, modern and optimized for YouTube and TikTok.",
                duration = "2 weeks",
                budget = "€800",
                skills = listOf("Video Editing", "Color Grading", "Storytelling")
            ),
            MissionListItem(
                title = "Brand Identity for Creative Studio",
                description = "Design a full brand identity including logo, color palette, typography and basic guidelines in a short PDF brandbook.",
                duration = "1 month",
                budget = "€1,200",
                skills = listOf("Graphic Design", "Branding", "Illustration")
            )
        )
    )
        private set

    val filteredMissions: List<MissionListItem>
        get() {
            val query = searchText.lowercase().trim()
            if (query.isEmpty()) return missions

            return missions.filter { mission ->
                mission.title.lowercase().contains(query) ||
                        mission.description.lowercase().contains(query) ||
                        mission.duration.lowercase().contains(query) ||
                        mission.budget.lowercase().contains(query) ||
                        mission.skills.any { it.lowercase().contains(query) }
            }
        }
}