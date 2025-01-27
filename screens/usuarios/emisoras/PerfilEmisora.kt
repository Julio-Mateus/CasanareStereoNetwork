package com.jcmateus.casanarestereo.screens.usuarios.emisoras

data class PerfilEmisora(
    val id: String = "",
    val nombre: String = "", // Cambiar el orden de los campos
    val descripcion: String = "",
    val imagenPerfilUri: String = "",
    val enlace: String = "",
    val paginaWeb: String = "",
    val ciudad: String = "",
    val departamento: String = "",
    val frecuencia: String = "",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0
)