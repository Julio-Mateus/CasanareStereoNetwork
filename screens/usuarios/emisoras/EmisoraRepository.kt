package com.jcmateus.casanarestereo.screens.usuarios.emisoras

import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EmisoraRepository(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) {
    suspend fun guardarPerfilEmisora(perfil: PerfilEmisora, userId: String) {
        val emisoraData = hashMapOf(
            "imagenPerfilUri" to perfil.imagenPerfilUri,
            "nombre" to perfil.nombre,
            "descripcion" to perfil.descripcion,
            "enlace" to perfil.enlace,
            "paginaWeb" to perfil.paginaWeb,
            "ciudad" to perfil.ciudad,
            "departamento" to perfil.departamento,
            "frecuencia" to perfil.frecuencia
        )
        db.collection("emisoras").document(userId).set(emisoraData).await()
    }

    suspend fun actualizarImagenPerfil(imagenUri: Uri, userId: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$userId")
        val uploadTask = storageRef.putFile(imagenUri).await()
        val downloadUri = storageRef.child("images/$userId").downloadUrl.await()
        val perfilEmisora = cargarPerfilEmisora(userId)
        if (perfilEmisora != null) {
            val emisoraData = hashMapOf(
                "imagenPerfilUri" to downloadUri.toString(),
                "nombre" to perfilEmisora.nombre,
                "descripcion" to perfilEmisora.descripcion,
                "enlace" to perfilEmisora.enlace,
                "paginaWeb" to perfilEmisora.paginaWeb,
                "ciudad" to perfilEmisora.ciudad,
                "departamento" to perfilEmisora.departamento,
                "frecuencia" to perfilEmisora.frecuencia,
                "latitud" to perfilEmisora.latitud, // Agregar latitud
                "longitud" to perfilEmisora.longitud // Agregar longitud
            )
            db.collection("emisoras").document(userId).set(emisoraData).await()
        }
    }

    suspend fun cargarPerfilEmisora(userId: String): PerfilEmisora? {
        val documentSnapshot = db.collection("emisoras").document(userId).get().await()
        return documentSnapshot.toObject(PerfilEmisora::class.java)
    }

    // Nuevo método para obtener las emisoras cercanas
    suspend fun getEmisorasCercanas(location: Location): List<PerfilEmisora> {
        val emisoras = mutableListOf<PerfilEmisora>()
        try {
            val querySnapshot = db.collection("emisoras").get().await()
            for (document in querySnapshot.documents) {
                val emisora = document.toObject(PerfilEmisora::class.java)
                if (emisora != null) {
                    // Calcular la distancia
                    val emisoraLocation = Location("").apply {
                        latitude = emisora.latitud
                        longitude = emisora.longitud
                    }
                    val distance = location.distanceTo(emisoraLocation) / 1000 // Distancia en km
                    val maxDistance = 50 // Radio de búsqueda en km

                    // Filtrar las emisoras cercanas
                    if (distance <= maxDistance) {
                        emisoras.add(emisora)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("EmisoraRepository", "Error al obtener las emisoras cercanas", e)
        }
        return emisoras
    }
}