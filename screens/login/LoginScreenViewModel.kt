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
    private val authService: AuthService,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // Usamos Destinos en lugar de NavigationDestination
    private val _navigateTo = MutableStateFlow<Destinos?>(null)
    val navigateTo: StateFlow<Destinos?> = _navigateTo.asStateFlow()

    val authState: StateFlow<EstadoAutenticacion> = authService.authState

    // Nuevo estado para la persistencia de sesión
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Nuevo estado para el rol del usuario
    private val _userRol = MutableStateFlow<Rol>(Rol.USUARIO)
    val userRol: StateFlow<Rol> = _userRol.asStateFlow()

    init {
        Log.d("LoginScreenViewModel", "init: LoginScreenViewModel inicializado")
        // Observar los cambios en authState
        observeAuthState()
        observeUserRol()
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
                        if (rol != null) {
                            _userRol.value = rol
                            // Navegar a la pantalla correspondiente según el rol
                            navigateToScreenByRol(rol)
                        } else {
                            Log.e("LoginScreenViewModel", "observeAuthState: Rol es null")
                        }
                        _isLoggedIn.value = true

                    }

                    is EstadoAutenticacion.LoggedInWithPendingRol -> {
                        _errorMessage.value = null
                        _successMessage.value = "Inicio de sesión exitoso, obteniendo rol..."
                        _navigateTo.value = null
                        _isLoggedIn.value = true // Mantener isLoggedIn en true
                    }

                    is EstadoAutenticacion.LoggedOut -> {
                        _errorMessage.value = null
                        _successMessage.value = null
                        _isLoggedIn.value = false
                        _navigateTo.value = null
                    }

                    is EstadoAutenticacion.Error -> {
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
                _userRol.value = rol ?: Rol.USUARIO // Agregar esta linea
                navigateToScreenByRol(rol ?: Rol.USUARIO) // Agregar esta linea
            }
        }
    }

    private fun navigateToScreenByRol(rol: Rol) {
        when (rol) {
            Rol.ADMINISTRADOR -> _navigateTo.value = Destinos.Pantalla1
            Rol.EMISORA -> _navigateTo.value = Destinos.EmisoraVista
            Rol.USUARIO -> _navigateTo.value = Destinos.UsuarioPerfilScreen // Navega al perfil
            else -> {
                Log.e("LoginScreenViewModel", "observeAuthState: Rol no reconocido: $rol")
                _errorMessage.value = "Rol no reconocido"
                _navigateTo.value = null
            }
        }
    }

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
        selectedRol: Rol?, // Ahora el rol es nullable
        latitud: Double? = null,
        longitud: Double? = null
    ) {
        Log.d(
            "LoginScreenViewModel",
            "crearUsuarioConCorreoYContrasena: Creando usuario con correo y contraseña"
        )
        viewModelScope.launch {
            _isLoading.value = true
            if (selectedRol != null) {
                authService.crearUsuarioConCorreoYContrasena(
                    email,
                    password,
                    selectedRol,
                    latitud,
                    longitud
                )
                // Guardar que no ha completado el formulario
                if (selectedRol == Rol.USUARIO) {
                    dataStoreManager.saveHasCompletedForm(false)
                }
            } else {
                _errorMessage.value = "Por favor, selecciona un rol."
                Log.e("LoginScreenViewModel", "crearUsuarioConCorreoYContrasena: Rol es null")
            }
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
                    _errorMessage.value = "Por favor, selecciona un rol."
                    Log.e("LoginScreenViewModel", "iniciarSesionConGoogle: Rol es null")
                }
                // El manejo del éxito y la navegación ahora se hace en observeAuthState
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión con Google: ${e.message}"
                Log.e("LoginScreenViewModel", "iniciarSesionConGoogle: Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}


