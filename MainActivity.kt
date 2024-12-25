package com.jcmateus.casanarestereo

//import com.jcmateus.casanarestereo.navigation.NavegacionCasanare
//import com.jcmateus.casanarestereo.navigation.AuthenticationNavHost
//import com.jcmateus.casanarestereo.navigation.NavegacionCasanare
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.formulario.FormularioViewModel
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.createLoginViewModel
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = (application as HomeApplication).navController
        setContent {
            CasanareStereoTheme {
                navController.setViewModelStore(ViewModelStore())
                MainScreen(navController)
                //NavigationHost( navController, PaddingValues())
                //PantallaPresentacion(navController)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as HomeApplication
    val authService = AuthService(application.firebaseAuth) // Acceder a firebaseAuth desde HomeApplication
    val loginViewModel = createLoginViewModel(application)

    CasanareStereoTheme {
        NavigationHost(navController, PaddingValues(), loginViewModel, formularioViewModel = FormularioViewModel(), authService)
    }
}

@Composable
fun PantallaPresentacion(navController: NavHostController, loginViewModel: LoginScreenViewModel) {
    var user by remember { mutableStateOf<FirebaseUser?>(null) }
    val authListener = FirebaseAuth.AuthStateListener { auth ->
        user = auth.currentUser
    }
    var showAnimation by remember { mutableStateOf(false) }
    var animationMessage by remember { mutableStateOf("") }
    DisposableEffect(authListener) {
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(authListener)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Usamos el color de fondo del tema
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = 0.7f), // Oscurecemos la imagen con un filtro de color
                blendMode = BlendMode.Darken
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), //Padding consistente
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BIENVENIDO A:",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 43.sp,
                color = MaterialTheme.colorScheme.onBackground, // Color del texto sobre el fondo
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu imagen
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp) // Establece el tamaño del logo
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "CASANARE STEREO NETWORK",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
            )
            Text(
                text = "DONDE LATE EL CORAZÓN DEL LLANO",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
            )

            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    if (user != null) {
                        navController.navigate(Destinos.HomeCasanareVista.ruta) {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(Destinos.CasanareLoginScreen.ruta) {
                            launchSingleTop = true
                        }
                    }
                    // Mostrar mensaje o animación con la información del usuario
                    val currentUser = loginViewModel.authState.value
                    if (currentUser is EstadoAutenticacion.LoggedIn) {
                        // Accede a la información del usuario (correo o nombre)
                        val userEmail =
                            currentUser.user?.email ?: "" // O currentUser.user?.displayName

                        // Actualiza el estado para mostrar el Snackbar
                        showAnimation = true
                        animationMessage = "Bienvenido, $userEmail"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp), // Alto del botón
                shape = MaterialTheme.shapes.medium, // Usamos las formas del tema
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
            {
                Text(
                    "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            // Mostrar la animación si showAnimation es true
            AnimatedVisibility(visible = showAnimation) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        animationMessage,
                        fontSize = 20.sp
                    ) // Puedes reemplazar esto con tu animación
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground // Color del texto del botón
                )
            ) {
                Text(
                    "En otro momento",
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    "Registrate",
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Text(
            text = "Beneficios de tener una cuenta",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)

        )
    }
}


