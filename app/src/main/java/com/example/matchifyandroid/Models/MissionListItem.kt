package com.example.matchifyandroid.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class MissionListItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val duration: String,
    val budget: String,
    val skills: List<String>
) : Parcelable