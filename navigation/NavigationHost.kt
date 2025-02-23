package com.jcmateus.casanarestereo.navigation


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
import com.jcmateus.casanarestereo.screens.home.Drawer
import com.jcmateus.casanarestereo.screens.home.HomeCasanareVista
import com.jcmateus.casanarestereo.screens.home.HomeViewModel
import com.jcmateus.casanarestereo.screens.home.NavegacionInferior
import com.jcmateus.casanarestereo.screens.home.TopBar
import com.jcmateus.casanarestereo.screens.home.currentRoute
import com.jcmateus.casanarestereo.screens.home.shouldShowBottomBar
import com.jcmateus.casanarestereo.screens.home.shouldShowDrawer
import com.jcmateus.casanarestereo.screens.home.shouldShowTopBar
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.menus.Clasificados
import com.jcmateus.casanarestereo.screens.menus.configuracion.Configuraciones
import com.jcmateus.casanarestereo.screens.menus.Contactenos
import com.jcmateus.casanarestereo.screens.menus.Noticias_Internacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Nacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Regionales
import com.jcmateus.casanarestereo.screens.menus.Podcast
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
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraVista
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.FormularioPerfilEmisora
import com.google.gson.Gson
import com.jcmateus.casanarestereo.screens.home.Destinos.Tema
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
import com.jcmateus.casanarestereo.screens.menus.configuracion.PantallaAcercaDe
import com.jcmateus.casanarestereo.screens.menus.configuracion.PantallaNotificaciones
import com.jcmateus.casanarestereo.screens.menus.configuracion.PantallaPrivacidad
import com.jcmateus.casanarestereo.screens.menus.configuracion.PantallaTema
import com.jcmateus.casanarestereo.screens.menus.emisoras.EmisorasScreen
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias.FormularioNoticia
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias.VistaNoticia
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias.VistaNoticiaScreen
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.FormularioPodcast
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.PodcastRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.PodcastViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.PodcastViewModelFactory
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.VistaPodcast
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion.FormularioPrograma
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioPerfilScreen
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioPerfilViewModel
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    loginViewModel: LoginScreenViewModel,
    formularioViewModel: FormularioViewModel,
    authService: AuthService,
    emisoraViewModel: EmisoraViewModel,
    podcastViewModel: PodcastViewModel,
    usuarioViewModel: UsuarioViewModel,
    dataStoreManager: DataStoreManager,
    usuarioPerfilViewModel: UsuarioPerfilViewModel,
    emisoraRepository: EmisoraRepository
) {
    Log.d("NavController", "NavigationHost: $navController")
    val authState = loginViewModel.authState.collectAsStateWithLifecycle()

    /*
    val startDestination = if (authState.value is EstadoAutenticacion.LoggedIn) {
        Destinos.HomeCasanareVista.ruta
    } else {
        Destinos.SplashScreen.ruta
    }
     */

    // Lista de rutas que NO deben mostrar el Scaffold
    val excludedRoutes = listOf(
        Destinos.PantallaPresentacion.ruta,
        Destinos.CasanareLoginScreen.ruta,
        Destinos.SplashScreen.ruta,
        PantallaFormulario.SeleccionRol.ruta,
        PantallaFormulario.Estudiantes.ruta,
        PantallaFormulario.Estudiantes1.ruta,
        PantallaFormulario.Estudiantes2.ruta,
        PantallaFormulario.Estudiantes3.ruta,
        PantallaFormulario.Docentes.ruta,
        PantallaFormulario.PantallaFinal.ruta,
        Destinos.Pantalla16.ruta
    )

    // Función para determinar si se debe mostrar Scaffold
    fun shouldShowScaffold(route: String): Boolean {
        return !excludedRoutes.contains(route)
    }

    // Función auxiliar para mostrar contenido dentro de un Scaffold
    @Composable
    fun ScaffoldContent(content: @Composable (PaddingValues) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val viewModel: HomeViewModel = viewModel()
        //val navController = rememberNavController()
        val allDestinations = listOf(
            Destinos.Pantalla1,
            Destinos.Pantalla2,
            Destinos.Pantalla3,
            Destinos.Pantalla4,
            Destinos.Pantalla5,
            Destinos.Pantalla6,
            Destinos.Pantalla7,
            Destinos.Pantalla8,
            Destinos.Pantalla9,
            Destinos.Pantalla10,
            Destinos.Pantalla11,
            Destinos.Pantalla12,
            Destinos.Pantalla13,
        )
        val bottomNavDestinations = listOf(
            Destinos.Pantalla1, // Inicio
            Destinos.Pantalla2, // Emisoras
            Destinos.Pantalla8, // Podcast
            //Destinos.Pantalla14, // Preferencias
        )
        val currentRoute = currentRoute(navController) ?: ""
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                if (shouldShowBottomBar(currentRoute)) {
                    NavegacionInferior(
                        navController,
                        bottomNavDestinations,
                        expanded,
                        { expanded = it },
                        innerPadding = innerPadding,
                        dataStoreManager,
                        authService
                    )
                }
            },
            topBar = {
                if (shouldShowTopBar(currentRoute)) {
                    TopBar(scope, scaffoldState, navController, allDestinations, viewModel)
                }
            },
            drawerContent = {
                if (shouldShowDrawer(currentRoute)) {
                    Drawer(
                        scope,
                        scaffoldState,
                        navController,
                        items = allDestinations
                    )
                }
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destinos.PantallaPresentacion.ruta,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ) {
        // Inicio
        composable(Destinos.SplashScreen.ruta) {
            SplashScreen(
                navController = navController,
                authService = authService,
                dataStoreManager = dataStoreManager,
                emisoraRepository = emisoraRepository // Corregido: Usa la instancia recibida
            )
        }
        composable(Destinos.PantallaPresentacion.ruta) {
            PantallaPresentacion(navController = navController, loginViewModel = loginViewModel)
        }
        composable(Destinos.CasanareLoginScreen.ruta) {
            val emisoraViewModel: EmisoraViewModel = viewModel( // Obtener emisoraViewModel aquí
                factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
            )
            CasanareLoginScreen(
                navController = navController,
                emisoraViewModel = emisoraViewModel,
                loginViewModel = loginViewModel
            ) // Pasar loginViewModel
        }
        // Rutas del formulario
        composable(PantallaFormulario.SeleccionRol.ruta) {
            SeleccionRolScreen(navController)
        }
        composable(PantallaFormulario.Estudiantes.ruta) {
            Estudiantes(formularioViewModel, navController)
        }
        composable(PantallaFormulario.Estudiantes1.ruta) {
            Estudiantes1(formularioViewModel, navController)
        }
        composable(PantallaFormulario.Estudiantes2.ruta) {
            Estudiantes2(formularioViewModel, navController)
        }
        composable(PantallaFormulario.Estudiantes3.ruta) {
            Estudiantes3(formularioViewModel, navController)
        }
        composable(PantallaFormulario.Docentes.ruta) {
            Docentes(formularioViewModel, navController)
        }
        composable(PantallaFormulario.PantallaFinal.ruta) {
            PantallaFinalScreen(formularioViewModel, navController)
        }

        composable(Destinos.HomeCasanareVista.ruta) {
            // Llama a HomeCasanareVista con shouldShowScaffold
            val shouldShowScaffold = !excludedRoutes.contains(Destinos.HomeCasanareVista.ruta)
            HomeCasanareVista(
                navController,
                shouldShowScaffold,
                emisoraViewModel,
                usuarioPerfilViewModel,
                authService
            )
        }


        composable(Destinos.Pantalla1.ruta) {
            ScaffoldContent { innerPadding -> Inicio(innerPadding) }
        }
        composable(Destinos.Pantalla2.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> EmisorasScreen(innerPadding, navController) }
        }
        composable(Destinos.Pantalla3.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Noticias_Regionales(innerPadding, navController) }
        }
        composable(Destinos.Pantalla4.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Noticias_Nacionales(innerPadding) }
        }
        composable(Destinos.Pantalla5.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Noticias_Internacionales(innerPadding) }
        }
        composable(Destinos.Pantalla6.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Programacion(innerPadding) }
        }
        composable(Destinos.Pantalla7.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Programas(innerPadding) }
        }
        composable(Destinos.Pantalla8.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Podcast(innerPadding) }
        }
        composable(Destinos.Pantalla9.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Contactenos(innerPadding) }
        }
        composable(Destinos.Pantalla10.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Clasificados(innerPadding) }
        }
        composable(Destinos.Pantalla11.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Youtube_Casanare(innerPadding) }
        }
        //Configuraciones
        composable(Destinos.Pantalla12.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Configuraciones(innerPadding, navController) }
        }
        composable(Destinos.AcercaDe.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> PantallaAcercaDe(innerPadding, navController) }
        }
        composable(Destinos.Notificaciones.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> PantallaNotificaciones(innerPadding, navController) }
        }
        composable(Destinos.Privacidad.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> PantallaPrivacidad(innerPadding, navController) }
        }
        composable(Destinos.Tema.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> PantallaTema(innerPadding, navController) }
        }


        composable(Destinos.Pantalla13.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding ->
                CerrarSesionButton(
                    navController,
                    authService,
                    innerPadding
                )
            }
        }
        /*composable(Destinos.Pantalla14.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Preferencias(
                innerPadding,
                navController
            ) }
        }

         */

        // Educativo
        composable(Destinos.Pantalla15.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Se_Le_Tiene(innerPadding) }
        }
        composable(Destinos.Pantalla16.ruta) { backStackEntry ->
            VideosYoutubeView(navController, innerPadding)
        }
        composable(Destinos.Pantalla17.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding -> Mi_Zona(innerPadding) }
        }

        //Emisora
        composable(Destinos.EmisoraVista.ruta) { backStackEntry ->
            val emisoraViewModel: EmisoraViewModel = viewModel( // Obtener emisoraViewModel aquí
                factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
            )
            ScaffoldContent { innerPadding ->
                EmisoraVista(navController = navController, emisoraViewModel)
            }
        }
        composable(Destinos.FormularioPerfilEmisora.ruta) { backStackEntry ->
            ScaffoldContent { innerPadding ->
                FormularioPerfilEmisora(
                    navController = navController,
                    authService = authService
                )
            }
        }

        // Noticias
        composable(
            route = Destinos.VistaNoticia().ruta + "?noticiaJson={noticiaJson}",
            arguments = listOf(navArgument("noticiaJson") {
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val noticiaJson = backStackEntry.arguments?.getString("noticiaJson")
            ScaffoldContent { innerPadding ->
                VistaNoticia(
                    noticiaJson = noticiaJson,
                    innerPadding = innerPadding,
                    navController = navController
                )
            }
        }

        // Podcast
        composable(
            route = Destinos.VistaPodcast().ruta + "?podcastJson={podcastJson}",
            arguments = listOf(navArgument("podcastJson") {
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val podcastJson = backStackEntry.arguments?.getString("podcastJson")
            val podcast = Gson().fromJson(podcastJson, Contenido.Podcast::class.java)
            val podcastViewModel: PodcastViewModel = viewModel(
                factory = PodcastViewModelFactory(
                    PodcastRepository(FirebaseFirestore.getInstance()),
                    FirebaseAuth.getInstance(),
                    FirebaseFirestore.getInstance()
                )
            )
            ScaffoldContent { innerPadding ->
                VistaPodcast(podcast, innerPadding, podcastViewModel)
            }
        }
        composable(Destinos.FormularioPodcast.ruta) { backStackEntry ->
            val podcastViewModel: PodcastViewModel = viewModel(
                factory = PodcastViewModelFactory(
                    PodcastRepository(FirebaseFirestore.getInstance()),
                    FirebaseAuth.getInstance(),
                    FirebaseFirestore.getInstance()
                )
            )
            LaunchedEffect(podcastViewModel.navegarAInformacion.collectAsState().value) {
                if (podcastViewModel.navegarAInformacion.value) {
                    val podcastJson = Gson().toJson(podcastViewModel.podcast.value)
                    navController.navigate(Destinos.VistaPodcast(podcastJson).ruta + "?podcastJson=$podcastJson")
                    podcastViewModel.resetNavegarAInformacion()
                }
            }
            ScaffoldContent { innerPadding ->
                FormularioPodcast(
                    innerPadding,
                    podcastViewModel,
                    navController
                )
            }
        }


        // Formularios
        composable(Destinos.FormularioNoticia.ruta) { // Ruta sin noticiaId
            ScaffoldContent { innerPadding ->
                FormularioNoticia(
                    innerPadding,
                    navController,
                    noticiaId = null // noticiaId es null cuando no se proporciona
                )
            }
        }
        composable(
            route = "${Destinos.FormularioNoticia.ruta}/{noticiaId}",
            arguments = listOf(navArgument("noticiaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noticiaId = backStackEntry.arguments?.getString("noticiaId")
            ScaffoldContent { innerPadding ->
                FormularioNoticia(
                    innerPadding,
                    navController,
                    noticiaId = noticiaId
                )
            }
        }
        //composable(Destinos.FormularioPodcast.ruta) { ScaffoldContent { innerPadding -> FormularioPodcast(innerPadding, navController) } }
        composable(Destinos.FormularioPrograma.ruta) {
            ScaffoldContent { innerPadding ->
                FormularioPrograma(
                    innerPadding,
                    navController,

                    )
            }
        }
        //composable(Destinos.FormularioBanner.ruta) { ScaffoldContent { innerPadding -> FormularioBanner(innerPadding, navController) } }

        composable(
            route = Destinos.UsuarioPerfilScreen.ruta,
        ) {
            val uid = authService.getCurrentUser()?.uid ?: "" // Obtener el UID del usuario actual
            ScaffoldContent { innerPadding ->
                UsuarioPerfilScreen(usuarioPerfilViewModel, navController, uid)
            }
        }


    }

}


