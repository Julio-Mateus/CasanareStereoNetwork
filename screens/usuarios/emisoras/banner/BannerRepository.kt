package com.jcmateus.casanarestereo.screens.usuarios.emisoras.banner

import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.text.get
import kotlin.text.set
import kotlin.toString

class BannerRepository(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) {

    suspend fun guardarBanner(banner: Contenido.Banner, userId: String) {
        val id = UUID.randomUUID().toString()
        val bannerWithId = banner.copy(id = id)
        db.collection("emisoras").document(userId).collection("banners").document(id).set(bannerWithId).await()
    }

    // Función para obtener todos los banners de una emisora
    suspend fun obtenerBanners(userId: String): List<Contenido.Banner> {
        return db.collection("emisoras").document(userId).collection("banners")
            .get().await().toObjects(Contenido.Banner::class.java)
    }

    // Función para obtener un banner específico por su ID
    suspend fun obtenerBanner(bannerId: String): Contenido.Banner? {
        return db.collectionGroup("banners").whereEqualTo("id", bannerId).get().await()
            .toObjects(Contenido.Banner::class.java).firstOrNull()
    }

    // Función para eliminar un banner por su ID
    suspend fun eliminarBanner(bannerId: String) {
        db.collectionGroup("banners").whereEqualTo("id", bannerId).get().await()
            .documents.forEach { it.reference.delete().await() }
    }

    // Función para actualizar un banner
    suspend fun actualizarBanner(bannerId: String, banner: Contenido.Banner) {
        db.collectionGroup("banners").whereEqualTo("id", bannerId).get().await()
            .documents.firstOrNull()?.reference?.set(banner)?.await()
    }
}