package com.jcmateus.casanarestereo.screens.login

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

class AuthService(private val firebaseAuth: FirebaseAuth) {

    // Flujo de estado de autenticación
    private val _authState = MutableStateFlow<EstadoAutenticacion>(EstadoAutenticacion.Loading)
    val authState: StateFlow<EstadoAutenticacion> = _authState.asStateFlow()


    init {
        // Observa los cambios de estado de autenticación de Firebase
        firebaseAuth.addAuthStateListener { auth ->
            _authState.value = if (auth.currentUser != null) {
                EstadoAutenticacion.LoggedIn(auth.currentUser, null) // Asumiendo que Rol es anulable
            } else {
                EstadoAutenticacion.LoggedOut
            }
        }
    }

    fun actualizarEstadoAutenticacion() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // El usuario ha iniciado sesión, actualiza el estado de autenticación
            _authState.value = EstadoAutenticacion.LoggedIn(currentUser, null)
        } else {
            // El usuario no ha iniciado sesión
            _authState.value = EstadoAutenticacion.LoggedOut
        }
    }

    // Iniciar sesión con correo electrónico y contraseña
    suspend fun iniciarSesionConCorreoYContrasena(email: String, password: String, selectedRol: Rol?): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            // Verificar si el usuario ya existe en Firestore
            val userDocument = FirebaseFirestore.getInstance().collection("users").document(user!!.uid).get().await()
            if (!userDocument.exists()) {
                // Si el usuario no existe, crear una nueva cuenta
                val displayName = user.displayName
                createUser(displayName, selectedRol) // Usar selectedRol o Rol.USUARIO por defecto
            } else {
                // Si el usuario ya existe, actualizar el rol si es necesario
                val currentRol = userDocument.getString("rol")
                if (currentRol == null) {
                    FirebaseFirestore.getInstance().collection("users").document(user.uid)
                        .update("rol", selectedRol?.name)
                        .await()
                }
            }

            user
        } catch (e: Exception) {
            // Manejar excepciones (por ejemplo, FirebaseAuthInvalidCredentialsException)
            null
        }
    }

    // Iniciar sesión con credencial de Google
    suspend fun iniciarSesionConGoogle(credential: AuthCredential, selectedRol: Rol?): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            // Verificar si el usuario ya existe en Firestore
            val userDocument = FirebaseFirestore.getInstance().collection("users").document(user!!.uid).get().await()

            val rol: Rol? = if (!userDocument.exists()) {
                // Si el usuario no existe, crear una nueva cuenta
                val displayName = user.displayName
                createUser(displayName, selectedRol) // Usar selectedRol o Rol.USUARIO por defecto
                selectedRol // Asignar el rol seleccionado o USUARIO por defecto
            } else {
                // Si el usuario ya existe, obtener el rol de Firestore
                userDocument.getString("rol")?.let { Rol.valueOf(it) } // Convertir a Rol
            }

            // Actualizar el estado de autenticación con el rol
            _authState.value = EstadoAutenticacion.LoggedIn(user, selectedRol)

            user
        } catch (e: Exception) {
            // Manejar excepciones
            null
        }
    }

    // Cerrar sesión
    fun cerrarSesion() {
        firebaseAuth.signOut()
    }

    // Validar correo electrónico
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    data class User(
        val id: String?,
        val userId: String,
        val displayName: String,
        val avatarUrl: String,
        val quote: String,
        val profession: String,
        val isFirstTime: Boolean = true,
        val rol: String

    ){
        fun toMap(): Map<String, Any?>{
            return mapOf(
                "id" to id,
                "userId" to userId,
                "displayName" to displayName,
                "avatarUrl" to avatarUrl,
                "quote" to quote,
                "profession" to profession,
                "isFirstTime" to isFirstTime,
                "rol" to rol
            )
        }
    }

    // Crear usuario en Firebase Authentication y Firestore
    suspend fun createUser(displayName: String?, rol: Rol?): Boolean {
        val userId = firebaseAuth.currentUser?.uid ?: return false

        val user = User( // Crear una instancia de User
            id = null,
            userId = userId,
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "",
            profession = "",
            rol = rol.toString() // Usar rol o Rol.USUARIO por defecto
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


}