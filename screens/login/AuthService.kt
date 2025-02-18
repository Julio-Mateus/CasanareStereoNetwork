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
            // Si hay un usuario, obtener el rol y actualizar el estado
            viewModelScope.launch {
                val rol = obtenerRolUsuario(currentUser.uid)
                if (rol != null) {
                    _authState.value = EstadoAutenticacion.LoggedIn(rol)
                    dataStoreManager.saveIsLoggedIn(true)
                    Log.d("AuthService", "checkSession: isLoggedIn guardado como true")
                } else {
                    _authState.value = EstadoAutenticacion.LoggedOut
                    dataStoreManager.saveIsLoggedIn(false)
                    Log.d("AuthService", "checkSession: isLoggedIn guardado como false")
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
    suspend fun obtenerRolUsuario(userId: String): Rol? {
        Log.d("AuthService", "obtenerRolUsuario: Obteniendo rol del usuario: $userId")
        var rol: Rol? = null

        // Primero, intenta obtener el rol de la colección 'emisoras'
        try {
            val emisoraDoc = db.collection("emisoras").document(userId).get().await()
            if (emisoraDoc.exists()) {
                rol = Rol.EMISORA
                Log.d("AuthService", "obtenerRolUsuario: Usuario con rol definido: EMISORA")
                return rol
            }
        } catch (e: Exception) {
            Log.e("AuthService", "Error al obtener el rol de 'emisoras'", e)
        }

        // Si no se encontró en 'emisoras', intenta obtener el rol de la colección 'usuarios'
        try {
            val usuarioDoc = db.collection("usuarios").document(userId).get().await()
            if (usuarioDoc.exists()) {
                rol = Rol.USUARIO
                Log.d("AuthService", "obtenerRolUsuario: Usuario con rol definido: USUARIO")
                return rol
            }
        } catch (e: Exception) {
            Log.e("AuthService", "Error al obtener el rol de 'usuarios'", e)
        }

        // Si no se encontró en ninguna colección, intenta obtener el rol de la colección 'administradores'
        try {
            val administradorDoc = db.collection("administradores").document(userId).get().await()
            if (administradorDoc.exists()) {
                rol = Rol.ADMINISTRADOR
                Log.d("AuthService", "obtenerRolUsuario: Usuario con rol definido: ADMINISTRADOR")
                return rol
            }
        } catch (e: Exception) {
            Log.e("AuthService", "Error al obtener el rol de 'administradores'", e)
        }

        Log.d("AuthService", "obtenerRolUsuario: Usuario sin rol definido")
        return rol
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
        selectedRol: Rol?,
        latitud: Double? = null,
        longitud: Double? = null
    ) {
        Log.d(
            "AuthService",
            "verificarYCrearUsuarioEnFirestore: Verificando usuario en Firestore: ${user.uid} con rol: $selectedRol"
        )
        try {
            val userId = user.uid
            val collection = when (selectedRol) {
                Rol.EMISORA -> "emisoras"
                else -> "usuarios" // Para ADMINISTRADOR y USUARIO
            }

            val userDocument = db.collection(collection).document(userId).get().await()

            if (!userDocument.exists()) {
                Log.d(
                    "AuthService",
                    "verificarYCrearUsuarioEnFirestore: Usuario no encontrado en '$collection', creando..."
                )
                // Crear el objeto correcto según el rol
                val newUser = when (selectedRol) {
                    Rol.EMISORA -> {
                        PerfilEmisora(
                            id = userId,
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
                    }
                    else -> { // Rol.USUARIO o Rol.ADMINISTRADOR
                        PerfilUsuario(
                            uid = userId,
                            nombre = user.displayName ?: "Sin nombre",
                            email = user.email ?: "",
                            rol = selectedRol?.name.toString(),
                            avatarUrl = null,
                            frase = null,
                            profesion = null,
                            emisorasFavoritas = emptyList(),
                            ciudad = null,
                            departamento = null,
                            latitud = latitud,
                            longitud = longitud
                        )
                    }
                }

                // Guardar el objeto en Firestore
                if (newUser is PerfilEmisora) {
                    db.collection(collection).document(userId).set(newUser).await()
                } else if (newUser is PerfilUsuario) {
                    db.collection(collection).document(userId).set(newUser.toMap()).await()
                }

                Log.d(
                    "AuthService",
                    "verificarYCrearUsuarioEnFirestore: Usuario creado en '$collection'"
                )
            } else {
                Log.d(
                    "AuthService",
                    "verificarYCrearUsuarioEnFirestore: Usuario encontrado en '$collection'"
                )
                // Si el usuario ya existe, actualizar el rol y la ubicación si es necesario
                val updates = mutableMapOf<String, Any>()
                updates["rol"] = selectedRol?.name as Any
                if (latitud != null) {
                    updates["latitud"] = latitud
                }
                if (longitud != null) {
                    updates["longitud"] = longitud
                }
                if (updates.isNotEmpty()) {
                    db.collection(collection).document(userId).update(updates).await()
                    Log.d(
                        "AuthService",
                        "verificarYCrearUsuarioEnFirestore: Datos de usuario actualizados en '$collection'"
                    )
                } else {
                    Log.d(
                        "AuthService",
                        "verificarYCrearUsuarioEnFirestore: No hay cambios en los datos del usuario en '$collection'"
                    )
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
                val rol = obtenerRolUsuario(user.uid)
                _authState.value = EstadoAutenticacion.LoggedIn(rol)
                dataStoreManager.saveIsLoggedIn(true)
                Log.d("AuthService", "crearUsuarioConCorreoYContrasena: isLoggedIn guardado como true")
                checkSession()
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
                val rol = obtenerRolUsuario(user.uid)
                _authState.value = EstadoAutenticacion.LoggedIn(rol)
                dataStoreManager.saveIsLoggedIn(true)
                Log.d("AuthService", "iniciarSesionConCorreoYContrasena: isLoggedIn guardado como true")
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
        selectedRol: Rol?
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
                verificarYCrearUsuarioEnFirestore(user, selectedRol)
                // Obtener el rol del usuario
                val rol = obtenerRolUsuario(user.uid)
                _authState.value = EstadoAutenticacion.LoggedIn(rol)
                dataStoreManager.saveIsLoggedIn(true)
                Log.d("AuthService", "iniciarSesionConGoogle: isLoggedIn guardado como true")
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
}


