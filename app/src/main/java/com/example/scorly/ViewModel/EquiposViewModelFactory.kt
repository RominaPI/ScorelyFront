package com.example.Scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Scorly.Data.ApiService

class EquiposViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquiposViewModel::class.java)) {
            return EquiposViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


