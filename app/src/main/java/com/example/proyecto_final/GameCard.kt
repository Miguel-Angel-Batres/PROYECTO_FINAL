package com.example.proyecto_final

data class GameCard(
    val word: String,
    val trad: String,
    var volteada: Boolean,
    var selected: Boolean
)