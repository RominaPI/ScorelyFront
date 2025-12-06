package com.example.scorly.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorly.Data.ApiService
import com.example.scorly.Data.ApiServiceFactory
import com.example.scorly.Models.LoginRequest
import com.example.scorly.Models.RegistroRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthStatus {
    object Idle : AuthStatus()
    object Loading : AuthStatus()
    data class Success(val message: String) : AuthStatus()
    data class Error(val message: String) : AuthStatus()
    object Finished : AuthStatus()
}

class AuthViewModel(private val apiService: ApiService) : ViewModel() {

    private val _authState = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val authState: StateFlow<AuthStatus> = _authState.asStateFlow()

    fun registerUser(nombre: String, email: String, password: String) {
        _authState.value = AuthStatus.Loading
        viewModelScope.launch {
            try {
                val request = RegistroRequest(nombre = nombre, email = email, password = password)
                val response = apiService.register(request)

                if (response.isSuccessful) {
                    _authState.value = AuthStatus.Success(response.body()?.mensaje ?: "Registro exitoso")
                    kotlinx.coroutines.delay(1000)
                    _authState.value = AuthStatus.Finished
                } else {
                    val errorMessage = when (response.code()) {
                        409 -> "El email ya está registrado."
                        400 -> "Faltan datos en la solicitud."
                        else -> "Error en el servidor: ${response.code()}"
                    }
                    _authState.value = AuthStatus.Error(errorMessage)
                }
            } catch (e: Exception) {
                _authState.value = AuthStatus.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        _authState.value = AuthStatus.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = apiService.login(request) // Llama al endpoint

                if (response.isSuccessful) {
                    _authState.value = AuthStatus.Success(response.body()?.mensaje ?: "Login exitoso")
                    kotlinx.coroutines.delay(1000)
                    _authState.value = AuthStatus.Finished
                } else {
                    val errorMessage = when (response.code()) {
                        404 -> "Credenciales inválidas o servidor no responde."
                        401 -> "Credenciales inválidas."
                        else -> "Error en el servidor: ${response.code()}"
                    }
                    _authState.value = AuthStatus.Error(errorMessage)
                }
            } catch (e: Exception) {
                _authState.value = AuthStatus.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthStatus.Idle
    }
}