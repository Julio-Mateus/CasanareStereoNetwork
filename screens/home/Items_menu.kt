package com.jcmateus.casanarestereo.screens.home

import com.jcmateus.casanarestereo.R

sealed class Items_menu(
    val icon1: Int,
    val title1: String,
    val ruta1: String
) {
    object Inicio1: Items_menu(R.drawable.inicio_50,
        "Inicio", "pantalla1")
    object Emisoras2: Items_menu(R.drawable.emisoras_50,
        "Emisoras", "pantalla2")
    object Pantalla8: Items_menu(R.drawable.podcast_50,
        "Podcast", "pantalla8")
    object Pantalla14: Items_menu(R.drawable.preferencias_50,
        "Preferencias", "pantalla14")

}