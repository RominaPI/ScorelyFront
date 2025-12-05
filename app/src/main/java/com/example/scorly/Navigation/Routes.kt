package com.example.scorly.Navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
object PruebaRoute

@Serializable
object LoginRoute

@Serializable
object JugadoresRoute

@Serializable
object LigasRoute

@Serializable
data class EquiposRoute(val id: String)

@Serializable
object PrincipalRoute

@Serializable
object SignUpRoute
