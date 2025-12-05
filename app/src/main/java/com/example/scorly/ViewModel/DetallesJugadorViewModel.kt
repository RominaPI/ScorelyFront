package com.example.scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiService
import com.example.scorly.Models.Jugador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetallesJugadorViewModel(private val api: ApiService) : ViewModel() {

    private val _jugador = MutableStateFlow<Jugador?>(null)
    val jugador = _jugador.asStateFlow()

    private val _nombreEquipo = MutableStateFlow<String>("Cargando equipo...")
    val nombreEquipo = _nombreEquipo.asStateFlow()

    private val _nombreLiga = MutableStateFlow<String>("Cargando liga...")
    val nombreLiga = _nombreLiga.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun obtenerDetallesJugador(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getJugadorById(id)

                if (response.isSuccessful) {
                    val respuestaCruda = response.body()
                    val jugadorReal = respuestaCruda?.data ?: respuestaCruda
                    _jugador.value = jugadorReal

                    if (!jugadorReal?.nombre_equipo.isNullOrEmpty()) {
                        _nombreEquipo.value = jugadorReal?.nombre_equipo ?: ""
                    }

                    jugadorReal?.equipo_id?.let { idEquipo ->
                        cargarDatosComplementarios(idEquipo)
                    }

                } else {
                    println("Error API: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun cargarDatosComplementarios(equipoId: Int) {
        viewModelScope.launch {
            try {
                val responseEquipos = api.getEquipos()
                if (responseEquipos.isSuccessful) {
                    val listaEquipos = responseEquipos.body()?.data ?: emptyList()
                    val miEquipo = listaEquipos.find { it.equipo_id == equipoId }

                    if (miEquipo != null) {
                        _nombreEquipo.value = miEquipo.nombre ?: "Equipo desconocido"
                        val ligaId = miEquipo.liga_id
                        cargarNombreLiga(ligaId)
                    }
                }
            } catch (e: Exception) {
                println("Error cargando equipos: ${e.message}")
            }
        }
    }

    private suspend fun cargarNombreLiga(ligaId: Int?) {
        if (ligaId == null) return
        try {
            val responseLigas = api.getLigas()
            if (responseLigas.isSuccessful) {
                val listaLigas = responseLigas.body()?.data ?: emptyList()
                val miLiga = listaLigas.find { it.id == ligaId }
                _nombreLiga.value = miLiga?.nombre ?: "Liga desconocida"
            }
        } catch (e: Exception) {
            println("Error cargando ligas: ${e.message}")
        }
    }
}

class DetallesJugadorViewModelFactory(private val api: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetallesJugadorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetallesJugadorViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}