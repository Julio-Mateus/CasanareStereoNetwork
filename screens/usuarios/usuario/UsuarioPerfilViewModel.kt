package com.jcmateus.casanarestereo.screens.usuarios.usuario

import android.net.Uri
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcmateus.casanarestereo.screens.login.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioPerfilViewModel(private val usuarioRepository: UsuarioRepository, private val authService: AuthService) : ViewModel() {

    private val _usuario = MutableStateFlow<AuthService.User?>(null)
    val usuario: StateFlow<AuthService.User?> = _usuario

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    init {
        obtenerPerfilUsuario()
    }

    fun obtenerPerfilUsuario() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val uid = authService.getCurrentUser()?.uid ?: ""
                val usuario = usuarioRepository.getUsuario(uid)
                _usuario.value = usuario
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al obtener el perfil del usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarPerfilUsuario(nombre: String, fotoUri: Uri?) {
        viewModelScope.launch {
            _isLoading.value = true
            _isSuccess.value = false
            try {
                val uid = authService.getCurrentUser()?.uid ?: ""
                val fotoUrl = if (fotoUri != null) {
                    usuarioRepository.uploadFotoPerfil(fotoUri, uid)
                } else {
                    _usuario.value?.avatarUrl
                }
                val usuarioActualizado = _usuario.value?.copy(nombre = nombre, avatarUrl = fotoUrl) ?: AuthService.User(uid = uid, nombre = nombre, avatarUrl = fotoUrl)
                usuarioRepository.updateUsuario(usuarioActualizado)
                _usuario.value = usuarioActualizado
                _error.value = null
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Error al actualizar el perfil del usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun resetIsSuccess() {
        _isSuccess.value = false
    }
}