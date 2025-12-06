package com.example.scorly.Models

data class StatsResponse(
    val success: Boolean,
    val data: List<EstadisticaJugador>,
    val total: Int
)

data class EstadisticaJugador(
    val estadistica_id: Int,
    val goles: Int,
    val asistencias: Int,
    val partidos_jugados: Int,
    val nombre: String,
    val apellido: String,
    val nombre_equipo: String,
    val foto_url: String?,
    val promedio_goles: Double?
)

data class Estadistica(
    val estadistica_id: Int = 0,
    val temporada: String,
    val nombre_equipo: String,
    val nombre_liga: String,
    val goles: Int,
    val asistencias: Int,
    val tarjetas_amarillas: Int,
    val tarjetas_rojas: Int,
    val minutos_jugados: Int,
    val partidos_jugados: Int
)

data class EstadisticaResponse(
    val success: Boolean,
    val data: List<Estadistica>
)