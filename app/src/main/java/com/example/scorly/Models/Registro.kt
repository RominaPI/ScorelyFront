package com.example.scorly.Models



data class RegistroResponse(
    val mensaje: String,
    val usuario: Usuario
)

data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String
)