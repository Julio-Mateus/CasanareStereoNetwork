//@file:Suppress("UNREACHABLE_CODE")

@file:Suppress("DEPRECATION")

package com.jcmateus.casanarestereo.screens.login

//import kotlinx.coroutines.DefaultExecutor.delay
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val dataStoreManager: DataStoreManager,
    private val authService: AuthService,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _authState = MutableStateFlow<EstadoAutenticacion>(EstadoAutenticacion.Loading)
    val authState: StateFlow<EstadoAutenticacion> = _authState.asStateFlow()

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    fun crearUsuarioConCorreoYContrasena(
        email: String,
        password: String,
        checkTerminos: Boolean,
        selectedRol: Rol?,
        function: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (selectedRol != null) {
                    authService.crearUsuarioConCorreoYContrasena(
                        email,
                        password,
                        checkTerminos,
                        selectedRol
                    )
                    _successMessage.value = "Usuario creado exitosamente"
                } else {
                    _errorMessage.value = "Debes seleccionar un rol"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear el usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun iniciarSesionConCorreoYContrasena(
        context: Context,
        email: String,
        password: String,
        selectedRol: Rol?,
        function: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (selectedRol != null) {
                    authService.iniciarSesionConCorreoYContrasena(
                        context.toString(),
                        email,
                        password,
                        selectedRol
                    )
                } else {
                    _errorMessage.value = "Debes seleccionar un rol"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun iniciarSesionConGoogle(
        context: Context,
        credential: AuthCredential,
        selectedRol: Rol?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (selectedRol != null) {
                    authService.iniciarSesionConGoogle(context, credential, selectedRol)
                    onSuccess()
                } else {
                    _errorMessage.value = "Debes seleccionar un rol"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión con Google: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}



