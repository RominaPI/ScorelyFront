package com.example.scorly.Models


data class EstadisticaResponse(
    val success: Boolean,
    val data: List<Estadistica>,
    val total: Int
)

data class Estadistica(
    val estadistica_id: Int,
    val jugador_id: Int,
    val liga_id: Int,
    val temporada: String,
    val partidos_jugados: Int,
    val goles: Int,
    val asistencias: Int,
    val tarjetas_amarillas: Int,
    val tarjetas_rojas: Int,
    val minutos_jugados: Int,
    val nombre_liga: String,
    val nombre_equipo: String,
    val nombre: String,
    val apellido: String
)