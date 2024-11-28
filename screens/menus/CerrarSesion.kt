package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModelFactory

@Composable
fun CerrarSesionButton(navController: NavHostController, innerPadding: PaddingValues) {
    val application = LocalContext.current.applicationContext as HomeApplication
    val factory = LoginScreenViewModelFactory(
        application.dataStoreManager,
        application.authService, // Accede a authService a través de application
        application.firebaseAuth
    )
    val loginViewModel: LoginScreenViewModel = viewModel(factory = factory)
    val authState = loginViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.cerrarSesion()
        navController.navigate(Destinos.CasanareLoginScreen.ruta)
    }

}