package com.jcmateus.casanarestereo.screens.menus.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jcmateus.casanarestereo.R

@Composable
fun PantallaTema(innerPadding: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Tema",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Aquí podrás cambiar el tema de la aplicación.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        // Aquí va la interfaz de usuario para seleccionar el tema
        SelectorDeTema()
    }
}

@Composable
fun SelectorDeTema() {
    var temaSeleccionado by remember { mutableStateOf(OpcionTema.PredeterminadoDelSistema) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ItemOpcionTema(
            opcionTema = OpcionTema.PredeterminadoDelSistema,
            estaSeleccionado = temaSeleccionado == OpcionTema.PredeterminadoDelSistema,
            onSeleccionar = { temaSeleccionado = OpcionTema.PredeterminadoDelSistema }
        )
        ItemOpcionTema(
            opcionTema = OpcionTema.Claro,
            estaSeleccionado = temaSeleccionado == OpcionTema.Claro,
            onSeleccionar = { temaSeleccionado = OpcionTema.Claro }
        )
        ItemOpcionTema(
            opcionTema = OpcionTema.Oscuro,
            estaSeleccionado = temaSeleccionado == OpcionTema.Oscuro,
            onSeleccionar = { temaSeleccionado = OpcionTema.Oscuro }
        )
    }
}

@Composable
fun ItemOpcionTema(opcionTema: OpcionTema, estaSeleccionado: Boolean, onSeleccionar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onSeleccionar)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RadioButton(
            selected = estaSeleccionado,
            onClick = onSeleccionar,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Icon(
            imageVector = opcionTema.icono,
            contentDescription = opcionTema.titulo,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = opcionTema.titulo,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

enum class OpcionTema(val titulo: String, val icono: ImageVector) {
    PredeterminadoDelSistema("Predeterminado del Sistema", Icons.Filled.AutoMode),
    Claro("Claro", Icons.Filled.LightMode),
    Oscuro("Oscuro", Icons.Filled.DarkMode)
}