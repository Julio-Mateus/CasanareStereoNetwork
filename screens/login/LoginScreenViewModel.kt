//@file:Suppress("UNREACHABLE_CODE")

@file:Suppress("DEPRECATION")

package com.jcmateus.casanarestereo.screens.login

//import kotlinx.coroutines.DefaultExecutor.delay
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginScreenViewModel(
    private val dataStoreManager: DataStoreManager,
    private val authService: AuthService,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de autenticación (obtenido de AuthService)
    val authState: StateFlow<EstadoAutenticacion> = authService.authState

    // Mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Mensaje de éxito
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    // Iniciar sesión con Google
    fun iniciarSesionConGoogle(context: Context, credential: AuthCredential, rol: Rol?, home: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = authService.iniciarSesionConCredencialDeGoogle(credential)
            _isLoading.value = false

            if (user != null) {
                dataStoreManager.saveIsLoggedIn(true)
                dataStoreManager.guardarRolUsuario(rol ?: Rol.USUARIO)
                _successMessage.value = "¡Logueado con éxito!"
                home()
            } else {
                _errorMessage.value = "Error al iniciar sesión con Google."
            }
        }
    }

    // Iniciar sesión con correo electrónico y contraseña
    fun iniciarSesionConCorreoYContrasena(context: Context, email: String, password: String, rol: Rol?, home: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = authService.iniciarSesionConCorreoYContrasena(email, password)
            _isLoading.value = false

            if (user != null) {
                dataStoreManager.saveIsLoggedIn(true)
                dataStoreManager.guardarRolUsuario(rol ?: Rol.USUARIO)
                _successMessage.value = "¡Logueado con éxito!"
                home()
            } else {
                _errorMessage.value = "Error al iniciar sesión con correo electrónico y contraseña."
            }
        }
    }

    // Crear usuario con correo electrónico y contraseña
    fun crearUsuarioConCorreoYContrasena(email: String, password: String, checkTerminos: Boolean, rol: Rol?, home: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            val valido = email.trim().isNotEmpty() && password.trim().isNotEmpty() && checkTerminos
            if (valido) {
                if (!authService.isValidEmail(email)) {
                    _errorMessage.value = "Correo electrónico inválido"
                    _isLoading.value = false
                    return@launch
                }
                if (password.length < 6) {
                    _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
                    _isLoading.value = false
                    return@launch
                }

                // Crear usuario en Firebase Authentication
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await() // Assuming firebaseAuth is accessible
                if (authResult.user != null) {
                    val displayName = authResult.user?.email?.split("@")?.get(0)

                    // Crear usuario en Firestore usando AuthService
                    val userCreated = authService.createUser(displayName, rol)

                    if (userCreated) {
                        dataStoreManager.guardarRolUsuario(rol ?: Rol.USUARIO)
                        _successMessage.value = "¡Cuenta creada con éxito!"
                        home()
                    } else {
                        _errorMessage.value = "Error al crear la cuenta en Firestore."
                    }
                } else {
                    _errorMessage.value = "Error al crear la cuenta en Firebase Authentication."
                }
            } else {
                _errorMessage.value = "Debes aceptar las notificaciones y los términos y condiciones."
            }

            _isLoading.value = false
        }
    }

    // Cerrar sesión
    fun cerrarSesion() {
        viewModelScope.launch {
            authService.cerrarSesion()
            dataStoreManager.saveIsLoggedIn(false)
        }
    }
}




