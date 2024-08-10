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
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomAppBar
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ScaffoldState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = (application as HomeApplication).navController // Obtener navController de la aplicación
        setContent {
            CasanareStereoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(navController)
                }
            }

        }


    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController){
    val scaffoldState = rememberScaffoldState()
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
    val bottomNavDestinations  = listOf(
        Destinos.Pantalla1, // Inicio
        Destinos.Pantalla2, // Emisoras
        Destinos.Pantalla8, // Podcast
        Destinos.Pantalla14, // Preferencias
    )
    /*val navigation_item1 = listOf(
        Personales_menu.Pantalla15,
        Personales_menu.VideosYoutube,
        Personales_menu.Pantalla17
    )*/
   Scaffold(
       scaffoldState = scaffoldState,
       bottomBar = {NavegacionInferior(navController, bottomNavDestinations)},
       topBar = { TopBar(scope, scaffoldState, navController, allDestinations) },
       drawerContent = { Drawer(
           scope,
           scaffoldState,
           navController,
           items = allDestinations
       )
       }
   ){innerPadding -> // Añade innerPadding
       NavigationHost(navController,innerPadding) // Llama a NavigationHost

   }

}

@Composable
fun NavegacionInferior(
    navController: NavController,
    items: List<Destinos>
){
    BottomAppBar(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            //.height(30.dp)
            .fillMaxWidth()

    ) {
        BottomNavigation(
            backgroundColor = Color.LightGray,
        ){
            val currentRoute = currentRoute(navController = navController)
            items.forEach{item ->
                BottomNavigationItem(selected = currentRoute == item.ruta,
                    onClick = {
                        navController.navigate(item.ruta){
                            popUpTo(navController.graph.startDestinationId){
                                saveState = true
                            }
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
                    label = {Text(item.title)
                            },
                    alwaysShowLabel = false
                )
            }
        }
    }
}
@Composable
fun  TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    items: List<Destinos>,

){
    val currentRoute = currentRoute(navController = navController)


    var miTitulo = "Menú"
    items.forEach{item ->
        if (currentRoute == item.ruta) miTitulo = item.title

    }
    /*var personaleMenu = "Educativo"
    navigationItem1.forEach{ item1 ->
        if (currentRoute == item1.ruta2) personaleMenu = item1.title2
    }*/

    currentRoute(navController = navController)

    TopAppBar(
        backgroundColor = Color(0xFF000000).copy(alpha = 0.5f),
        contentColor = Color.White,
        modifier = Modifier,
            //.height(120.dp),

            //.verticalScroll(rememberScrollState()),


        title = { Text(text = miTitulo,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier,

        )


                },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            },

                ){
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
                navController.navigate(Destinos.Pantalla15.ruta) }) {
                Text("Se le tiene")
            }
            TextButton(onClick = {  navController.navigate(Destinos.Pantalla16.ruta) }) {
                Text("Educación")
            }
            TextButton(onClick = {  navController.navigate(Destinos.Pantalla17.ruta) }) {
                Text("Mi zona")
            }
        }

    )

}


@Composable
fun Drawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    items: List<Destinos>,

){

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ){
        Image(
            painterResource(id = R.drawable.fondo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(Color(0xFF000000).copy(alpha = 0.7f)),
            contentScale = ContentScale.FillWidth,

        )
        Spacer(modifier = Modifier
            .height(15.dp)
            .fillMaxWidth()
        )
        val currentRoute = currentRoute(navController)
        items.forEach {item ->
            DrawerItem(item = item,
                selected = currentRoute == item.ruta,
                ){
                navController.navigate(it.ruta){
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
fun DrawerItem(item: Destinos,
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
                if (selected) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.surface
            )
            .padding(8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically,


    ){
        if (item.icon != null)
        Image(painterResource(id = item.icon),
            contentDescription = item.title)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onPrimary,

        )

    }
}

// Funcion para el resalte del la opcion seleccionada en el menu
@Composable
fun currentRoute(navController: NavController): String?{
    return navController.currentBackStackEntryAsState().value?.destination?.route
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
        Home(navController = rememberNavController())

}