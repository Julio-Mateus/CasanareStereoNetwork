package com.jcmateus.casanarestereo.screens.login

import android.annotation.SuppressLint
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

    // Iniciar sesión con correo electrónico y contraseña
    suspend fun iniciarSesionConCorreoYContrasena(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
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
            authResult.user
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

    // Crear usuario en Firebase Authentication y Firestore
    suspend fun createUser(displayName: String?, rol: Rol?): Boolean {
        val userId = firebaseAuth.currentUser?.uid ?: return false

        val user = User(
            id = null, // Firestore generará un ID automáticamente
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
                .set(user.toMap()) // Guardar el mapa en Firestore
                .await()
            true // Usuario creado con éxito
        } catch (e: Exception) {
            // Manejar la excepción
            false // Error al crear el usuario
        }
    }


}