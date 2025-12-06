package com.example.scorly.Navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
object LoginRoute

@Serializable
object PrincipalRoute

@Serializable
object SignUpRoute

@Serializable
object LigasRoute

@Serializable
data class EquiposRoute(val id: Int)

@Serializable
object RegistroEquiposRoute

@Serializable
object JugadoresRoute

@Serializable
object RegistroJugadoresRoute

@Serializable
data class DetallesJugadorRoute(val id: Int)

@Serializable
object SeleccionLigaParaEstadisticasRoute

@Serializable
data class EstadisticasDetalleRoute(val id: Int)