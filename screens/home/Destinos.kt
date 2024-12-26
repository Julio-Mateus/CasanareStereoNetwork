package com.jcmateus.casanarestereo.screens.home

import com.jcmateus.casanarestereo.R

sealed class Destinos(
    val icon: Int?,
    val title: String,
    val ruta: String
) {
    data object Pantalla1 : Destinos(R.drawable.casa1_100, "Inicio", "pantalla1")
    data object Pantalla2 : Destinos(R.drawable.emisora_100, "Emisoras", "pantalla2")
    data object Pantalla3 : Destinos(R.drawable.noticias_regionales_100, "Noticias Regionales", "pantalla3")
    data object Pantalla4 : Destinos(R.drawable.noticias_nacionales_100, "Noticias Nacionales", "pantalla4")
    data object Pantalla5 : Destinos(R.drawable.noticias_internacionales_100, "Noticias Internacionales", "pantalla5")
    data object Pantalla6 : Destinos(R.drawable.programacion_100, "Programación", "pantalla6")
    data object Pantalla7 : Destinos(R.drawable.programas_100, "Programas", "pantalla7")
    data object Pantalla8 : Destinos(R.drawable.podcast_100, "Podcast", "pantalla8")
    data object Pantalla9 : Destinos(R.drawable.contactenos_100, "Contactenos", "pantalla9")
    data object Pantalla10 : Destinos(R.drawable.clasificados_100, "Clasificados", "pantalla10")
    data object Pantalla11 : Destinos(R.drawable.youtube_casanare_100, "Casanare Stereo", "pantalla11")
    data object Pantalla12 : Destinos(R.drawable.ajustes1_100, "Configuración", "pantalla12")
    data object Pantalla13 : Destinos(R.drawable.salida1_100, "Cerrar Sesión", "pantalla13")
    //data object Pantalla14 : Destinos(R.drawable.preferencias_50, "Preferencias", "pantalla14")
    //Acciones
    data object Pantalla15: Destinos( null,"pantalla15", "pantalla15")
    data object Pantalla16: Destinos(null, "pantalla16", "pantalla16")
    data object Pantalla17: Destinos(null, "Pantalla 17", "pantalla17")
    //Inicio
    data object PantallaPresentacion : Destinos( icon = null, title = "Pantalla Presentacion", ruta = "PantallaPresentacion")
    data object HomeCasanareVista : Destinos( icon = null, title = "HomeCasanareVista", ruta = "HomeCasanareVista")
    data object CasanareLoginScreen : Destinos( icon = null, title = "Login", ruta = "CasanareLoginScreen")

    //Emisora
    data object EmisoraVista : Destinos( icon = null, title = "Emisora", ruta = "EmisoraVista")
    data object FormularioPerfilEmisora : Destinos( icon = null, title = "FormularioEmisora", ruta = "FormularioPerfilEmisora")

    //Noticias
    data object VistaNoticia : Destinos( icon = null, title = "Noticias", ruta = "VistaNoticias")
    data object FormularioNoticia : Destinos( icon = null, title = "FormularioNoticia", ruta = "FormularioNoticia")

    //Podcast
    data object VistaPodcast : Destinos( icon = null, title = "Podcast", ruta = "PodcastVista")
    data object FormularioPodcast : Destinos( icon = null, title = "FormularioPodcast", ruta = "FormularioPodcast")

    //Programacion
    data object VistaPrograma : Destinos( icon = null, title = "Programacion", ruta = "VistaPrograma")
    data object FormularioPrograma : Destinos( icon = null, title = "FormularioPrograma", ruta = "FormularioPrograma")

    //Banner
    data object VistaBanner : Destinos( icon = null, title = "Banner", ruta = "BannerVista")
    data object FormularioBanner : Destinos( icon = null, title = "FormularioBanner", ruta = "FormularioBanner")




    object SplashScreen : Destinos(null, "SplashScreen", "SplashScreen")


}
