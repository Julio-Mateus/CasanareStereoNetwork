package com.jcmateus.casanarestereo.screens.usuarios.usuario

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.AuthService.User
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsuarioPerfilViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val authService: AuthService,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getUsuario(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = usuarioRepository.getUsuario(uid)
            _usuario.value = user
            _isLoading.value = false
        }
    }

    fun actualizarPerfilUsuario(nombre: String, fotoUri: Uri?, frase: String, profesion: String, ciudad: String, departamento: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val usuarioActual = _usuario.value
            if (usuarioActual != null) {
                val usuarioActualizado = usuarioActual.copy(
                    nombre = nombre,
                    frase = frase,
                    profesion = profesion,
                    ciudad = ciudad,
                    departamento = departamento
                )
                val success = usuarioRepository.updateUsuario(usuarioActualizado)
                if (success) {
                    _isSuccess.value = true
                    _usuario.value = usuarioActualizado
                } else {
                    _error.value = "Error al actualizar el perfil"
                }
            } else {
                _error.value = "No se encontró el usuario"
            }
            _isLoading.value = false
        }
    }

    fun resetIsSuccess() {
        _isSuccess.value = false
    }

    suspend fun obtenerEmisoraPorId(emisoraId: String): PerfilEmisora? {
        return try {
            val document = db.collection("emisoras").document(emisoraId).get().await()
            if (document.exists()) {
                document.toObject<PerfilEmisora>()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UsuarioPerfilViewModel", "Error al obtener la emisora", e)
            null
        }
    }
}