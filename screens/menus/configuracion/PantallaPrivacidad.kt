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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
fun PantallaPrivacidad(innerPadding: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Privacidad",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Aquí podrás configurar las opciones de privacidad de tu cuenta.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        // Aquí va la interfaz de usuario para configurar la privacidad
        ConfiguracionPrivacidad()
    }
}

@Composable
fun ConfiguracionPrivacidad() {
    var mostrarPerfilPublicamente by remember { mutableStateOf(true) }
    var permitirBusquedaPorCorreo by remember { mutableStateOf(true) }
    var permitirBusquedaPorTelefono by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        ItemConfiguracionPrivacidad(
            titulo = "Mostrar perfil públicamente",
            descripcion = "Permitir que otros usuarios vean tu perfil.",
            estaActivado = mostrarPerfilPublicamente,
            onCambioEstado = { mostrarPerfilPublicamente = it }
        )

        ItemConfiguracionPrivacidad(
            titulo = "Permitir búsqueda por correo electrónico",
            descripcion = "Permitir que otros usuarios te encuentren usando tu correo electrónico.",
            estaActivado = permitirBusquedaPorCorreo,
            onCambioEstado = { permitirBusquedaPorCorreo = it }
        )

        ItemConfiguracionPrivacidad(
            titulo = "Permitir búsqueda por número de teléfono",
            descripcion = "Permitir que otros usuarios te encuentren usando tu número de teléfono.",
            estaActivado = permitirBusquedaPorTelefono,
            onCambioEstado = { permitirBusquedaPorTelefono = it }
        )
    }
}

@Composable
fun ItemConfiguracionPrivacidad(
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
            Checkbox(
                checked = estaActivado,
                onCheckedChange = onCambioEstado,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = Color.Gray
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}