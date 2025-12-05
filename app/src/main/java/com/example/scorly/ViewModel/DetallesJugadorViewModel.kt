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
                    println("DEBUG: Nombre recuperado: ${jugadorReal?.nombre}")
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