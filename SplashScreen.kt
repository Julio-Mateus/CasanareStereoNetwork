package com.jcmateus.casanarestereo


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.launch
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
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(
    navController: NavHostController,
    authService: AuthService,
    loginViewModel: LoginScreenViewModel,
    dataStoreManager: DataStoreManager,
    emisoraRepository: EmisoraRepository
) {
    val context = LocalContext.current
    val authState by authService.authState.collectAsStateWithLifecycle()
    var isFirstTime by remember { mutableStateOf(true) }
    var hasShownPresentation by remember { mutableStateOf(false) }
    var destination by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isCreatingAccount by remember { mutableStateOf(false) }
    var hasCompletedForm by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
    isFirstTime = dataStoreManager.isFirstTimeAppOpen().first()
    hasShownPresentation = dataStoreManager.getHasShownPresentation().first()
    hasCompletedForm = dataStoreManager.getHasCompletedForm().first()
    authService.checkSession()
}

    suspend fun determineNavigationDestination(role: Rol?) {
        Log.d(
            "SplashScreen",
            "isFirstTime: $isFirstTime, hasShownPresentation: $hasShownPresentation, role: $role, hasCompletedForm: $hasCompletedForm"
        )
        destination = when {
            isFirstTime -> {
                Destinos.PantallaPresentacion.ruta
            }
            role == Rol.USUARIO && !hasCompletedForm -> {
                PantallaFormulario.SeleccionRol.ruta
            }

            role == Rol.ADMINISTRADOR -> Destinos.Pantalla1.ruta
            role == Rol.USUARIO -> Destinos.UsuarioPerfilScreen.ruta // Navega directo al perfil
            role == Rol.EMISORA -> {
                val userId = authService.getCurrentUser()?.uid
                if (userId != null) {
                    try {
                        val perfil = emisoraRepository.cargarPerfilEmisora(userId)
                        if (perfil != null) {
                            Destinos.EmisoraVista.ruta
                        } else {
                            Destinos.FormularioPerfilEmisora.ruta
                        }
                    } catch (e: Exception) {
                        Log.e("SplashScreen", "Error al cargar el perfil de la emisora", e)
                        Destinos.CasanareLoginScreen.ruta
                    }
                } else {
                    Log.e("SplashScreen", "No se pudo obtener el userId")
                    Destinos.CasanareLoginScreen.ruta
                }
            }

            else -> Destinos.CasanareLoginScreen.ruta
        }
        Log.d("SplashScreen", "Destination: $destination")
    }

    LaunchedEffect(authState, isFirstTime, hasShownPresentation, hasCompletedForm) {
        val currentAuthState = authState
        Log.d("SplashScreen", "Estado de autenticación: $currentAuthState")
        when (currentAuthState) {
            is EstadoAutenticacion.LoggedIn -> {
                determineNavigationDestination(currentAuthState.rol)
            }

            is EstadoAutenticacion.LoggedInWithPendingRol -> {
                val rolFromDataStore = dataStoreManager.getRol().first()
                determineNavigationDestination(rolFromDataStore)
            }

            is EstadoAutenticacion.LoggedOut -> {
                destination = Destinos.CasanareLoginScreen.ruta
            }

            is EstadoAutenticacion.Loading -> {
                // No hacemos nada aquí, el when de abajo se encarga de mostrar el indicador
            }

            is EstadoAutenticacion.Error -> {
                Toast.makeText(context, currentAuthState.message, Toast.LENGTH_LONG).show()
                destination = Destinos.CasanareLoginScreen.ruta
            }

            else -> {
                Log.e("SplashScreen", "Estado de autenticación desconocido")
                destination = Destinos.CasanareLoginScreen.ruta
            }
        }
        if (isFirstTime || !hasShownPresentation) {
            dataStoreManager.saveAppOpened()
        }
        if (destination != null) {
            if (destination == Destinos.PantallaPresentacion.ruta) {
                navController.navigate(destination!!) {
                    popUpTo(Destinos.SplashScreen.ruta) { inclusive = true }
                }
                delay(3000)
                dataStoreManager.savePresentationShown()
                val rol = dataStoreManager.getRol().first()
                determineNavigationDestination(rol)
            } else {
                navController.navigate(destination!!) {
                    popUpTo(Destinos.SplashScreen.ruta) { inclusive = true }
                }
            }
            isLoading = false
        }
    }
    if (isLoading) {
        Splash()
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
                        .alpha(alpha),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}