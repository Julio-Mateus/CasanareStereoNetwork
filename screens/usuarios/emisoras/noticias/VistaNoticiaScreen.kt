package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaNoticiaScreen(navController: NavController) {
    val noticiaJson = navController.currentBackStackEntry?.arguments?.getString("noticiaJson")
    Scaffold { innerPadding ->
        VistaNoticia(
            noticiaJson = noticiaJson,
            innerPadding = innerPadding,
            navController = navController
        )
    }
}