package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@Composable
fun VistaPrograma(programa: Contenido.Programa?, innerPadding: PaddingValues) {
    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()) {
        programa?.let {
            Text("Título: ${it.nombrePrograma}")
            Text("Descripción: ${it.descripcionPrograma}")
            // ... mostrar otros campos del programa ...
        } ?: run {
            Text("Programa no encontrado")
        }
    }
}