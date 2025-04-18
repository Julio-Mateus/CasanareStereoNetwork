package com.jcmateus.casanarestereo.screens.menus.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaNotificaciones(innerPadding: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Aquí podrás gestionar las notificaciones de la aplicación.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        // Aquí va la interfaz de usuario para gestionar las notificaciones
        ConfiguracionNotificaciones()
    }
}

@Composable
fun ConfiguracionNotificaciones() {
    var notificacionesGeneralesActivadas by remember { mutableStateOf(true) }
    var notificacionesNoticiasActivadas by remember { mutableStateOf(true) }
    var notificacionesActualizacionesActivadas by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        ItemConfiguracionNotificacion(
            titulo = "Notificaciones Generales",
            descripcion = "Recibir notificaciones generales de la aplicación.",
            estaActivado = notificacionesGeneralesActivadas,
            onCambioEstado = { notificacionesGeneralesActivadas = it }
        )

        ItemConfiguracionNotificacion(
            titulo = "Noticias",
            descripcion = "Recibir notificaciones sobre nuevas noticias.",
            estaActivado = notificacionesNoticiasActivadas,
            onCambioEstado = { notificacionesNoticiasActivadas = it }
        )

        ItemConfiguracionNotificacion(
            titulo = "Actualizaciones",
            descripcion = "Recibir notificaciones sobre actualizaciones de la aplicación.",
            estaActivado = notificacionesActualizacionesActivadas,
            onCambioEstado = { notificacionesActualizacionesActivadas = it }
        )
    }
}

@Composable
fun ItemConfiguracionNotificacion(
    titulo: String,
    descripcion: String,
    estaActivado: Boolean,
    onCambioEstado: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Switch(
                checked = estaActivado,
                onCheckedChange = onCambioEstado,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}