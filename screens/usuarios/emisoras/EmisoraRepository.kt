package com.jcmateus.casanarestereo.screens.usuarios.emisoras

import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EmisoraRepository(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) {
    suspend fun subirPodcast(podcast: Contenido.Podcast) {
        val id = UUID.randomUUID().toString()
        val podcastWithId = podcast.copy(id = id)
        db.collection("podcast").document(id).set(podcastWithId).await()
    }
    suspend fun subirNoticia(noticia: Contenido.Noticia) {
        val id = UUID.randomUUID().toString()
        val noticiaWithId = noticia.copy(id = id)
        db.collection("noticias").document(id).set(noticiaWithId).await()
    }
    suspend fun subirPrograma(programa: Contenido.Programa) {
        val id = UUID.randomUUID().toString()
        val programaWithId = programa.copy(id = id)
        db.collection("programas").document(id).set(programaWithId).await()
    }
    suspend fun subirBanner(banner: Contenido.Banner) {
        val id = UUID.randomUUID().toString()
        val bannerWithId = banner.copy(id = id)
        db.collection("banners").document(id).set(bannerWithId).await()
    }
}