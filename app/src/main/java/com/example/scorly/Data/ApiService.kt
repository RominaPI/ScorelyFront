package com.example.scorly.Data

import com.example.scorly.Models.Equipo
import com.example.scorly.Models.EquiposResponse
import com.example.scorly.Models.EstadisticaResponse
import com.example.scorly.Models.Jugador
import com.example.scorly.Models.JugadoresResponse
import com.example.scorly.Models.LigasResponse
import com.example.scorly.Models.LoginRequest
import com.example.scorly.Models.LoginResponse
import com.example.scorly.Models.RegistroRequest
import com.example.scorly.Models.RegistroResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val BASE_URL = "http://165.227.57.191:3000/api/"

interface ApiService {

    // ----------- AUTH -----------
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegistroRequest): Response<RegistroResponse>

    // ----------- JUGADORES -----------
    @GET("jugadores")
    suspend fun getJugadores(): Response<JugadoresResponse>

    @GET("jugadores/{id}")
    suspend fun getJugadorById(@Path("id") id: Int): Response<Jugador>

    @POST("jugadores")
    suspend fun createJugador(@Body jugador: Jugador): Response<Jugador>

    @PUT("jugadores/{id}")
    suspend fun updateJugador(@Path("id") id: Int, @Body jugador: Jugador): Response<Jugador>

    @DELETE("jugadores/{id}")
    suspend fun deleteJugador(@Path("id") id: Int): Response<Unit>

    // ----------- EQUIPOS -----------
    @GET("equipos")
    suspend fun getEquipos(): Response<EquiposResponse>

    // Equipos por Liga
    @GET("equipos/liga/{id}")
    suspend fun getEquiposPorLiga(@Path("id") id: Int): Response<EquiposResponse>

    // ----------- LIGAS -----------
    @GET("ligas")
    suspend fun getLigas(): Response<LigasResponse>

    // ----------- ESTADISTICAS -----------
    @GET("Jugadores/{id}/estadisticas")
    suspend fun getEstadisticasJugador(@Path("id") id: Int): Response<EstadisticaResponse>
}

object ApiServiceFactory {
    fun create(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}