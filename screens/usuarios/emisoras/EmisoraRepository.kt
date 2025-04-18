package com.jcmateus.casanarestereo.screens.usuarios.emisoras

import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.compareTo
import kotlin.div
import kotlin.text.get
import kotlin.text.set
import kotlin.toString

class EmisoraRepository(private val db: FirebaseFirestore, private val storage: FirebaseStorage) {

    suspend fun guardarPerfilEmisora(perfil: PerfilEmisora, userId: String) {
        try {
            db.collection("emisoras").document(userId).set(perfil).await()
            Log.d("EmisoraRepository", "Perfil guardado correctamente")
        } catch (e: Exception) {
            Log.e("EmisoraRepository", "Error al guardar el perfil", e)
            throw e
        }
    }

    suspend fun cargarPerfilEmisora(userId: String): PerfilEmisora? {
        return try {
            val document = db.collection("emisoras").document(userId).get().await()
            if (document.exists()) {
                document.toObject<PerfilEmisora>()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("EmisoraRepository", "Error al cargar el perfil", e)
            throw e
        }
    }

    suspend fun actualizarImagenPerfil(imagenUri: Uri, userId: String) {
        try {
            val storageRef = storage.reference
            val imageRef = storageRef.child("perfiles/$userId/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(imagenUri).await()
            val url = imageRef.downloadUrl.await().toString()
            db.collection("emisoras").document(userId).update("imagenPerfilUri", url).await()
            Log.d("EmisoraRepository", "Imagen de perfil actualizada correctamente")
        } catch (e: Exception) {
            Log.e("EmisoraRepository", "Error al actualizar la imagen de perfil", e)
            throw e
        }
    }
    suspend fun getEmisorasCercanas(location: Location): List<PerfilEmisora> {
        // Aquí debes implementar la lógica para obtener las emisoras cercanas
        // usando la ubicación del usuario (location)
        // Por ahora, vamos a simular que obtenemos una lista de emisoras
        return emptyList()
    }
}