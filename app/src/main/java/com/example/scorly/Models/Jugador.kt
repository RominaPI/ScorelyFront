
package com.example.scorly.Models


data class Jugador(
    val jugador_id: Int?,
    val equipo_id: Int?,
    val nombre: String?,
    val apellido: String?,
    val posicion: String?,
    val numero_camiseta: Int?,
    val nacionalidad: String?,
    val foto_url: String?,
    val fecha_nacimiento: String?,
    val nombre_equipo: String? = null,
    val escudo_equipo: String? = null,
    val data: Jugador? = null
)