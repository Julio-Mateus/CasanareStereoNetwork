package com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@Composable
fun VistaPodcast(podcast: Contenido.Podcast?, innerPadding: PaddingValues) {
    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()) {
        podcast?.let {
            Text("Título: ${it.tituloPodcast}")
            Text("Descripción: ${it.descripcionPodcast}")
            // ... mostrar otros campos del podcast ...
        } ?: run {
            Text("Podcast no encontrado")
        }
    }
}