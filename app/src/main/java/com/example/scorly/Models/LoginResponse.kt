package com.example.scorly.Models



data class LoginResponse(
    val usuario: Usuario,
    val mensaje: String,
    val token: String
)


data class Usuario(
    val usuario_id: Int,
    val nombre_usuario: String,
    val email: String
)
