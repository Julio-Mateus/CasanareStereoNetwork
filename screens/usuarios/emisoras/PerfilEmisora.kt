package com.jcmateus.casanarestereo.screens.usuarios.emisoras

data class PerfilEmisora(
    val id: String = "",
    val rol: String = "",
    val nombre: String = "",
    val email: String = "",
    val descripcion: String = "",
    val imagenPerfilUri: String = "",
    val enlace: String = "",
    val paginaWeb: String = "",
    val ciudad: String = "",
    val departamento: String = "",
    val frecuencia: String = "",
    var latitud: Double? = null,
    var longitud: Double? = null
)