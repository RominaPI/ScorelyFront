package com.example.Scorly.Models

data class Equipo(
    val equipo_id: Int,
    val liga_id: Int,
    val nombre: String,
    val escudo_logo_url: String,
    val ciudad: String,
    val nombre_estadio: String,
    val nombre_liga: String
)
