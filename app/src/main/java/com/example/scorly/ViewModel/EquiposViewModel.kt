package com.example.Scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Scorly.Data.ApiService
import com.example.Scorly.Models.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EquiposViewModel(private val api: ApiService) : ViewModel() {

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos = _equipos.asStateFlow()

    init {
        getEquipos()
    }

    fun getEquipos() {
        viewModelScope.launch {
            val response = api.getEquipos()
            if (response.isSuccessful) {
                _equipos.value = response.body() ?: emptyList()
            }
        }
    }
}
