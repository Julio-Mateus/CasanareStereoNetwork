package com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

sealed class Contenido : Parcelable {
    @Parcelize
    data class Noticia(
        val tituloNoticia: String = "",
        val imagenUriNoticia: String = "",
        val fuenteNoticia: String = "",
        val fechaPublicacionNoticia: String = "",
        val autorNoticia: String = "",
        val enlaceNoticia: String = "",
        val contenidoNoticia: String = "",
        val ubicacionNoticia: String = "",
        val etiquetaNoticia: String = "",
        val id: String = "",
        val categoria: String = ""
    ) : Contenido(), Parcelable

    @Parcelize
    data class Podcast(
        val tituloPodcast: String = "",
        val descripcionPodcast: String = "",
        val audioUriPodcast: String = "",
        val fechaPodcast: String = "",
        val autorPodcast: String = "",
        val enlacePodcast: String = "",
        val imagenUriPodcast: String = "",
        val etiquetaPodcast: String = "",
        val duracionPodcast: String = "",
        val numeroEpisodioPodcast: String = "",
        val numeroTemporadaPodcast: String = "",
        val id: String = ""
    ) : Contenido(), Parcelable

    @Parcelize
    data class Programa(
        val nombrePrograma: String = "",
        val descripcionPrograma: String = "",
        val horarioPrograma: String = "",
        val imagenUriPrograma: String = "",
        val enlacePrograma: String = "",
        val fechaPrograma: String = "",
        val autorPrograma: String = "",
        val etiquetaPrograma: String = "",
        val duracionPrograma: String = "",
        val id: String = ""
    ) : Contenido(), Parcelable

    @Parcelize
    data class Banner(
        val imagenUriPublicidad: String = "",
        val enlacePublicidad: String = "",
        val fechaPublicidad: String = "",
        val autorPublicidad: String = "",
        val etiquetaPublicidad: String = "",
        val descripcionPublicidad: String = "",
        val tituloPublicidad: String = "",
        val duracionPublicidad: String = "",
        val tipoPublicidad: String = "",
        val id: String = ""
    ) : Contenido(), Parcelable
}