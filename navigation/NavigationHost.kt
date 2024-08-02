package com.jcmateus.casanarestereo.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.PantallaPresentacion
import com.jcmateus.casanarestereo.screens.formulario.FormularioScreen
import com.jcmateus.casanarestereo.screens.formulario.FormularioViewModel
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.Home
import com.jcmateus.casanarestereo.screens.menus.Clasificados
import com.jcmateus.casanarestereo.screens.menus.Configuraciones
import com.jcmateus.casanarestereo.screens.menus.Contactenos
import com.jcmateus.casanarestereo.screens.menus.Emisoras
import com.jcmateus.casanarestereo.screens.menus.Inicio
import com.jcmateus.casanarestereo.screens.menus.Noticias_Internacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Nacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Regionales
import com.jcmateus.casanarestereo.screens.menus.Podcast
import com.jcmateus.casanarestereo.screens.menus.Preferencias
import com.jcmateus.casanarestereo.screens.menus.Programacion
import com.jcmateus.casanarestereo.screens.menus.Programas
import com.jcmateus.casanarestereo.screens.menus.Youtube_Casanare
import com.jcmateus.casanarestereo.screens.login.CasanareLoginScreen
import com.jcmateus.casanarestereo.screens.login.InicioCasanareVista
//import com.jcmateus.casanarestereo.screens.menus.Educativo
import com.jcmateus.casanarestereo.screens.menus.Mi_Zona
import com.jcmateus.casanarestereo.screens.menus.Se_Le_Tiene
import com.jcmateus.casanarestereo.screens.menus.VideosYoutubeView

@Composable
fun NavigationHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinos.PantallaPresentacion.ruta,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ) {
        // Inicio
        composable(Destinos.PantallaPresentacion.ruta){
            PantallaPresentacion(navController = navController)
        }
        composable(Destinos.InicioCasanareVista.ruta){
            InicioCasanareVista(navController = navController)
        }
        composable(Destinos.HomeCasanareVista.ruta){
            Home(navController = navController)
        }
        composable(Destinos.FormularioScreen.ruta){
            val viewModel: FormularioViewModel = viewModel()
            FormularioScreen(navController = navController, viewModel = viewModel)
        }
        composable(Destinos.CasanareLoginScreen.ruta){
            CasanareLoginScreen(navController = navController)

        }

        composable(Destinos.Pantalla1.ruta) {
            Inicio()
        }
        composable(Destinos.Pantalla2.ruta) {
            Emisoras()
        }
        composable(Destinos.Pantalla3.ruta) {
            Noticias_Regionales()
        }
        composable(Destinos.Pantalla4.ruta) {
            Noticias_Nacionales()
        }
        composable(Destinos.Pantalla5.ruta) {
            Noticias_Internacionales()
        }
        composable(Destinos.Pantalla6.ruta) {
            Programacion()
        }
        composable(Destinos.Pantalla7.ruta) {
            Programas()
        }
        composable(Destinos.Pantalla8.ruta) {
            Podcast()
        }
        composable(Destinos.Pantalla9.ruta) {
            Contactenos()
        }
        composable(Destinos.Pantalla10.ruta) {
            Clasificados()
        }
        composable(Destinos.Pantalla11.ruta) {
            Youtube_Casanare()
        }
        composable(Destinos.Pantalla12.ruta) {
            Configuraciones()
        }
        composable(Destinos.Pantalla14.ruta) {
            Preferencias()
        }
        // Educativo
        composable(Destinos.Pantalla15.ruta){
            Se_Le_Tiene()
        }
        composable(Destinos.Pantalla16.ruta){
            VideosYoutubeView( navController)
        }
        composable(Destinos.Pantalla17.ruta){
            Mi_Zona()
        }
    }
}


