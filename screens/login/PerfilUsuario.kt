package com.jcmateus.casanarestereo.screens.login

data class PerfilUsuario(
    val id: String = "", // userId
    val rol: String = "", // "EMISORA" o "USUARIO"
    val nombre: String = "",
    val email: String = "",
    val isFirstTime: Boolean = true,
    val uid: String = "",
    val emisoraId: String? = null, // Solo si el rol es "EMISORA"
    val avatarUrl: String? = null,
    val frase: String? = null,
    val profesion: String? = null,
    val emisorasFavoritas: List<String> = emptyList(),
    val ciudad: String? = null,
    val departamento: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
    // ... otros campos de usuario ...
)

// Función de extensión para convertir PerfilUsuario a un mapa
fun PerfilUsuario.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "rol" to rol,
        "nombre" to nombre,
        "email" to email,
        "isFirstTime" to isFirstTime,
        "uid" to uid,
        "emisoraId" to emisoraId,
        "avatarUrl" to avatarUrl,
        "frase" to frase,
        "profesion" to profesion,
        "emisorasFavoritas" to emisorasFavoritas,
        "ciudad" to ciudad,
        "departamento" to departamento,
        "latitud" to latitud,
        "longitud" to longitud
    )
}