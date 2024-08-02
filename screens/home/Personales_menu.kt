package com.jcmateus.casanarestereo.screens.home

sealed class Personales_menu(
   val route: String,
) {

        object Pantalla15: Personales_menu("Se le tiene")
        object VideosYoutube: Personales_menu("Educativo")
        object Pantalla17: Personales_menu("Mi zona")
}