package com.example.scorly.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Models.LigaData
import com.example.scorly.Screens.LigaUI
import kotlinx.coroutines.launch

class LigasViewModel : ViewModel() {

    var listaLigas by mutableStateOf<List<LigaUI>>(emptyList())
        private set

    init {
        cargarLigas()
    }

    fun cargarLigas() {
        viewModelScope.launch {
            try {
                val service = ApiServiceFactory.create()

                val respuesta = service.getLigas()

                if (respuesta.isSuccessful) {
                    val ligasBackend = respuesta.body()?.data ?: emptyList()

                    listaLigas = ligasBackend.map { mapToLigaUI(it) }
                } else {
                    println("Error en la respuesta: ${respuesta.code()}")
                }

            } catch (e: Exception) {
                println("Error de conexión: ${e.message}")
            }
        }
    }

    private fun mapToLigaUI(liga: LigaData): LigaUI {
        val (start, end) = when (liga.nombre) {
            "Premier League" -> Pair(Color(0xFF38003C), Color(0xFF00FF85))
            "La Liga EA Sports" -> Pair(Color(0xFF111111), Color(0xFFEE2323))
            "Liga MX" -> Pair(Color(0xFF003823), Color(0xFFC9F740))
            "Serie A" -> Pair(Color(0xFF0054A6), Color(0xFF00B5E2))
            "Bundesliga" -> Pair(Color(0xFFD20515), Color(0xFFFFFFFF))
            else -> Pair(Color.Gray, Color.White)
        }

        return LigaUI(
            id = liga.id.toString(),
            nombre = liga.nombre,
            pais = liga.pais,
            logoUrl = liga.logoUrl ?: "",
            colorStart = start,
            colorEnd = end
        )
    }
}