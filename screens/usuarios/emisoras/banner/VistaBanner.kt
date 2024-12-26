package com.jcmateus.casanarestereo.screens.usuarios.emisoras.banner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@Composable
fun VistaBanner(banner: Contenido.Banner?, innerPadding: PaddingValues) {
    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()) {
        banner?.let {
            Text("Título: ${it.tituloPublicidad}")
            Text("Descripción: ${it.descripcionPublicidad}")
            // ... mostrar otros campos del banner ...
        } ?: run {
            Text("Banner no encontrado")
        }
    }
}