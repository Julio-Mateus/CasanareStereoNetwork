package com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast

import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.text.get
import kotlin.text.set
import kotlin.toString

class PodcastRepository(private val db: FirebaseFirestore) {

    suspend fun guardarPodcast(podcast: Contenido.Podcast, userId: String) {
        val id = UUID.randomUUID().toString() // Generate a unique ID
        val podcastWithId = podcast.copy(id = id) // Assign the ID to the podcast object
        db.collection("emisoras").document(userId).collection("podcasts").document(id).set(podcastWithId).await()
    }

    suspend fun obtenerPodcasts(userId: String): List<Contenido.Podcast> {
        return db.collection("emisoras").document(userId).collection("podcasts")
            .get().await().toObjects(Contenido.Podcast::class.java)
    }

    suspend fun obtenerPodcast(podcastId: String): Contenido.Podcast? {
        return db.collectionGroup("podcasts").whereEqualTo("id", podcastId).get().await()
            .toObjects(Contenido.Podcast::class.java).firstOrNull()
    }

    suspend fun eliminarPodcast(podcastId: String) {
        db.collectionGroup("podcasts").whereEqualTo("id", podcastId).get().await()
            .documents.forEach { it.reference.delete().await() }
    }

    suspend fun actualizarPodcast(podcastId: String, podcast: Contenido.Podcast) {
        db.collectionGroup("podcasts").whereEqualTo("id", podcastId).get().await()
            .documents.firstOrNull()?.reference?.set(podcast)?.await()
    }
}