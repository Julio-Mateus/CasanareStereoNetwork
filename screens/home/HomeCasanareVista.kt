package com.jcmateus.casanarestereo.screens.home

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//import androidx.compose.material.ExperimentalMaterial3Api
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModelFactory
import com.jcmateus.casanarestereo.screens.login.Rol
import com.jcmateus.casanarestereo.screens.menus.CerrarSesionButton
import com.jcmateus.casanarestereo.screens.menus.Clasificados
import com.jcmateus.casanarestereo.screens.menus.Configuraciones
import com.jcmateus.casanarestereo.screens.menus.Contactenos
import com.jcmateus.casanarestereo.screens.menus.Emisoras
import com.jcmateus.casanarestereo.screens.menus.Inicio
import com.jcmateus.casanarestereo.screens.menus.Mi_Zona
import com.jcmateus.casanarestereo.screens.menus.Noticias_Internacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Nacionales
import com.jcmateus.casanarestereo.screens.menus.Noticias_Regionales
import com.jcmateus.casanarestereo.screens.menus.Podcast
import com.jcmateus.casanarestereo.screens.menus.Preferencias
import com.jcmateus.casanarestereo.screens.menus.Programacion
import com.jcmateus.casanarestereo.screens.menus.Programas
import com.jcmateus.casanarestereo.screens.menus.Se_Le_Tiene
import com.jcmateus.casanarestereo.screens.menus.VideosYoutubeView
import com.jcmateus.casanarestereo.screens.menus.Youtube_Casanare
import com.jcmateus.casanarestereo.screens.usuarios.EmisoraVista
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.jcmateus.casanarestereo.screens.usuarios.EmisoraViewModel

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class HomeActivity : ComponentActivity() {
    lateinit var loginViewModel: LoginScreenViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CasanareStereoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val loginViewModel = createLoginViewModel(application as HomeApplication)
                    val showScaffold = (application as HomeApplication).showScaffold
                    HomeCasanareVista(navController, loginViewModel, showScaffold = showScaffold)
                }
            }
        }
    }
}

@Composable
fun createLoginViewModel(application: HomeApplication): LoginScreenViewModel {
    val dataStoreManager = application.dataStoreManager
    val authService = AuthService(application.firebaseAuth) // Crear instancia de AuthService
    return viewModel(
        factory = LoginScreenViewModelFactory(
            dataStoreManager,
            authService, // Pasar authService
            application.firebaseAuth // Pasar firebaseAuth
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeCasanareVista(navController: NavHostController, loginViewModel: LoginScreenViewModel, showScaffold: Boolean) {
    Log.d("NavController", "Home: $navController")
    val dataStoreManager =
        (LocalContext.current.applicationContext as HomeApplication).dataStoreManager
    val viewModel: HomeViewModel = viewModel()

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current.applicationContext
    //val dataStoreManager = DataStoreManager.getInstance(context)
    val scope = rememberCoroutineScope()
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
        Destinos.Pantalla14, // Preferencias
    )
    val currentRoute = currentRoute(navController) ?: ""
    /*val shouldShowScaffold = currentRoute != Destinos.PantallaPresentacion.ruta &&
            currentRoute != Destinos.CasanareLoginScreen.ruta &&
            currentRoute != Destinos.SplashScreen.ruta &&
            currentRoute != PantallaFormulario.SeleccionRol.ruta &&
            currentRoute != PantallaFormulario.Estudiantes.ruta &&
            currentRoute != PantallaFormulario.Estudiantes1.ruta &&
            currentRoute != PantallaFormulario.Estudiantes2.ruta &&
            currentRoute != PantallaFormulario.Estudiantes3.ruta &&
            currentRoute != PantallaFormulario.Docentes.ruta
     */
    val application = LocalContext.current.applicationContext as HomeApplication
    val rolUsuario = dataStoreManager.getRolUsuario().collectAsState(initial = Rol.USUARIO).value
    when (rolUsuario) {
        Rol.USUARIO -> {
            // Composable para la vista del usuario
            //UsuarioVista(navController)
        }
        Rol.EMISORA -> {
            // Composable para la vista de la emisora
            EmisoraVista(
                navController,
                viewModel = TODO(),
            )
        }
    }



    var authState by remember { mutableStateOf<EstadoAutenticacion>(EstadoAutenticacion.Loading) }

    fun updateAuthState(newState: EstadoAutenticacion) {
        authState = newState
    }
    LaunchedEffect(key1 = authState) {
        when (authState) {
            is EstadoAutenticacion.LoggedIn -> {
                navController.navigate(Destinos.HomeCasanareVista.ruta) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            }

            EstadoAutenticacion.LoggedOut -> {
                navController.navigate(Destinos.CasanareLoginScreen.ruta) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            }

            else -> {}
        }
    }


    if (showScaffold) {
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                if (shouldShowBottomBar(currentRoute)) {
                    NavegacionInferior(navController, bottomNavDestinations)
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
            when (currentRoute) {
                Destinos.Pantalla1.ruta -> Inicio(innerPadding)
                Destinos.Pantalla2.ruta -> Emisoras(innerPadding)
                Destinos.Pantalla3.ruta -> Noticias_Regionales(innerPadding)
                Destinos.Pantalla4.ruta -> Noticias_Nacionales(innerPadding)
                Destinos.Pantalla5.ruta -> Noticias_Internacionales(innerPadding)
                Destinos.Pantalla6.ruta -> Programacion(innerPadding)
                Destinos.Pantalla7.ruta -> Programas(innerPadding)
                Destinos.Pantalla8.ruta -> Podcast(innerPadding)
                Destinos.Pantalla9.ruta -> Contactenos(innerPadding)
                Destinos.Pantalla10.ruta -> Clasificados(innerPadding)
                Destinos.Pantalla11.ruta -> Youtube_Casanare(innerPadding)
                Destinos.Pantalla12.ruta -> Configuraciones(innerPadding)
                Destinos.Pantalla13.ruta -> CerrarSesionButton(navController, innerPadding)
                Destinos.Pantalla14.ruta -> Preferencias(innerPadding)
                Destinos.Pantalla15.ruta -> Se_Le_Tiene(innerPadding)
                Destinos.Pantalla16.ruta -> VideosYoutubeView(navController, innerPadding)
                Destinos.Pantalla17.ruta -> Mi_Zona(innerPadding)
                else -> {} // Manejar otras rutas o mostrar un mensaje de error
            }
        }
    }
}

fun shouldShowBottomBar(currentRoute: String): Boolean {
    return currentRoute == Destinos.Pantalla1.ruta ||
            currentRoute == Destinos.Pantalla2.ruta ||
            currentRoute == Destinos.Pantalla8.ruta ||
            currentRoute == Destinos.Pantalla14.ruta
}

fun shouldShowTopBar(currentRoute: String): Boolean {
    return currentRoute != Destinos.PantallaPresentacion.ruta &&
            currentRoute != Destinos.CasanareLoginScreen.ruta &&
            currentRoute != Destinos.SplashScreen.ruta &&
            currentRoute != PantallaFormulario.SeleccionRol.ruta &&
            currentRoute != PantallaFormulario.Estudiantes.ruta &&
            currentRoute != PantallaFormulario.Estudiantes1.ruta &&
            currentRoute != PantallaFormulario.Estudiantes2.ruta &&
            currentRoute != PantallaFormulario.Estudiantes3.ruta &&
            currentRoute != PantallaFormulario.Docentes.ruta
}

fun shouldShowDrawer(currentRoute: String): Boolean {
    return currentRoute != Destinos.PantallaPresentacion.ruta &&
            currentRoute != Destinos.CasanareLoginScreen.ruta &&
            currentRoute != Destinos.SplashScreen.ruta &&
            currentRoute != PantallaFormulario.SeleccionRol.ruta &&
            currentRoute != PantallaFormulario.Estudiantes.ruta &&
            currentRoute != PantallaFormulario.Estudiantes1.ruta &&
            currentRoute != PantallaFormulario.Estudiantes2.ruta &&
            currentRoute != PantallaFormulario.Estudiantes3.ruta &&
            currentRoute != PantallaFormulario.Docentes.ruta
}

@Composable
fun NavegacionInferior(
    navController: NavHostController,
    items: List<Destinos>
) {
    BottomAppBar(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            //.height(30.dp)
            .fillMaxWidth()

    ) {
        BottomNavigation(
            backgroundColor = Color.LightGray,
        ) {
            val currentRoute = currentRoute(navController = navController)
            items.forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.ruta,
                    onClick = {
                        navController.navigate(item.ruta) {
                            /*popUpTo(navController.graph.startDestinationId) {
                                saveState =true
                            }*/
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        item.icon?.let {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title,
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        }

                    },
                    label = {
                        Text(item.title)
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

@Composable
fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    items: List<Destinos>,
    viewModel: HomeViewModel

) {
    currentRoute(navController = navController)

    TopAppBar(
        backgroundColor = Color(0xFF000000).copy(alpha = 0.5f),
        contentColor = Color.Black,
        modifier = Modifier,
        //.height(120.dp),

        //.verticalScroll(rememberScrollState()),


        title = {
            Text(
                text = viewModel.currentTitle,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier,

                )


        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },

                ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp),

                    )
            }
        },
        actions = {
            TextButton(onClick = {
                navController.navigate(Destinos.Pantalla15.ruta)
            }) {
                Text("Se le tiene")
            }
            TextButton(onClick = { navController.navigate(Destinos.Pantalla16.ruta) }) {
                Text("Educación")
            }
            TextButton(onClick = { navController.navigate(Destinos.Pantalla17.ruta) }) {
                Text("Mi zona")
            }
        }

    )

}


@Composable
fun Drawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    items: List<Destinos>,

    ) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painterResource(id = R.drawable.fondo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(Color(0xFF000000).copy(alpha = 0.7f)),
            contentScale = ContentScale.FillWidth,

            )
        Spacer(
            modifier = Modifier
                .height(15.dp)
                .fillMaxWidth()
        )
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            DrawerItem(
                item = item,
                selected = currentRoute == item.ruta,
            ) {
                navController.navigate(it.ruta) {
                    launchSingleTop = true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun DrawerItem(
    item: Destinos,
    selected: Boolean,
    onItemClick: (Destinos) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.surface
            )
            .padding(8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically,


        ) {
        if (item.icon != null)
            Image(
                painterResource(id = item.icon),
                contentDescription = item.title
            )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.background,

            )

    }
}

// Funcion para el resalte del la opcion seleccionada en el menu
@Composable
fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntryAsState().value?.destination?.route
}




