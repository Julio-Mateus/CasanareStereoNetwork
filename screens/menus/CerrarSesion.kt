package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel

@Composable
fun CerrarSesionButton(navController: NavHostController) {
    val loginViewModel: LoginScreenViewModel = viewModel()
    Button(onClick = {
        loginViewModel.cerrarSesion()
        navController.navigate(Destinos.CasanareLoginScreen.ruta)
    }) {
        Text("Cerrar Sesión")
    }
}