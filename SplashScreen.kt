package com.jcmateus.casanarestereo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination

import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.currentRoute
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModelFactory
import com.jcmateus.casanarestereo.screens.login.Rol
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(navController: NavHostController, authService: AuthService) {
    val application = LocalContext.current.applicationContext as HomeApplication
    val dataStoreManager = application.dataStoreManager
    val viewModel: LoginScreenViewModel = remember {
        LoginScreenViewModelFactory(
            dataStoreManager,
            authService,
            application.firebaseAuth // Pasar firebaseAuth desde HomeApplication
        ).create(LoginScreenViewModel::class.java)
    }
    val authState = viewModel.authState.collectAsStateWithLifecycle() // Use collectAsStateWithLifecycle

    LaunchedEffect(key1 = authState.value) {
        Log.d("SplashScreen", "Estado de autenticación: ${authState.value}")
        delay(5000)
        when (authState.value) {
            is EstadoAutenticacion.LoggedIn -> {
                delay(100)
                // Verificar si el usuario ha seleccionado un rol
                val userRole = dataStoreManager.getRolUsuario().first()
                if (userRole == Rol.USUARIO || userRole == Rol.EMISORA) {
                    // El usuario ya ha seleccionado un rol, navegar a la pantalla principal
                    navController.navigate(Destinos.Pantalla1.ruta)
                } else {
                    // El usuario no ha seleccionado un rol, navegar a la pantalla de selección de roles
                    navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                }
            }
            EstadoAutenticacion.LoggedOut -> {
                // Usuario no ha iniciado sesión, navegar a la pantalla de inicio de sesión
                navController.navigate(Destinos.CasanareLoginScreen.ruta)
            }
            EstadoAutenticacion.Loading -> {
                // Mostrar la pantalla de carga mientras se verifica el estado de autenticación
            }
        }
    }
    @Composable
    fun Splash() {
        var visible by remember { mutableStateOf(false) }
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        LaunchedEffect(Unit) {
            visible = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo1),
                        contentDescription = "Splash Image",
                        modifier = Modifier
                            .size(150.dp)
                            .alpha(alpha)
                    )
                    /*
                    Spacer(modifier = Modifier.height(8.dp)) // Espacio entre imágenes
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Reemplaza con tu segunda imagen
                        contentDescription = "Splash Image 2",
                        modifier = Modifier.size(140.dp) // Ajusta el tamaño según sea necesario
                    )
                     */
                }
            }
        }
    }
    Splash()
}