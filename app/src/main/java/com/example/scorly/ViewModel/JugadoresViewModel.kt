package com.example.scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiService
import com.example.scorly.Models.Jugador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class JugadoresViewModel(private val api: ApiService) : ViewModel() {

    private val _jugadores = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadores = _jugadores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getJugadores()
    }

    fun getJugadores() {
        _isLoading.value = true
        viewModelScope.launch {
            try {

                val response = api.getJugadores()

                if (response.isSuccessful) {
                    _jugadores.value = response.body()?.data ?: emptyList()
                } else {
                    println("Error HTTP al obtener jugadores. Código: ${response.code()}")
                }
            } catch (e: Exception) {
                println("FATAL ERROR en getJugadores: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}