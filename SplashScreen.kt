package com.jcmateus.casanarestereo


import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
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
fun SplashScreen(
    navController: NavHostController,
    authService: AuthService,
    loginViewModel: LoginScreenViewModel, // Recibir LoginScreenViewModel como parámetro
    dataStoreManager: DataStoreManager // Recibir dataStoreManager como parámetro
) {
    val context = LocalContext.current
    val authState = authService.authState.collectAsStateWithLifecycle()
    var isFirstTime by remember { mutableStateOf(true) }
    var hasShownPresentation by remember { mutableStateOf(false) }
    var userRole by remember { mutableStateOf(Rol.NO_DEFINIDO) }

    LaunchedEffect(key1 = Unit) {
        isFirstTime = dataStoreManager.isFirstTimeAppOpen().first()
        hasShownPresentation = dataStoreManager.getHasShownPresentation().first()
        userRole = dataStoreManager.getRolUsuario().first()
    }
    var currentAuthState by remember { mutableStateOf<EstadoAutenticacion>(EstadoAutenticacion.Loading) }
    LaunchedEffect(authState.value, isFirstTime, hasShownPresentation, userRole) {
        Log.d("SplashScreen", "Estado de autenticación: ${authState.value}")
        currentAuthState = authState.value
        if (isFirstTime || !hasShownPresentation) {
            dataStoreManager.saveAppOpened() // Correcto: Llamar a la función suspend directamente
            navController.navigate(Destinos.PantallaPresentacion.ruta)
        } else {
            when (val state = authState.value) {
                is EstadoAutenticacion.LoggedIn -> {
                    Log.d("SplashScreen", "Rol de usuario recuperado: $userRole")
                    when (userRole) {
                        Rol.USUARIO -> {
                            navController.navigate(Destinos.UsuarioPerfilScreen.ruta) // Navegar al perfil de usuario
                        }

                        Rol.EMISORA -> {
                            navController.navigate(Destinos.FormularioPerfilEmisora.ruta) // Navegar al perfil de emisora
                        }

                        Rol.NO_DEFINIDO -> {
                            navController.navigate(Destinos.CasanareLoginScreen.ruta) // Navegar al login
                        }
                    }
                }

                is EstadoAutenticacion.LoggedInWithPendingRol -> {
                    // Aquí puedes mostrar un indicador de carga o esperar a que el rol se cargue
                    Log.d("SplashScreen", "Usuario logueado, esperando rol...")
                }

                EstadoAutenticacion.LoggedOut -> {
                    navController.navigate(Destinos.CasanareLoginScreen.ruta)
                }

                EstadoAutenticacion.Loading -> {
                    // No hacemos nada aquí, el when de abajo se encarga de mostrar el indicador
                }

                is EstadoAutenticacion.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (currentAuthState is EstadoAutenticacion.Loading || currentAuthState is EstadoAutenticacion.LoggedInWithPendingRol) {
            CircularProgressIndicator()
        }
    }
    when (val state = currentAuthState) {
        EstadoAutenticacion.Loading -> {
            Splash()
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

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
            }
        }
    }
}