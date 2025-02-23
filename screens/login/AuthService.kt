package com.jcmateus.casanarestereo.screens.login

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlin.collections.toMap
import kotlin.text.get
import kotlin.text.matches
import kotlin.text.set

// Mover la función de extensión fuera de la clase
fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

class AuthService(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    // Flujo de estado de autenticación
    private val _authState = MutableStateFlow<EstadoAutenticacion>(EstadoAutenticacion.Loading)
    val authState: StateFlow<EstadoAutenticacion> = _authState.asStateFlow()

    init {
        Log.d("AuthService", "init: AuthService inicializado")
    }

    fun checkSession() {
        Log.d("AuthService", "checkSession: Verificando sesión...")
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Log.d("AuthService", "checkSession: Sesión activa encontrada para: ${currentUser.uid}")
            // El usuario está logueado, guardamos el valor en el datastore
            viewModelScope.launch {
                dataStoreManager.saveIsLoggedIn(true)
                dataStoreManager.saveUserId(currentUser.uid)
                Log.d("AuthService", "checkSession: isLoggedIn guardado como true")
                // Intentamos obtener el rol
                val rolResult = obtenerRolUsuario(currentUser.uid)
                when (rolResult) {
                    is RolResult.Success -> {
                        // Si se obtuvo el rol, actualizamos el estado
                        _authState.value = EstadoAutenticacion.LoggedIn(rolResult.rol)
                    }
                    is RolResult.Error -> {
                        // Si hubo un error, mostramos un mensaje de error y deslogueamos al usuario
                        Log.e("AuthService", "checkSession: Error al obtener el rol: ${rolResult.message}")
                        _authState.value = EstadoAutenticacion.Error(rolResult.message)
                        cerrarSesion()
                    }
                    is RolResult.NoRol -> {
                        // Si no se encontro el rol, mostramos un mensaje de error y deslogueamos al usuario
                        Log.e("AuthService", "checkSession: No se encontro el rol")
                        _authState.value = EstadoAutenticacion.Error("No se encontro el rol")
                        cerrarSesion()
                    }
                }
            }
        } else {
            Log.d("AuthService", "checkSession: No hay sesión activa")
            _authState.value = EstadoAutenticacion.LoggedOut
            viewModelScope.launch {
                dataStoreManager.saveIsLoggedIn(false)
                Log.d("AuthService", "checkSession: isLoggedIn guardado como false")
            }
        }
    }

    // Función para obtener el rol del usuario desde Firestore
    suspend fun obtenerRolUsuario(userId: String): RolResult {
        Log.d("AuthService", "obtenerRolUsuario: Obteniendo rol del usuario: $userId")
        try {
            // Primero, intenta obtener el rol de la colección 'usuarios'
            val usuarioDoc = db.collection("usuarios").document(userId).get().await()
            if (usuarioDoc.exists()) {
                val perfilUsuario = usuarioDoc.toObject(PerfilUsuario::class.java)
                val rol = perfilUsuario?.rol?.let { Rol.valueOf(it) }
                if (rol != null) {
                    Log.d("AuthService", "obtenerRolUsuario: Usuario con rol definido: ${rol.name}")
                    return RolResult.Success(rol)
                } else {
                    Log.e("AuthService", "obtenerRolUsuario: El rol es nulo")
                    return RolResult.NoRol
                }
            } else {
                // Si no se encuentra en 'usuarios', intenta buscar en 'emisoras'
                val emisoraDoc = db.collection("emisoras").document(userId).get().await()
                if (emisoraDoc.exists()) {
                    val perfilEmisora = emisoraDoc.toObject(PerfilEmisora::class.java)
                    val rol = perfilEmisora?.rol?.let { Rol.valueOf(it) }
                    if (rol != null) {
                        Log.d("AuthService", "obtenerRolUsuario: Emisora con rol definido: ${rol.name}")
                        dataStoreManager.saveEmisoraId(userId)
                        return RolResult.Success(rol)
                    } else {
                        Log.e("AuthService", "obtenerRolUsuario: El rol de la emisora es nulo")
                        return RolResult.NoRol
                    }
                } else {
                    Log.e("AuthService", "obtenerRolUsuario: No se encontro el usuario ni la emisora")
                    return RolResult.NoRol
                }
            }
        } catch (e: Exception) {
            Log.e("AuthService", "obtenerRolUsuario: Error al obtener el rol: ${e.message}")
            return RolResult.Error("Error al obtener el rol: ${e.message}")
        }
    }

    // Obtener el usuario actual
    fun getCurrentUser(): FirebaseUser? {
        Log.d("AuthService", "getCurrentUser: Obteniendo usuario actual")
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            Log.d("AuthService", "getCurrentUser: Usuario actual encontrado: ${firebaseUser.uid}")
        } else {
            Log.d("AuthService", "getCurrentUser: No hay usuario actual")
        }
        return firebaseUser
    }

    // Verificar si el usuario existe en Firestore y crearlo si es necesario
    suspend fun verificarYCrearUsuarioEnFirestore(
        user: FirebaseUser,
        selectedRol: Rol,
        latitud: Double? = null,
        longitud: Double? = null
    ) {
        Log.d(
            "AuthService",
            "verificarYCrearUsuarioEnFirestore: Verificando usuario en Firestore: ${user.uid} con rol: $selectedRol"
        )
        try {
            val userId = user.uid

            when (selectedRol) {
                Rol.EMISORA -> {
                    // Verificar si la emisora ya existe
                    val emisoraDoc = db.collection("emisoras").document(userId).get().await()
                    if (!emisoraDoc.exists()) {
                        Log.d(
                            "AuthService",
                            "verificarYCrearUsuarioEnFirestore: Emisora no encontrada, creando..."
                        )
                        // Crear la emisora
                        val nuevaEmisora = PerfilEmisora(
                            emisoraId = userId, // Usamos userId como emisoraId
                            nombre = user.displayName ?: "Sin nombre",
                            email = user.email ?: "",
                            rol = selectedRol.name,
                            descripcion = "",
                            imagenPerfilUri = "",
                            enlace = "",
                            paginaWeb = "",
                            ciudad = "",
                            departamento = "",
                            frecuencia = "",
                            latitud = latitud,
                            longitud = longitud
                        )
                        db.collection("emisoras").document(userId).set(nuevaEmisora).await()
                        Log.d(
                            "AuthService",
                            "verificarYCrearUsuarioEnFirestore: Emisora creada en 'emisoras'"
                        )
                        dataStoreManager.saveEmisoraId(userId)
                    } else {
                        Log.d(
                            "AuthService",
                            "verificarYCrearUsuarioEnFirestore: Emisora encontrada en 'emisoras'"
                        )
                        // Si la emisora ya existe, actualizar los datos
                        val updates = mutableMapOf<String, Any>()
                        updates["rol"] = selectedRol.name
                        if (latitud != null) {
                            updates["latitud"] = latitud
                        }
                        if (longitud != null) {
                            updates["longitud"] = longitud
                        }
                        if (updates.isNotEmpty()) {
                            db.collection("emisoras").document(userId).update(updates).await()
                            Log.d(
                                "AuthService",
                                "verificarYCrearUsuarioEnFirestore: Datos de emisora actualizados en 'emisoras'"
                            )
                        } else {
                            Log.d(
                                "AuthService",
                                "verificarYCrearUsuarioEnFirestore: No hay cambios en los datos de la emisora en 'emisoras'"
                            )
                        }
                    }
                    // Si el rol es EMISORA, NO se crea ni se actualiza en 'usuarios'
                }

                else -> { // Rol.USUARIO o Rol.ADMINISTRADOR
                    // Verificar si el usuario ya existe
                    val usuarioDoc = db.collection("usuarios").document(userId).get().await()
                    if (!usuarioDoc.exists()) {
                        Log.d(
                            "AuthService",
                            "verificarYCrearUsuarioEnFirestore: Usuario no encontrado, creando..."
                        )
                        // Crear el usuario
                        val newUser = PerfilUsuario(
                            uid = userId,
                            nombre = user.displayName ?: "Sin nombre",
                            email = user.email ?: "",
                            rol = selectedRol.name,
                            avatarUrl = null,
                            frase = null,
                            profesion = null,
                            emisorasFavoritas = emptyList(),
                            ciudad = null,
                            departamento = null,
                            latitud = latitud,
                            longitud = longitud
                        )
                        db.collection("usuarios").document(userId).set(newUser.toMap()).await()
                        Log.d(
                            "AuthService",
                            "verificarYCrearUsuarioEnFirestore: Usuario creado en 'usuarios'"
                        )
                    } else {
                        Log.d(
                            "AuthService",
                            "verificarYCrearUsuarioEnFirestore: Usuario encontrado en 'usuarios'"
                        )
                        // Si el usuario ya existe, actualizar los datos
                        val updates = mutableMapOf<String, Any>()
                        updates["rol"] = selectedRol.name
                        if (latitud != null) {
                            updates["latitud"] = latitud
                        }
                        if (longitud != null) {
                            updates["longitud"] = longitud
                        }
                        if (updates.isNotEmpty()) {
                            db.collection("usuarios").document(userId).update(updates).await()
                            Log.d(
                                "AuthService",
                                "verificarYCrearUsuarioEnFirestore: Datos de usuario actualizados en 'usuarios'"
                            )
                        } else {
                            Log.d(
                                "AuthService",
                                "verificarYCrearUsuarioEnFirestore: No hay cambios en los datos del usuario en 'usuarios'"
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(
                "AuthService",
                "verificarYCrearUsuarioEnFirestore: Error al verificar o crear el usuario en Firestore: ${e.message}"
            )
            _authState.value =
                EstadoAutenticacion.Error("Error al verificar o crear el usuario en Firestore: ${e.message}")
        }
    }

    // Crear usuario con correo y contraseña
    suspend fun crearUsuarioConCorreoYContrasena(
        email: String,
        password: String,
        selectedRol: Rol,
        latitud: Double? = null,
        longitud: Double? = null
    ) {
        Log.d(
            "AuthService",
            "crearUsuarioConCorreoYContrasena: Creando usuario con correo y contraseña"
        )
        _authState.value = EstadoAutenticacion.Loading
        Log.d("AuthService", "crearUsuarioConCorreoYContrasena: Estado actualizado a Loading")
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                Log.d(
                    "AuthService",
                    "crearUsuarioConCorreoYContrasena: Usuario creado: ${user.uid}"
                )
                // Verificar y crear el usuario en Firestore
                verificarYCrearUsuarioEnFirestore(user, selectedRol, latitud, longitud)
                // Obtener el rol del usuario
                val rolResult = obtenerRolUsuario(user.uid)
                when (rolResult) {
                    is RolResult.Success -> {
                        _authState.value = EstadoAutenticacion.LoggedIn(rolResult.rol)
                        dataStoreManager.saveIsLoggedIn(true)
                        Log.d("AuthService", "crearUsuarioConCorreoYContrasena: isLoggedIn guardado como true")
                    }
                    is RolResult.Error -> {
                        _authState.value = EstadoAutenticacion.Error(rolResult.message)
                        Log.e("AuthService", "crearUsuarioConCorreoYContrasena: Error al obtener el rol: ${rolResult.message}")
                    }
                    is RolResult.NoRol -> {
                        _authState.value = EstadoAutenticacion.Error("No se encontro el rol")
                        Log.e("AuthService", "crearUsuarioConCorreoYContrasena: No se encontro el rol")
                    }
                }
            } else {
                _authState.value =
                    EstadoAutenticacion.Error("Error al crear el usuario")
                Log.d("AuthService", "crearUsuarioConCorreoYContrasena: Estado actualizado a Error")
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            _authState.value = EstadoAutenticacion.Error("El correo electrónico ya está en uso")
            Log.e(
                "AuthService",
                "crearUsuarioConCorreoYContrasena: El correo electrónico ya está en uso: ${e.message}"
            )
        } catch (e: Exception) {
            _authState.value = EstadoAutenticacion.Error(e.message ?: "Error desconocido")
            Log.e(
                "AuthService",
                "crearUsuarioConCorreoYContrasena: Error desconocido: ${e.message}"
            )
        }
    }


    // Iniciar sesión con correo electrónico y contraseña
    suspend fun iniciarSesionConCorreoYContrasena(
        email: String,
        password: String
    ) {
        Log.d(
            "AuthService",
            "iniciarSesionConCorreoYContrasena: Iniciando sesión con correo y contraseña"
        )
        _authState.value = EstadoAutenticacion.Loading
        Log.d("AuthService", "iniciarSesionConCorreoYContrasena: Estado actualizado a Loading")
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                Log.d(
                    "AuthService",
                    "iniciarSesionConCorreoYContrasena: Usuario obtenido: ${user.uid}"
                )
                // Obtener el rol del usuario
                val rolResult = obtenerRolUsuario(user.uid)
                when (rolResult) {
                    is RolResult.Success -> {
                        _authState.value = EstadoAutenticacion.LoggedIn(rolResult.rol)
                        dataStoreManager.saveIsLoggedIn(true)
                        Log.d("AuthService", "iniciarSesionConCorreoYContrasena: isLoggedIn guardado como true")
                    }
                    is RolResult.Error -> {
                        _authState.value = EstadoAutenticacion.Error(rolResult.message)
                        Log.e("AuthService", "iniciarSesionConCorreoYContrasena: Error al obtener el rol: ${rolResult.message}")
                    }
                    is RolResult.NoRol -> {
                        _authState.value = EstadoAutenticacion.Error("No se encontro el rol")
                        Log.e("AuthService", "iniciarSesionConCorreoYContrasena: No se encontro el rol")
                    }
                }
            } else {
                _authState.value =
                    EstadoAutenticacion.Error("Error al obtener el usuario")
                Log.d("AuthService", "iniciarSesionConCorreoYContrasena: Estado actualizado a Error")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _authState.value = EstadoAutenticacion.Error("Credenciales inválidas")
            Log.e(
                "AuthService",
                "iniciarSesionConCorreoYContrasena: Credenciales inválidas: ${e.message}"
            )
        } catch (e: Exception) {
            _authState.value = EstadoAutenticacion.Error(e.message ?: "Error desconocido")
            Log.e(
                "AuthService",
                "iniciarSesionConCorreoYContrasena: Error desconocido: ${e.message}"
            )
        }
    }

    // Iniciar sesión con credencial de Google
    suspend fun iniciarSesionConGoogle(
        context: Context,
        credential: AuthCredential,
        selectedRol: Rol // Modificado a Rol (non-nullable)
    ) {
        Log.d("AuthService", "iniciarSesionConGoogle: Iniciando sesión con Google")
        _authState.value = EstadoAutenticacion.Loading
        Log.d("AuthService", "iniciarSesionConGoogle: Estado actualizado a Loading")
        try {
            // Intenta iniciar sesión con Firebase Authentication usando la credencial de Google
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            Log.d("AuthService", "iniciarSesionConGoogle: Inicio de sesión con Google exitoso")

            // Si el inicio de sesión es exitoso, obtenemos el usuario
            val user = authResult.user
            if (user != null) {
                Log.d("AuthService", "iniciarSesionConGoogle: Usuario obtenido: ${user.uid}")
                // Verificar y crear el usuario en Firestore
                verificarYCrearUsuarioEnFirestore(user, selectedRol) // Pasando selectedRol
                // Obtener el rol del usuario
                val rolResult = obtenerRolUsuario(user.uid)
                when (rolResult) {
                    is RolResult.Success -> {
                        _authState.value = EstadoAutenticacion.LoggedIn(rolResult.rol)
                        dataStoreManager.saveIsLoggedIn(true)
                        Log.d("AuthService", "iniciarSesionConGoogle: isLoggedIn guardado como true")
                    }
                    is RolResult.Error -> {
                        _authState.value = EstadoAutenticacion.Error(rolResult.message)
                        Log.e("AuthService", "iniciarSesionConGoogle: Error al obtener el rol: ${rolResult.message}")
                    }
                    is RolResult.NoRol -> {
                        _authState.value = EstadoAutenticacion.Error("No se encontro el rol")
                        Log.e("AuthService", "iniciarSesionConGoogle: No se encontro el rol")
                    }
                }
            } else {
                Log.e(
                    "AuthService",
                    "iniciarSesionConGoogle: Error al obtener el usuario después del inicio de sesión"
                )
                _authState.value = EstadoAutenticacion.Error("Error al obtener el usuario")
                Log.d("AuthService", "iniciarSesionConGoogle: Estado actualizado a Error")
            }
        } catch (e: Exception) {
            Log.e(
                "AuthService",
                "iniciarSesionConGoogle: Error al iniciar sesión con Google: ${e.message}"
            )
            _authState.value =
                EstadoAutenticacion.Error("Error al iniciar sesión con Google: ${e.message}")
            Log.d("AuthService", "iniciarSesionConGoogle: Estado actualizado a Error")
        }
    }

    // Cerrar sesión
    suspend fun cerrarSesion() {
        Log.d("AuthService", "cerrarSesion: Cerrando sesión")
        try {
            // Primero, actualiza el estado a LoggedOut
            _authState.value = EstadoAutenticacion.LoggedOut
            Log.d("AuthService", "cerrarSesion: Estado actualizado a LoggedOut")

            // Luego, cierra la sesión en Firebase
            firebaseAuth.signOut()
            Log.d("AuthService", "cerrarSesion: Sesión cerrada en Firebase")

            // Finalmente, actualiza el DataStore
            dataStoreManager.saveIsLoggedIn(false)
            Log.d("AuthService", "cerrarSesion: isLoggedIn guardado como false")
        } catch (e: Exception) {
            Log.e("AuthService", "cerrarSesion: Error al cerrar sesión: ${e.message}")
            _authState.value = EstadoAutenticacion.Error("Error al cerrar sesión: ${e.message}")
        }
    }

    // Validar correo electrónico
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    // Clase sellada para representar el resultado de obtenerRolUsuario
    sealed class RolResult {
        data class Success(val rol: Rol) : RolResult()
        data class Error(val message: String) : RolResult()
        object NoRol : RolResult()
    }
}


