package com.jcmateus.casanarestereo.screens.usuarios.usuario

import android.net.Uri
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.add
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.login.AuthService
import kotlinx.coroutines.tasks.await
import kotlin.collections.remove
import kotlin.collections.toMap
import kotlin.collections.toMutableList
import kotlin.text.contains

class UsuarioRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUsuario(uid: String): AuthService.User? {
        return try {
            val document = firestore.collection("usuarios").document(uid).get().await()
            if (document.exists()) {
                document.toObject(AuthService.User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            // Manejar la excepción
            null
        }
    }

    suspend fun updateUsuario(usuario: AuthService.User): Boolean {
        return try {
            firestore.collection("usuarios").document(usuario.uid!!).set(usuario.toMap()).await()
            true
        } catch (e: Exception) {
            // Manejar la excepción
            false
        }
    }

    suspend fun addEmisoraFavorita(uid: String, emisoraId: String): Boolean {
        return try {
            val usuario = getUsuario(uid)
            if (usuario != null) {
                val emisorasFavoritas = usuario.emisorasFavoritas.toMutableList()
                if (!emisorasFavoritas.contains(emisoraId)) {
                    emisorasFavoritas.add(emisoraId)
                    val updatedUsuario = usuario.copy(emisorasFavoritas = emisorasFavoritas)
                    updateUsuario(updatedUsuario)
                } else {
                    true
                }
            } else {
                false
            }
        } catch (e: Exception) {
            // Manejar la excepción
            false
        }
    }

    suspend fun removeEmisoraFavorita(uid: String, emisoraId: String): Boolean {
        return try {
            val usuario = getUsuario(uid)
            if (usuario != null) {
                val emisorasFavoritas = usuario.emisorasFavoritas.toMutableList()
                if (emisorasFavoritas.contains(emisoraId)) {
                    emisorasFavoritas.remove(emisoraId)
                    val updatedUsuario = usuario.copy(emisorasFavoritas = emisorasFavoritas)
                    updateUsuario(updatedUsuario)
                } else {
                    true
                }
            } else {
                false
            }
        } catch (e: Exception) {
            // Manejar la excepción
            false
        }
    }
    // Método para subir la foto de perfil
    suspend fun uploadFotoPerfil(fotoUri: Uri, uid: String): String? {
        return try {
            val storageRef = FirebaseStorage.getInstance().reference
            val fotoRef = storageRef.child("fotos_perfil/$uid/${fotoUri.lastPathSegment}")
            val uploadTask = fotoRef.putFile(fotoUri).await()
            fotoRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            // Manejar la excepción
            null
        }
    }
}