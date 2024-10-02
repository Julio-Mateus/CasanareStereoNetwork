package com.jcmateus.casanarestereo.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.PantallaPresentacion
import com.jcmateus.casanarestereo.SplashScreen
import com.jcmateus.casanarestereo.screens.formulario.Docentes
import com.jcmateus.casanarestereo.screens.formulario.Estudiantes
import com.jcmateus.casanarestereo.screens.formulario.Estudiantes1
import com.jcmateus.casanarestereo.screens.formulario.Estudiantes2
import com.jcmateus.casanarestereo.screens.formulario.Estudiantes3
import com.jcmateus.casanarestereo.screens.formulario.FormularioViewModel
import com.jcmateus.casanarestereo.screens.formulario.PantallaFinalScreen
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.formulario.SeleccionRolScreen
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.HomeCasanareVista
import com.jcmateus.casanarestereo.screens.menus.Clasificados
import com.jcmateus.casanarestereo.screens.menus.Configuraciones
import com.jcmateus.casanarestereo.screens.menus.Contactenos
import com.jcmateus.casanarestereo.screens.menus.Emisoras
import com.jcmateus.casanarestereo.screens.menus.Noticias_Internacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Nacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Regionales
import com.jcmateus.casanarestereo.screens.menus.Podcast
import com.jcmateus.casanarestereo.screens.menus.Preferencias
import com.jcmateus.casanarestereo.screens.menus.Programacion
import com.jcmateus.casanarestereo.screens.menus.Programas
import com.jcmateus.casanarestereo.screens.menus.Youtube_Casanare
import com.jcmateus.casanarestereo.screens.login.CasanareLoginScreen
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.menus.CerrarSesionButton
import com.jcmateus.casanarestereo.screens.menus.Inicio
//import com.jcmateus.casanarestereo.InicioCasanareVista
//import com.jcmateus.casanarestereo.screens.menus.Educativo
import com.jcmateus.casanarestereo.screens.menus.Mi_Zona
import com.jcmateus.casanarestereo.screens.menus.Se_Le_Tiene
import com.jcmateus.casanarestereo.screens.menus.VideosYoutubeView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    loginViewModel: LoginScreenViewModel,
) {
    Log.d("NavController", "NavigationHost: $navController")
    val viewModel: FormularioViewModel = viewModel()
    val context = LocalContext.current.applicationContext
    val application = LocalContext.current.applicationContext as HomeApplication
    val showScaffold = application.showScaffold
    NavHost(
        navController = navController,
        startDestination = Destinos.PantallaPresentacion.ruta,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ) {
        // Inicio
        composable(Destinos.SplashScreen.ruta){
            SplashScreen(navController = navController)
        }
        composable(Destinos.PantallaPresentacion.ruta){
            PantallaPresentacion(navController = navController)
        }
        composable(Destinos.CasanareLoginScreen.ruta){
            CasanareLoginScreen(navController = navController)

        }
        composable(Destinos.HomeCasanareVista.ruta){
            HomeCasanareVista(navController = navController, loginViewModel = loginViewModel, showScaffold = showScaffold)
        }
        // Rutas del formulario
        composable(PantallaFormulario.SeleccionRol.ruta) {
            SeleccionRolScreen(navController)
        }
        composable(PantallaFormulario.Estudiantes.ruta) {
            Estudiantes(viewModel, navController)
        }
        composable(PantallaFormulario.Estudiantes1.ruta) {
            Estudiantes1(viewModel, navController)
        }
        composable(PantallaFormulario.Estudiantes2.ruta) {
            Estudiantes2(viewModel, navController)
        }
        composable(PantallaFormulario.Estudiantes3.ruta) {
            Estudiantes3(viewModel, navController)
        }
        composable(PantallaFormulario.Docentes.ruta) {
            Docentes(viewModel, navController)
        }
        composable(PantallaFormulario.PantallaFinal.ruta) {
            PantallaFinalScreen(viewModel, navController)
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
        composable(Destinos.Pantalla13.ruta) {
            CerrarSesionButton( navController)
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


