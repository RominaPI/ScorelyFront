package com.example.scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiService
import com.example.scorly.Models.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class EquiposViewModel(private val api: ApiService) : ViewModel() {

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos = _equipos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getEquipos()
    }

    fun getEquipos() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = api.getEquipos()

                if (response.isSuccessful) {
                    _equipos.value = response.body()?.data ?: emptyList()
                } else {
                    println("Error HTTP al obtener equipos. Código: ${response.code()}")
                }
            } catch (e: Exception) {
                println("FATAL ERROR en getEquipos: ${e.message}")
            } finally {
                _isLoading.value = false

            }
        }
    }
}