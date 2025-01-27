package com.jcmateus.casanarestereo.screens.login


import android.content.Context
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

// Mover la función de extensión fuera de la clase
fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

class AuthService(
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager
) {

    // Flujo de estado de autenticación
    private val _authState = MutableStateFlow<EstadoAutenticacion>(EstadoAutenticacion.Loading)
    val authState: StateFlow<EstadoAutenticacion> = _authState.asStateFlow()

    init {
        // Observa los cambios de estado de autenticación de Firebase
        firebaseAuth.addAuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                // El usuario ha iniciado sesión, actualiza el estado de autenticación
                _authState.value = EstadoAutenticacion.LoggedInWithPendingRol(user)
                // Obtener el rol del usuario desde Firestore
                FirebaseFirestore.getInstance().collection("users").document(user.uid).get()
                    .addOnSuccessListener { document ->
                        val rol = document.getString("rol")?.let { Rol.valueOf(it) }
                        _authState.value = EstadoAutenticacion.LoggedIn(user, rol)
                    }
                    .addOnFailureListener { exception ->
                        Log.e(
                            "AuthService",
                            "Error al obtener el rol del usuario: ${exception.message}"
                        )
                        _authState.value = EstadoAutenticacion.Error("Error al obtener el rol")
                    }
            } else {
                // El usuario no ha iniciado sesión
                _authState.value = EstadoAutenticacion.LoggedOut
            }
        }
    }

    // Obtener el usuario actual
    fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            nombre = firebaseUser.displayName,
            email = firebaseUser.email,
            avatarUrl = firebaseUser.photoUrl?.toString()
        )
    }

    // Verificar si el usuario existe en Firestore y crearlo si es necesario
    private suspend fun verificarYCrearUsuarioEnFirestore(user: FirebaseUser, selectedRol: Rol?) {
        val userDocument =
            FirebaseFirestore.getInstance().collection("users").document(user.uid).get().await()
        if (!userDocument.exists()) {
            // Si el usuario no existe, crear una nueva cuenta
            val displayName = user.displayName
            createUser(displayName, selectedRol) // Usar selectedRol o Rol.NO_DEFINIDO por defecto
        } else {
            // Si el usuario ya existe, obtener el rol de Firestore
            val currentRol = userDocument.getString("rol")
            if (currentRol == null) {
                FirebaseFirestore.getInstance().collection("users").document(user.uid)
                    .update("rol", selectedRol?.name)
                    .await()
            }
        }
    }

    // Iniciar sesión con correo electrónico y contraseña
    suspend fun iniciarSesionConCorreoYContrasena(
        email: String,
        password: String,
        password1: String,
        selectedRol: Rol?
    ): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user!!
            verificarYCrearUsuarioEnFirestore(user, selectedRol)
            // Guardar el rol en DataStore
            dataStoreManager.saveRolUsuario(selectedRol ?: Rol.NO_DEFINIDO)
            // Guardar que el usuario esta logueado
            dataStoreManager.saveIsLoggedIn(true)
            // Actualizar el estado de autenticación con el rol
            _authState.value = EstadoAutenticacion.LoggedIn(user, selectedRol)
            user
        } catch (e: Exception) {
            _authState.value =
                EstadoAutenticacion.Error("Error al iniciar sesión con correo y contraseña.")
            null
        }
    }

    // Iniciar sesión con credencial de Google
    suspend fun iniciarSesionConGoogle(
        context: Context,
        credential: AuthCredential,
        selectedRol: Rol?
    ): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user!!
            verificarYCrearUsuarioEnFirestore(user, selectedRol)
            // Guardar el rol en DataStore
            dataStoreManager.saveRolUsuario(selectedRol ?: Rol.NO_DEFINIDO)
            // Guardar que el usuario esta logueado
            dataStoreManager.saveIsLoggedIn(true)
            // Actualizar el estado de autenticación con el rol
            _authState.value = EstadoAutenticacion.LoggedIn(user, selectedRol)
            user
        } catch (e: Exception) {
            // Manejar excepciones
            _authState.value = EstadoAutenticacion.Error("Error al iniciar sesión con Google.")
            null
        }
    }

    // Cerrar sesión
    suspend fun cerrarSesion() {
        firebaseAuth.signOut()
        dataStoreManager.saveIsLoggedIn(false)
        _authState.value = EstadoAutenticacion.LoggedOut
    }

    // Validar correo electrónico
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    data class User(
        val uid: String? = null,
        val nombre: String? = null,
        val email: String? = null,
        val avatarUrl: String? = null,
        val quote: String? = null,
        val profession: String? = null,
        val isFirstTime: Boolean = true,
        val rol: String? = null,
        val emisorasFavoritas: List<String> = emptyList(),
        val ubicacion: String? = null
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "uid" to uid,
                "nombre" to nombre,
                "email" to email,
                "avatarUrl" to avatarUrl,
                "quote" to quote,
                "profession" to profession,
                "isFirstTime" to isFirstTime,
                "rol" to rol,
                "emisorasFavoritas" to emisorasFavoritas,
                "ubicacion" to ubicacion
            )
        }
    }

    // Crear usuario en Firebase Authentication y Firestore
    suspend fun createUser(displayName: String?, rol: Rol?): Boolean {
        val userId = firebaseAuth.currentUser?.uid ?: return false

        val user = User( // Crear una instancia de User
            uid = userId,
            nombre = displayName,
            email = firebaseAuth.currentUser?.email,
            avatarUrl = null,
            quote = null,
            profession = null,
            rol = rol?.name ?: Rol.NO_DEFINIDO.name // Usar rol o Rol.NO_DEFINIDO por defecto
        )
        val collection = if (rol == Rol.EMISORA) "emisoras" else "users"

        return try {
            FirebaseFirestore.getInstance().collection(collection)
                .document(userId)
                .set(user.toMap()) // Utilizar toMap() para obtener el mapa
                .await()
            true
        } catch (e: Exception) {
            // Manejar la excepción
            Log.e("AuthService", "Error al crear el usuario: ${e.message}")
            false
        }
    }

    // Crear usuario con correo y contraseña
    suspend fun crearUsuarioConCorreoYContrasena(
        email: String,
        password: String,
        checkTerminos: Boolean,
        selectedRol: Rol?
    ) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                // Verificar y crear el usuario en Firestore
                verificarYCrearUsuarioEnFirestore(user, selectedRol)
                // Guardar el rol en DataStore
                dataStoreManager.saveRolUsuario(selectedRol ?: Rol.NO_DEFINIDO)
                // Actualizar el estado de autenticación con el rol
                _authState.value = EstadoAutenticacion.LoggedIn(user, selectedRol)
            } else {
                _authState.value = EstadoAutenticacion.Error("Error al crear el usuario")
            }
        } catch (e: Exception) {
            _authState.value = EstadoAutenticacion.Error("Error al crear el usuario: ${e.message}")
        }
    }
}