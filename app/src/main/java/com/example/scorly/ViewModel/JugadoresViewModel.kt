package com.example.Scorly.ViewModel

import com.example.Scorly.Models.Jugador


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Scorly.Data.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JugadoresViewModel(
    private val api: ApiService
) : ViewModel() {

    // Estado de lista de jugadores
    private val _jugadores = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadores: StateFlow<List<Jugador>> = _jugadores

    // Estado de errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    // ---------- Cargar jugadores ----------
    fun cargarJugadores() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = api.getJugadores()

                if (response.isSuccessful) {
                    _jugadores.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al obtener jugadores"
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    // ---------- Crear jugador ----------
    fun crearJugador(jugador: Jugador) {
        viewModelScope.launch {
            try {
                val response = api.createJugador(jugador)

                if (response.isSuccessful) {
                    cargarJugadores()
                } else {
                    _error.value = "No se pudo crear"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    // ---------- Actualizar jugador ----------
    fun actualizarJugador(id: Int, jugador: Jugador) {
        viewModelScope.launch {
            try {
                val response = api.updateJugador(id, jugador)

                if (response.isSuccessful) {
                    cargarJugadores()
                } else {
                    _error.value = "No se pudo actualizar"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    // ---------- Eliminar jugador ----------
    fun eliminarJugador(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.deleteJugador(id)

                if (response.isSuccessful) {
                    cargarJugadores()
                } else {
                    _error.value = "No se pudo eliminar"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
