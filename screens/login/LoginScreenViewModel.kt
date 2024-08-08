//@file:Suppress("UNREACHABLE_CODE")

package com.jcmateus.casanarestereo.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import com.jcmateus.casanarestereo.model.User
import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.DefaultExecutor.delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginScreenViewModel: ViewModel() {
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
    private var _passwordVisible = mutableStateOf(false)
    val passwordVisible: State<Boolean> = _passwordVisible


    fun updatePasswordVisible(isVisible: Boolean) {_passwordVisible.value = isVisible
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    fun clearSuccessMessage() {
        _successMessage.value = null
    }

   // Login with Google
    fun signInWithGoogleCredential(credential: AuthCredential, home:()->Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("CasanareStereo", "Logueado con Google Exitoso")
                        _successMessage.value = "¡Logueado con éxito!"
                        home()
                    }
                }
                .addOnFailureListener {
                    Log.d("CasanareStereo", "Error al logear con Google")
                    _errorMessage.value = "Credenciales incorrectas"
                }
        }        
        catch (ex: Exception){
            Log.d("CasanareStereo", "Excepción al logear con Google" +
                    ex.localizedMessage
            )
        }
    }

    // Login with Correo y Contraseña
    fun signInWithEmailAndPassword(
        email:String,
        password:String,
        checkNotificaciones: Boolean,
        checkTerminos: Boolean,
        home: ()-> Unit
    )
        = viewModelScope.launch {
        val valido =email.trim().isNotEmpty() && password.trim().isNotEmpty() && checkNotificaciones && checkTerminos
        if(valido){
            try {
                _loading.value = true
                auth.signInWithEmailAndPassword(email, password, )
                    .addOnCompleteListener { task ->
                        _loading.value = false
                        if (task.isSuccessful) {
                            Log.d("CasanareStereo", "signInWithEmailAndPassword logueado!!")
                            _successMessage.value = "¡Logeado con éxito!"
                            viewModelScope.launch { // Lanzar una nueva corrutina
                                delay(1000) // Esperar 1 segundo
                                home()
                            }
                        }else{
                            // Manejo de la excepción FirebaseAuthInvalidCredentialsException
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                val authException = task.exception as FirebaseAuthInvalidCredentialsException
                                when (authException.errorCode) {
                                    "ERROR_WRONG_PASSWORD" -> {
                                        _errorMessage.value ="Contraseña incorrecta"
                                    }
                                    "ERROR_USER_NOT_FOUND" -> {
                                        _errorMessage.value = "Usuario no encontrado"
                                    }
                                    "ERROR_INVALID_EMAIL" -> {
                                        _errorMessage.value = "Correo electrónico inválido"
                                    }
                                    else -> {
                                        _errorMessage.value = "Error al iniciar sesión"
                                    }
                                }
                            }else {
                                // Verificar si la tarea ha sido exitosa antes de acceder a task.result
                                if (task.isSuccessful) {
                                    Log.d("CasanareStereo", "signInWithEmailAndPassword: ${task.result}")
                                }
                                Log.d("CasanareStereo", "signInWithEmailAndPassword: ${task.result}")
                                _errorMessage.value = "Credenciales incorrectas"
                            }
                        }
                    }

            } catch (e: FirebaseAuthInvalidCredentialsException){
                _loading.value = false // Ocultar indicador de progreso
                Log.d("CasanareStereo", "signInWithEmailAndPassword : ${e.message}"
                )
                _errorMessage.value = when (e.errorCode) {
                    "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta"
                    "ERROR_USER_NOT_FOUND" -> "Usuario no encontrado"
                    "ERROR_INVALID_EMAIL" -> "Correo electrónico inválido"
                    else -> "Credenciales incorrectas"
                }
            }catch (e: FirebaseAuthInvalidUserException) {
                // Manejar usuario inválido
                _errorMessage.value = "Usuario no encontrado o deshabilitado"
            }
        }
        isCreateUser = false
        _isLoggedIn.value = true // Actualiza el estado de inicio de sesión
        }
    fun clearIsLoggedIn() {
        _isLoggedIn.value = false
    }
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        checkNotificaciones: Boolean,
        checkTerminos: Boolean,
        home: () -> Unit
    ) {
        val valido = email.trim().isNotEmpty() && password.trim().isNotEmpty() && checkNotificaciones && checkTerminos
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
                                                val displayName = task.result.user?.email?.split("@")?.get(0)
                                                createUser(displayName)
                                                _successMessage.value = "¡Cuenta creada con éxito!"
                                                viewModelScope.launch {
                                                    delay(1000)
                                                    home()
                                                }
                                            } else {
                                                try {
                                                    throw task.exception!!
                                                } catch (e: FirebaseAuthUserCollisionException) {
                                                    viewModelScope.launch(Dispatchers.Main) {
                                                        _errorMessage.value = "El correo electrónico ya está en uso"
                                                    }
                                                } catch (e: Exception) {
                                                    viewModelScope.launch(Dispatchers.Main) {
                                                        Log.d("CasanareStereo", "createUserWithEmailAndPassword: ${task.result}")
                                                        _errorMessage.value = e.message ?: "Error al crear la cuenta"
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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid?: return
        //val user = mutableMapOf<String, Any>()

        //user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()

        //Usando Data Class
        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Hola que tal",
            profession = "Estudiante",
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
                viewModelScope.launch{
                    // Retrasar la navegación
                    delay(1000) // Esperar 1 segundo
                }

                Log.w("CasanareStereo", "Error de creacion de usuario", e)
            }

    }
}




