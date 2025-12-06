package com.example.scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiService
import com.example.scorly.Models.EstadisticaJugador
import com.example.scorly.Screens.JugadorTop // <--- IMPORT CRUCIAL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class EstadisticasViewModel(private val apiService: ApiService) : ViewModel() {

    private val _goleadores = MutableStateFlow<List<JugadorTop>>(emptyList())
    val goleadores: StateFlow<List<JugadorTop>> = _goleadores.asStateFlow()

    private val _asistidores = MutableStateFlow<List<JugadorTop>>(emptyList())
    val asistidores: StateFlow<List<JugadorTop>> = _asistidores.asStateFlow()

    private val _masLetales = MutableStateFlow<List<JugadorTop>>(emptyList())
    val masLetales: StateFlow<List<JugadorTop>> = _masLetales.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        cargarDatos(ligaId = 1, temporada = "2025-2026")
    }

    fun cargarDatos(ligaId: Int, temporada: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val responseGoles = apiService.getRankingGoleadores(ligaId, temporada)
                if (responseGoles.isSuccessful && responseGoles.body() != null) {
                    val datosReales = responseGoles.body()!!.data
                    _goleadores.value = mapearAJugadorTop(datosReales, esGoles = true)

                    val listaLetales = datosReales
                        .filter { it.partidos_jugados > 0 }
                        .sortedByDescending { it.goles.toDouble() / it.partidos_jugados }
                        .take(5)

                    val df = DecimalFormat("#.##")
                    _masLetales.value = listaLetales.mapIndexed { index, item ->
                        val promedio = item.goles.toDouble() / item.partidos_jugados
                        JugadorTop(
                            rank = index + 1,
                            nombre = "${item.nombre} ${item.apellido}",
                            equipo = item.nombre_equipo,
                            valor = "${df.format(promedio)} Gol/Part",
                            fotoUrl = item.foto_url ?: ""
                        )
                    }
                }

                val responseAsist = apiService.getRankingAsistencias(ligaId, temporada)
                if (responseAsist.isSuccessful && responseAsist.body() != null) {
                    val datosReales = responseAsist.body()!!.data
                    _asistidores.value = mapearAJugadorTop(datosReales, esGoles = false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun mapearAJugadorTop(lista: List<EstadisticaJugador>, esGoles: Boolean): List<JugadorTop> {
        return lista.mapIndexed { index, item ->
            JugadorTop(
                rank = index + 1,
                nombre = "${item.nombre} ${item.apellido}",
                equipo = item.nombre_equipo,
                valor = if (esGoles) "${item.goles} Goles" else "${item.asistencias} Asist.",
                fotoUrl = item.foto_url ?: ""
            )
        }
    }
}