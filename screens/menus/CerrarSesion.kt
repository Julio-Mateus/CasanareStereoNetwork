package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun CerrarSesionButton(
    navController: NavHostController,
    authService: AuthService,
    innerPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()

    // Cerrar sesión y navegar a la pantalla de inicio de sesión
    LaunchedEffect(key1 = Unit) { // Ejecutar solo una vez
        coroutineScope.launch {
            authService.cerrarSesion()
            navController.navigate(Destinos.CasanareLoginScreen.ruta) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }

    // Puedes mostrar un mensaje de "Cerrando sesión..." mientras se completa el proceso
    Text(
        text = "Cerrando sesión...",
        modifier = Modifier.padding(innerPadding)
    )
}