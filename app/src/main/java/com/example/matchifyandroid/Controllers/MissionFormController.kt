package com.example.matchifyandroid.Controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.matchifyandroid.Models.Mission

class MissionFormController {

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var duration by mutableStateOf("")
    var budget by mutableStateOf("")

    var skills by mutableStateOf(listOf<String>())
    var newSkill by mutableStateOf("")

    val maxSkills = 6

    // Ajouter une compétence
    fun addSkill() {
        val trimmed = newSkill.trim()
        if (trimmed.isEmpty()) return
        if (skills.size >= maxSkills) return
        if (skills.contains(trimmed)) return  // éviter les doublons

        skills = skills + trimmed
        newSkill = ""
    }

    // Supprimer une compétence
    fun removeSkill(index: Int) {
        if (index !in skills.indices) return
        skills = skills.toMutableList().also { it.removeAt(index) }
    }

    // Construction d'un objet Mission (statique pour l'instant)
    fun buildMission(): Mission {
        return Mission(
            title = title,
            description = description,
            duration = duration,
            budget = budget,
            skills = skills
        )
    }

    // Soumission (pour l'instant : juste un print)
    fun submit() {
        val mission = buildMission()
        println("Mission created: $mission")
        // plus tard : appel API ici
    }
}