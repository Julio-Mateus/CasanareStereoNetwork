package com.jcmateus.casanarestereo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination

import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.currentRoute
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModelFactory
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(navController: NavHostController) {
    val dataStoreManager = (LocalContext.current.applicationContext as HomeApplication).dataStoreManager
    val viewModel: LoginScreenViewModel = remember {
        LoginScreenViewModelFactory(dataStoreManager).create(LoginScreenViewModel::class.java)
    }
    val authState = viewModel.authState.collectAsState()
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        Log.d("SplashScreen", "Estado de autenticación: ${authState.value}")
        viewModel.authState.collect { state ->
            delay(5000) // Retraso para simular la carga
            showSplash = false
            //navController.navigate(Destinos.HomeCasanareVista.ruta)
        }
    }

    if (showSplash) {
        LoadingScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
@Composable
fun Splash() {

    var blinking by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = blinking) {
        delay(1000) // Ajusta el tiempo de parpadeo
        blinking = !blinking
    }

    val alpha by animateFloatAsState(
        targetValue = if (blinking) 0.5f else 1f,
        animationSpec = tween(durationMillis = 500) // Ajusta la duración de la animación
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surface
            )
    ){

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ){
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    //.alpha(alpha)
            )
        }
    }

}
