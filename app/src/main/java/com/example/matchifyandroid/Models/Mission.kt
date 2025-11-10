package com.example.matchifyandroid.Models

import java.util.UUID

data class Mission(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    var duration: String,
    var budget: String,
    var skills: List<String>
)