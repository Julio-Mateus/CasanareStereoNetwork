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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginScreenViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage
    val loading: LiveData<Boolean> = _loading
    private val _successMessage = MutableLiveData<String?>(null)
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    val successMessage: LiveData<String?> = _successMessage
    var isCreateUser = false
    private val _authSuccess = MutableSharedFlow<Boolean>()
    val authSuccess: SharedFlow<Boolean> = _authSuccess.asSharedFlow()

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    // Configuración de persistencia de autenticación
    private val _authState = MutableStateFlow<EstadoAutenticacion>(EstadoAutenticacion.Loading)
    val authState: StateFlow<EstadoAutenticacion> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("LoginScreenViewModel", "Valor inicial de _authState: ${_authState.value}") // Log para el valor inicial
            dataStoreManager.getIsLoggedIn().collect { isLoggedIn ->
                _authState.value = if (isLoggedIn) {
                    EstadoAutenticacion.LoggedIn
                } else {
                    EstadoAutenticacion.LoggedOut
                }
                Log.d("LoginScreenViewModel", "Valor de _authState después de DataStore: ${_authState.value}") // Log después de DataStore
            }
            // Verificar si hay un usuario actual en FirebaseAuth
            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.d("LoginScreenViewModel", "Usuario actual: ${currentUser.uid}") // Log del usuario actual
                _authSuccess.emit(true) // Indicar que el usuario ya ha iniciado sesión
            } else {
                Log.d("LoginScreenViewModel", "No hay usuario actual") // Log si no hay usuario
                _authSuccess.emit(false) // Indicar que el usuario no ha iniciado sesión
            }
        }
        setupAuthStateListener()

    }

    private var authStateListener: FirebaseAuth.AuthStateListener? =null

    private fun setupAuthStateListener() {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                viewModelScope.launch {
                    dataStoreManager.saveIsLoggedIn(true)
                    _isLoggedIn.value = true
                    _authState.value = EstadoAutenticacion.LoggedIn
                    Log.d("LoginScreenViewModel", "Usuario autenticado detectado: ${auth.currentUser?.uid}") // Log en AuthStateListener
                }
            } else {
                viewModelScope.launch {
                    dataStoreManager.saveIsLoggedIn(false)
                    _isLoggedIn.value = false
                    _authState.value = EstadoAutenticacion.LoggedOut}
                Log.d("LoginScreenViewModel", "No hay usuario autenticado") // Log en AuthStateListener
            }
        }
        authStateListener = listener
        FirebaseAuth.getInstance().addAuthStateListener(listener)
    }


    // Login with Google
    fun signInWithGoogleCredential(context: Context, credential: AuthCredential, rol: Rol?, home: () -> Unit) =
        viewModelScope.launch {
            try {
                _loading.value = true
                val authResult = auth.signInWithCredential(credential).await()
                val user = authResult.user
                if (user != null) {
                    delay(100) // Retraso
                    Log.d("CasanareStereo", "Logueado con Google Exitoso")
                    dataStoreManager.saveIsLoggedIn(true) // Guardar el estado de inicio de sesión
                    (context.applicationContext as HomeApplication).showScaffold = true
                    _successMessage.value = "¡Logueado con éxito!"
                    _authState.value = EstadoAutenticacion.LoggedIn
                    _authSuccess.emit(true)
                    home()
                } else {
                    Log.d("CasanareStereo", "Error al logear con Google")
                    _errorMessage.value = "Credenciales incorrectas"
                    _authState.value = EstadoAutenticacion.LoggedOut // Cambiar a LoggedOut si falla
                    _authSuccess.emit(false)
                }
            } catch (ex: Exception) {
                Log.d("CasanareStereo", "Excepción al logear con Google: ${ex.localizedMessage}")
                _errorMessage.value =
                    "Error al iniciar sesión con Google: ${ex.message}" // Mostrar mensaje de error
                _authState.value = EstadoAutenticacion.LoggedOut // Cambiar a LoggedOut si falla
                _authSuccess.emit(false)
            } finally {
                _loading.value = false
            }
        }

    // Login with Correo y Contraseña
    fun signInWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        rol: Rol?,
        home: () -> Unit
    ) = viewModelScope.launch {
        _authState.value = EstadoAutenticacion.Loading
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                (context.applicationContext as HomeApplication).showScaffold = true
                _authState.value = EstadoAutenticacion.LoggedIn
                viewModelScope.launch {
                    dataStoreManager.saveIsLoggedIn(true)
                    home()
                }
            } else {
                _authState.value = EstadoAutenticacion.LoggedOut
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Manejar credenciales inválidas
            _errorMessage.value = when (e.errorCode) {
                "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta"
                "ERROR_USER_NOT_FOUND" -> "Usuario no encontrado"
                "ERROR_INVALID_EMAIL" -> "Correo electrónico inválido"
                else -> "Credenciales incorrectas"
            }
            _authState.value = EstadoAutenticacion.LoggedOut
        } catch (e: FirebaseAuthInvalidUserException) {
            // Manejar usuario inválido
            _errorMessage.value = "Usuario no encontrado o deshabilitado"
            _authState.value = EstadoAutenticacion.LoggedOut
        } catch (e: Exception) {
            // Manejar otras excepciones
            Log.e("LoginScreenViewModel", "Error al iniciar sesión", e)
            _errorMessage.value = "Error al iniciar sesión: ${e.message}"
            _authState.value = EstadoAutenticacion.LoggedOut
        }

    }

    override fun onCleared() {
        super.onCleared()
        authStateListener?.let { FirebaseAuth.getInstance().removeAuthStateListener(it) }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        checkNotificaciones: Boolean,
        checkTerminos: Boolean,
        rol: Rol?,
        home: () -> Unit
    ) {

        val valido = email.trim().isNotEmpty() && password.trim()
            .isNotEmpty() && checkNotificaciones && checkTerminos
        if (valido) {
            if (_loading.value == true) return
            if (!isValidEmail(email)) {
                _errorMessage.value = "Correo electrónico inválido"
                return
            }
            if (password.length < 6) {
                _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
                return
            }
            _loading.value = true

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    auth.fetchSignInMethodsForEmail(email)
                        .addOnSuccessListener { methods ->
                            viewModelScope.launch(Dispatchers.Main) {
                                if (methods.signInMethods?.isNotEmpty() == true) {
                                    _errorMessage.value = "El correo electrónico ya está en uso"
                                    _loading.value = false
                                } else {
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val displayName =
                                                    task.result.user?.email?.split("@")?.get(0)
                                                createUser(displayName)
                                                _successMessage.value = "¡Cuenta creada con éxito!"
                                                viewModelScope.launch {
                                                    _authSuccess.emit(false)
                                                }
                                            } else {
                                                try {
                                                    throw task.exception!!
                                                } catch (e: FirebaseAuthUserCollisionException) {
                                                    viewModelScope.launch(Dispatchers.Main) {
                                                        _errorMessage.value =
                                                            "El correo electrónico ya está en uso"
                                                    }
                                                } catch (e: Exception) {
                                                    viewModelScope.launch(Dispatchers.Main) {
                                                        Log.d(
                                                            "CasanareStereo",
                                                            "createUserWithEmailAndPassword: ${task.result}"
                                                        )
                                                        _errorMessage.value =
                                                            e.message ?: "Error al crear la cuenta"
                                                    }
                                                } finally {
                                                    viewModelScope.launch(Dispatchers.Main) {
                                                        _loading.value = false
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            viewModelScope.launch(Dispatchers.Main) {
                                _errorMessage.value = e.message ?: "Error desconocido"
                                _loading.value = false
                            }
                        }
                } catch (e: Exception) {
                    viewModelScope.launch(Dispatchers.Main) {
                        _errorMessage.value = e.message ?: "Error desconocido"
                        _loading.value = false
                    }
                }
            }
        } else {
            _errorMessage.value = "Debes aceptar las notificaciones y los términos y condiciones."
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Implementa tu lógica de validación de correo electrónico
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid ?: return
        //val user = mutableMapOf<String, Any>()

        //user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()

        //Usando Data Class
        val user = User(
            userId = userId,
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "",
            profession = "",
            rol = "",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .document(userId) // Usar el ID del usuario como ID del documento
            .set(user)
            .addOnSuccessListener {
                _successMessage.value = "¡Creado con éxito!"
                Log.d("CasanareStereo", "Creado con exito: $it.id")
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Error al crear la cuenta + ${e.message}"
                viewModelScope.launch {
                    // Retrasar la navegación
                    //delay(1000) // Esperar 1 segundo
                }

                Log.w("CasanareStereo", "Error de creacion de usuario", e)
            }

    }

    fun cerrarSesion() {
        viewModelScope.launch {
            auth.signOut()
            dataStoreManager.saveIsLoggedIn(false)
            _authState.value = EstadoAutenticacion.LoggedOut
            _authSuccess.emit(false) // Indicar que el usuario ha cerrado sesión
        }
    }

    // Función para guardar el rol del usuario (adaptar según tu implementación)
    private fun guardarRolUsuario(rol: Rol?) {
        if (rol != null) {
            // Guardar el rol en la base de datos o en SharedPreferences
            // ...
        }
    }
}




