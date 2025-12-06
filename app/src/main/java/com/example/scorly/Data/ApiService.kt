package com.example.scorly.Data

import com.example.scorly.Models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL = "http://165.227.57.191:3000/api/"

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegistroRequest): Response<RegistroResponse>

    // --- JUGADORES ---
    @GET("jugadores")
    suspend fun getJugadores(): Response<JugadoresResponse>

    @GET("jugadores/equipo/{equipoId}")
    suspend fun getJugadoresPorEquipo(@Path("equipoId") equipoId: Int): Response<JugadoresResponse>

    @GET("jugadores/{id}")
    suspend fun getJugadorById(@Path("id") id: Int): Response<Jugador>

    @POST("jugadores")
    suspend fun createJugador(@Body jugador: Jugador): Response<Jugador>

    @PUT("jugadores/{id}")
    suspend fun updateJugador(@Path("id") id: Int, @Body jugador: Jugador): Response<Jugador>

    @DELETE("jugadores/{id}")
    suspend fun deleteJugador(@Path("id") id: Int): Response<Unit>

    // --- EQUIPOS/LIGAS/ESTADISTICAS ---
    @GET("equipos")
    suspend fun getEquipos(): Response<EquiposResponse>

    @GET("equipos/{id}")
    suspend fun getEquipoById(@Path("id") id: Int): Response<Equipo>

    @GET("equipos/liga/{id}")
    suspend fun getEquiposPorLiga(@Path("id") id: Int): Response<EquiposResponse>

    @GET("ligas")
    suspend fun getLigas(): Response<LigasResponse>

    @GET("Jugadores/{id}/estadisticas")
    suspend fun getEstadisticasJugador(@Path("id") id: Int): Response<EstadisticaResponse>

    @GET("ligas/{id}/ranking/goleadores/{temporada}")
    suspend fun getRankingGoleadores(
        @Path("id") ligaId: Int,
        @Path("temporada") temporada: String
    ): Response<StatsResponse>

    @GET("ligas/{id}/ranking/asistencias/{temporada}")
    suspend fun getRankingAsistencias(
        @Path("id") ligaId: Int,
        @Path("temporada") temporada: String
    ): Response<StatsResponse>
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