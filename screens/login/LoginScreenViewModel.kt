//@file:Suppress("UNREACHABLE_CODE")

@file:Suppress("DEPRECATION")

package com.jcmateus.casanarestereo.screens.login

//import kotlinx.coroutines.DefaultExecutor.delay
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.screens.home.Destinos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val dataStoreManager: DataStoreManager,
    private val authService: AuthService
) : ViewModel() {
    sealed class LoginError {
        data class GenericError(val message: String) : LoginError()
        object NoRolSelectedOnGoogleLogin : LoginError()
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _loginError = MutableStateFlow<LoginError?>(null)
    val loginError: StateFlow<LoginError?> = _loginError.asStateFlow()

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo.asStateFlow()

    val authState: StateFlow<EstadoAutenticacion> = authService.authState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userRol = MutableStateFlow<Rol>(Rol.USUARIO)
    val userRol: StateFlow<Rol> = _userRol.asStateFlow()

    init {
        Log.d("LoginScreenViewModel", "init: LoginScreenViewModel inicializado")
        observeAuthState()
        observeUserRol()
    }

    private fun navigateToScreenByRol(rol: Rol) {
        when (rol) {
            Rol.USUARIO -> _navigateTo.value = Destinos.UsuarioPerfilScreen.ruta
            Rol.EMISORA -> _navigateTo.value = Destinos.EmisoraVista.ruta
            Rol.ADMINISTRADOR -> _navigateTo.value = Destinos.Pantalla1.ruta
        }
    }

    private fun observeAuthState() {
        Log.d("LoginScreenViewModel", "observeAuthState: Observando cambios en authState")
        viewModelScope.launch {
            authService.authState.collectLatest { estado ->
                Log.d("LoginScreenViewModel", "observeAuthState: Nuevo estado: $estado")
                _isLoading.value = estado is EstadoAutenticacion.Loading
                when (estado) {
                    is EstadoAutenticacion.LoggedIn -> {
                        val rol = estado.rol
                        _errorMessage.value = null
                        _successMessage.value = "Inicio de sesión exitoso"
                        _userRol.value = rol
                        navigateToScreenByRol(rol)
                        _isLoggedIn.value = true
                    }

                    is EstadoAutenticacion.LoggedOut -> {
                        _errorMessage.value = null
                        _successMessage.value = null
                        _isLoggedIn.value = false
                        _navigateTo.value = null
                    }

                    is EstadoAutenticacion.Error -> {
                        _loginError.value = LoginError.GenericError(estado.message)
                        _errorMessage.value = estado.message
                        Log.e("LoginScreenViewModel", "observeAuthState: Error: ${estado.message}")
                        _successMessage.value = null
                        _isLoggedIn.value = false
                        _navigateTo.value = null
                    }

                    is EstadoAutenticacion.Loading -> {
                        _errorMessage.value = null
                        _successMessage.value = null
                        _isLoggedIn.value = false
                        _navigateTo.value = null
                    }
                }
            }
        }
    }

    private fun observeUserRol() {
        viewModelScope.launch {
            dataStoreManager.getRol().collectLatest { rol ->
                _userRol.value = rol ?: Rol.USUARIO
            }
        }
    }

    fun crearUsuarioConCorreoYContrasena(
        email: String,
        password: String,
        checkTerminos: Boolean,
        selectedRol: Rol,
        latitud: Double? = null,
        longitud: Double? = null
    ) {
        Log.d(
            "LoginScreenViewModel",
            "crearUsuarioConCorreoYContrasena: Creando usuario con correo y contraseña"
        )
        viewModelScope.launch {
            _isLoading.value = true
            authService.crearUsuarioConCorreoYContrasena(
                email,
                password,
                selectedRol,
                latitud,
                longitud
            )
            dataStoreManager.saveHasCompletedForm(false)
            _isLoading.value = false
        }
    }

    fun iniciarSesionConCorreoYContrasena(
        email: String,
        password: String
    ) {
        Log.d(
            "LoginScreenViewModel",
            "iniciarSesionConCorreoYContrasena: Iniciando sesión con correo y contraseña"
        )
        viewModelScope.launch {
            _isLoading.value = true
            authService.iniciarSesionConCorreoYContrasena(
                email,
                password
            )
            _isLoading.value = false
        }
    }

    fun iniciarSesionConGoogle(
        context: Context,
        credential: AuthCredential,
        selectedRol: Rol?
    ) {
        Log.d("LoginScreenViewModel", "iniciarSesionConGoogle: Iniciando sesión con Google")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (selectedRol != null) {
                    authService.iniciarSesionConGoogle(context, credential, selectedRol)
                } else {
                    _loginError.value = LoginError.NoRolSelectedOnGoogleLogin
                    _errorMessage.value = "Por favor, selecciona un rol."
                    Log.e("LoginScreenViewModel", "iniciarSesionConGoogle: Rol es null")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión con Google: ${e.message}"
                Log.e("LoginScreenViewModel", "iniciarSesionConGoogle: Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
        _navigateTo.value = null
    }

    fun setSuccessMessage(destino: String) {
        _successMessage.value = "Inicio de sesión exitoso"
        _navigateTo.value = destino
    }
}
