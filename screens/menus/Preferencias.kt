package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.screens.home.Destinos

@Composable
fun Preferencias(innerPadding: PaddingValues, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // ... (resto del contenido de tu vista) ...

        // Icono para abrir el menú desplegable
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "Menú")
        }

        // Menú desplegable
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Perfil") }, // Pasar el texto como un bloque de composición
                onClick = { navController.navigate(Destinos.EmisoraVista.ruta) }
            )
            DropdownMenuItem(
                text = { Text("Configuraciones") }, // Pasar el texto como un bloque de composición
                onClick = { navController.navigate(Destinos.Pantalla12.ruta) }
            )
            // ... (otras opciones del menú) ...
        }
    }
}