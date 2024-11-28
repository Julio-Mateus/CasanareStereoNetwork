package com.jcmateus.casanarestereo.screens.login

import android.annotation.SuppressLint
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.collections.toMap
import kotlin.text.get
import kotlin.text.set

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
    suspend fun iniciarSesionConCorreoYContrasena(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // Actualizar _authState después de iniciar sesión correctamente
            _authState.value = EstadoAutenticacion.LoggedIn(result.user, null) // O con el rol si lo tienes
            result.user
        } catch (e: Exception) {
            // Manejar excepciones (por ejemplo, FirebaseAuthInvalidCredentialsException)
            null
        }
    }

    // Iniciar sesión con credencial de Google
    suspend fun iniciarSesionConCredencialDeGoogle(credential: AuthCredential): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            // Verificar si el usuario ya existe en Firestore
            val userDocument = FirebaseFirestore.getInstance().collection("users").document(user!!.uid).get().await()
            if (!userDocument.exists()) {
                // Si el usuario no existe, crear una nueva cuenta
                val displayName = user.displayName
                val rol = Rol.USUARIO // O el rol que desees asignar por defecto
                createUser(displayName, rol)
            }

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
            rol = rol?.name ?: Rol.USUARIO.name
        )

        return try {
            FirebaseFirestore.getInstance().collection("users")
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