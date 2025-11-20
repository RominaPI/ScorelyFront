
package com.example.Scorly.Models
data class Jugador(
    val jugador_id: Int,
    val equipo_id: Int,
    val nombre: String,
    val apellido: String,
    val posicion: String,
    val numero_camiseta: Int?,
    val nacionalidad: String?,
    val foto_url: String?,
    val fecha_nacimiento: String?
)
