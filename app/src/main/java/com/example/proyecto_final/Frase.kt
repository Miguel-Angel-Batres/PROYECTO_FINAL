package com.example.proyecto_final

data class Frase(
    val id: Int,
    val texto: String,
    val correcta: String,
    val opciones: List<String>
)
