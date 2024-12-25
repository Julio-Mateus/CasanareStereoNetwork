package com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido

import android.util.Log
import androidx.compose.foundation.layout.add
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ContenidoViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun guardarContenido(contenido: Contenido) {
        when (contenido) {
            is Contenido.Noticia -> {
                val noticiaData = hashMapOf(
                    "tituloNoticia" to contenido.tituloNoticia,
                    "imagenUriNoticia" to contenido.imagenUriNoticia,
                    "fuenteNoticia" to contenido.fuenteNoticia,
                    "fechaPublicacionNoticia" to contenido.fechaPublicacionNoticia,
                    "autorNoticia" to contenido.autorNoticia,
                    "enlaceNoticia" to contenido.enlaceNoticia,
                    "contenidoNoticia" to contenido.contenidoNoticia,
                    "ubicacionNoticia" to contenido.ubicacionNoticia,
                    "etiquetaNoticia" to contenido.etiquetaNoticia
                )

                db.collection("noticias")
                    .add(noticiaData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Noticia guardada con ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error al guardar la noticia", e)
                    }
            }
            is Contenido.Podcast -> {
                val podcastData = hashMapOf(
                    "tituloPodcast" to contenido.tituloPodcast,
                    "descripcionPodcast" to contenido.descripcionPodcast,
                    "audioUriPodcast" to contenido.audioUriPodcast,
                    "fechaPodcast" to contenido.fechaPodcast,
                    "autorPodcast" to contenido.autorPodcast,
                    "enlacePodcast" to contenido.enlacePodcast,
                    "imagenUriPodcast" to contenido.imagenUriPodcast,
                    "etiquetaPodcast" to contenido.etiquetaPodcast,
                    "duracionPodcast" to contenido.duracionPodcast,
                    "numeroEpisodioPodcast" to contenido.numeroEpisodioPodcast,
                    "numeroTemporadaPodcast" to contenido.numeroTemporadaPodcast
                )

                db.collection("podcasts")
                    .add(podcastData)
                    .addOnSuccessListener{ documentReference ->
                        Log.d(TAG, "Podcast guardado con ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error al guardar el podcast", e)
                    }
            }
            is Contenido.Programa -> {
                // ... (Lógica para guardar Programa en Firestore) ...
            }
            is Contenido.Banner -> {
                // ... (Lógica para guardar Banner en Firestore) ...
            }
        }
    }

    companion object {
        private const val TAG = "ContenidoViewModel"
    }
}