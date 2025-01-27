package com.jcmateus.casanarestereo.screens.usuarios.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcmateus.casanarestereo.screens.login.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _usuario = MutableStateFlow<AuthService.User?>(null)
    val usuario: StateFlow<AuthService.User?> = _usuario.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun getUsuario(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = usuarioRepository.getUsuario(uid)
            _usuario.value = user
            _isLoading.value = false
        }
    }

    fun updateUsuario(usuario: AuthService.User) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = usuarioRepository.updateUsuario(usuario)
            if (!success) {
                _errorMessage.value = "Error al actualizar el usuario"
            }
            _isLoading.value = false
        }
    }

    fun addEmisoraFavorita(uid: String, emisoraId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = usuarioRepository.addEmisoraFavorita(uid, emisoraId)
            if (!success) {
                _errorMessage.value = "Error al añadir la emisora a favoritos"
            }
            _isLoading.value = false
        }
    }

    fun removeEmisoraFavorita(uid: String, emisoraId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = usuarioRepository.removeEmisoraFavorita(uid, emisoraId)
            if (!success) {
                _errorMessage.value = "Error al eliminar la emisora de favoritos"
            }
            _isLoading.value = false
        }
    }
}