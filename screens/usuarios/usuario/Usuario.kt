package com.jcmateus.casanarestereo.screens.usuarios.usuario

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val avatarUrl: String? = null,
    val frase: String = "",
    val profesion: String = "",
    val ciudad: String = "",
    val departamento: String = "",
    val emisorasFavoritas: List<String> = emptyList()
)