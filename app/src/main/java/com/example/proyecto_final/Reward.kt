package com.example.proyecto_final

data class Reward(
    val id: Int,
    val title: String,
    val description: String,
    val points: Int, // Puntos de XP que da esta recompensa
    val condition: String, // La condición para desbloquear (ej: "CompletedLessonId:1", "CompletedSection:Verbs")
    var isUnlocked: Boolean = false // Estado si el usuario ya la obtuvo
)
