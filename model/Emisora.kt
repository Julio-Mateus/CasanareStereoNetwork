package com.jcmateus.casanarestereo.model

data class Emisora(
    val id: String? = null,// ID de la emisora
    val nombre: String = "", // Nombre de la emisora
    val descripcion: String = "", // Descripción de la emisora
    val url: String = "", // URL de la emisora
    val logoUrl: String = "", // URL del logotipo de la emisora
    val ubicacion: Ubicacion = Ubicacion(), // Ubicación de la emisora
    val noticiasNacionales: List<Noticia> = emptyList(), // Noticias nacionales
    val noticiasLocales: List<Noticia> = emptyList(), // Noticias locales
    val noticiasInternacionales: List<Noticia> = emptyList() // Noticias internacionales
)
data class Ubicacion(
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

data class Noticia(
    val titulo: String = "",
    val contenido: String = "",
    val fecha: String = ""
)