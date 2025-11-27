package com.example.scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiService
import com.example.scorly.Models.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EquiposViewModel(private val api: ApiService) : ViewModel() {

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos = _equipos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Carga TODOS ( Puede ser que ya no la ocupemos)
    fun cargarEquipos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getEquipos()
                if (response.isSuccessful) {
                    _equipos.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    // FUNCION PARA FILTRAR SOLO POR LIGA SELECCIONADA
    fun cargarEquiposPorLiga(ligaId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getEquiposPorLiga(ligaId)
                if (response.isSuccessful) {
                    _equipos.value = response.body()?.data ?: emptyList()
                } else {
                    println("Error API: ${response.code()}")
                    _equipos.value = emptyList()
                }
            } catch (e: Exception) {
                println("Error conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}