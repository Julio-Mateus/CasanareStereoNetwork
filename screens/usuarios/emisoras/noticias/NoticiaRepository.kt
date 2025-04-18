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

    suspend fun guardarNoticia(noticia: Contenido.Noticia, emisoraId: String): String {
        val id = UUID.randomUUID().toString()
        val noticiaWithId = noticia.copy(id = id)
        val docRef = db.collection("emisoras").document(emisoraId).collection("noticias").document(id)
        docRef.set(noticiaWithId).await()
        return id
    }

    suspend fun obtenerNoticias(emisoraId: String): List<Contenido.Noticia> {
        return db.collection("emisoras").document(emisoraId).collection("noticias")
            .get().await().toObjects(Contenido.Noticia::class.java)
    }

    suspend fun obtenerNoticiasPorCategoria(emisoraId: String, categoria: String): List<Contenido.Noticia> {
        return db.collection("emisoras").document(emisoraId).collection("noticias")
            .whereEqualTo("categoria", categoria)
            .get().await().toObjects(Contenido.Noticia::class.java)
    }

    suspend fun obtenerNoticia(noticiaId: String, emisoraId: String): Contenido.Noticia? {
        val docRef = db.collection("emisoras").document(emisoraId).collection("noticias").document(noticiaId)
        val documentSnapshot = docRef.get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(Contenido.Noticia::class.java)
        } else {
            null
        }
    }

    suspend fun eliminarNoticia(noticiaId: String, emisoraId: String) {
        val docRef = db.collection("emisoras").document(emisoraId).collection("noticias").document(noticiaId)
        docRef.delete().await()
    }

    suspend fun actualizarNoticia(noticiaId: String, noticia: Contenido.Noticia, emisoraId: String) {
        val docRef = db.collection("emisoras").document(emisoraId).collection("noticias").document(noticiaId)
        docRef.set(noticia).await()
    }
}