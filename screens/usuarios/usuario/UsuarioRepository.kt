package com.jcmateus.casanarestereo.screens.usuarios.usuario

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.add
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.collections.remove
import kotlin.collections.toMutableList



class UsuarioRepository(private val db: FirebaseFirestore) {

    suspend fun getUsuario(uid: String): Usuario? {
        return try {
            val document = db.collection("usuarios").document(uid).get().await()
            if (document.exists()) {
                document.toObject<Usuario>()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al obtener el usuario", e)
            null
        }
    }

    suspend fun updateUsuario(usuario: Usuario): Boolean {
        return try {
            db.collection("usuarios").document(usuario.uid).set(usuario).await()
            true
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al actualizar el usuario", e)
            false
        }
    }

    suspend fun addEmisoraFavorita(uid: String, emisoraId: String): Boolean {
        return try {
            val usuarioRef = db.collection("usuarios").document(uid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(usuarioRef)
                val usuario = snapshot.toObject<Usuario>()
                if (usuario != null) {
                    val emisorasFavoritas = usuario.emisorasFavoritas.toMutableList()
                    if (!emisorasFavoritas.contains(emisoraId)) {
                        emisorasFavoritas.add(emisoraId)
                        transaction.update(usuarioRef, "emisorasFavoritas", emisorasFavoritas)
                    }
                }
            }.await()
            true
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al añadir emisora favorita", e)
            false
        }
    }

    suspend fun removeEmisoraFavorita(uid: String, emisoraId: String): Boolean {
        return try {
            val usuarioRef = db.collection("usuarios").document(uid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(usuarioRef)
                val usuario = snapshot.toObject<Usuario>()
                if (usuario != null) {
                    val emisorasFavoritas = usuario.emisorasFavoritas.toMutableList()
                    if (emisorasFavoritas.contains(emisoraId)) {
                        emisorasFavoritas.remove(emisoraId)
                        transaction.update(usuarioRef, "emisorasFavoritas", emisorasFavoritas)
                    }
                }
            }.await()
            true
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al eliminar emisora favorita", e)
            false
        }
    }
}