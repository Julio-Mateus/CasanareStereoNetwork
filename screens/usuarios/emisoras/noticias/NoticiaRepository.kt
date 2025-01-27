package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.text.get
import kotlin.text.set
import kotlin.toString

class NoticiaRepository(private val db: FirebaseFirestore) {

    suspend fun guardarNoticia(noticia: Contenido.Noticia, userId: String) {
        val id = UUID.randomUUID().toString() // Generate a unique ID
        val noticiaWithId = noticia.copy(id = id) // Assign the ID to the noticia object
        db.collection("emisoras").document(userId).collection("noticias").document(id).set(noticiaWithId).await()
    }

    suspend fun obtenerNoticias(userId: String): List<Contenido.Noticia> {
        return db.collection("emisoras").document(userId).collection("noticias")
            .get().await().toObjects(Contenido.Noticia::class.java)
    }

    suspend fun obtenerNoticiasPorCategoria(userId: String, categoria: String): List<Contenido.Noticia> {
        return db.collection("emisoras").document(userId).collection("noticias")
            .whereEqualTo("categoria", categoria)
            .get().await().toObjects(Contenido.Noticia::class.java)
    }

    suspend fun obtenerNoticia(noticiaId: String): Contenido.Noticia? {
        return db.collectionGroup("noticias").whereEqualTo("id", noticiaId).get().await()
            .toObjects(Contenido.Noticia::class.java).firstOrNull()
    }

    suspend fun eliminarNoticia(noticiaId: String, userId: String) {
        // Aquí debes usar el userId para eliminar la noticia de la base de datos
        // Ejemplo con Firebase:
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("usuarios").document(userId).collection("noticias").document(noticiaId)
        docRef.delete().await()
    }

    suspend fun actualizarNoticia(noticiaId: String, noticia: Contenido.Noticia) {
        db.collectionGroup("noticias").whereEqualTo("id", noticiaId).get().await()
            .documents.firstOrNull()?.reference?.set(noticia)?.await()
    }
}