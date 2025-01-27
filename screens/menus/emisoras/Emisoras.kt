package com.jcmateus.casanarestereo.screens.menus.emisoras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora


@Composable
fun EmisorasScreen(innerPadding: PaddingValues, navController: NavController) {
    val emisoraViewModel: EmisoraViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
    )
    val emisorasCercanas by emisoraViewModel.emisorasCercanas.collectAsState()
    val isLoading by emisoraViewModel.isLoading.collectAsState()
    var emisoraSeleccionada by remember { mutableStateOf<PerfilEmisora?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (emisoraSeleccionada != null) {
                // Mostrar detalles de la emisora seleccionada
                EmisoraDetalles(
                    emisora = emisoraSeleccionada!!,
                    navController = navController,
                    //onPodcastClick = { /* TODO: Navegar a la pantalla de podcasts */ },
                    //onNoticiasClick = { /* TODO: Navegar a la pantalla de noticias */ },
                    //onProgramasClick = { /* TODO: Navegar a la pantalla de programas */ }
                )
            } else {
                if (emisorasCercanas.isEmpty()) {
                    Text(text = "No hay emisoras cercanas", style = MaterialTheme.typography.bodyLarge)
                } else {
                    EmisorasList(emisoras = emisorasCercanas, navController = navController) { emisora ->
                        emisoraSeleccionada = emisora
                    }
                }
            }
        }
    }
}

@Composable
fun EmisorasList(emisoras: List<PerfilEmisora>, navController: NavController, onEmisoraClick: (PerfilEmisora) -> Unit) {
    LazyColumn {
        items(emisoras) { emisora ->
            EmisoraItem(emisora = emisora, navController = navController, onEmisoraClick = onEmisoraClick)
        }
    }
}