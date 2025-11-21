package com.example.Scorly.Data

import com.example.Scorly.Models.Equipo
import com.example.Scorly.Models.Jugador

import com.example.Scorly.Models.LoginRequest
import com.example.Scorly.Models.LoginResponse
import com.example.Scorly.Models.RegistroRequest
import com.example.Scorly.Models.RegistroResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {

    // ----------- AUTH -----------
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegistroRequest): Response<RegistroResponse>


    // ----------- JUGADORES -----------

    // Obtener todos los jugadores
    @GET("jugadores")
    suspend fun getJugadores(): Response<List<Jugador>>

    // Obtener jugador por ID
    @GET("jugadores/{id}")
    suspend fun getJugadorById(
        @Path("id") id: Int
    ): Response<Jugador>

    // Crear jugador
    @POST("jugadores")
    suspend fun createJugador(
        @Body jugador: Jugador
    ): Response<Jugador>

    // Actualizar jugador
    @PUT("jugadores/{id}")
    suspend fun updateJugador(
        @Path("id") id: Int,
        @Body jugador: Jugador
    ): Response<Jugador>

    // Eliminar jugador
    @DELETE("jugadores/{id}")
    suspend fun deleteJugador(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("equipos")
    suspend fun getEquipos(): Response<List<Equipo>>
}
object ApiServiceFactory {

    fun create(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // cámbialo si tu API es otra
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}