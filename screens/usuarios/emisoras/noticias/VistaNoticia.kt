package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@Composable
fun VistaNoticia(noticia: Contenido.Noticia?, innerPadding: PaddingValues, navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navegar al formulario de modificación
                navController.navigate(Destinos.FormularioNoticia.ruta)
            }) {
                Icon(Icons.Filled.Edit, contentDescription = "Modificar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            noticia?.let {
                // Mostrar la imagen de la noticia
                AsyncImage(
                    model = it.imagenUriNoticia,
                    contentDescription = "Imagen de la noticia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar el título de la noticia
                Text(
                    text = it.tituloNoticia,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar la fuente y la fecha de publicación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Fuente: ${it.fuenteNoticia}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Fecha: ${it.fechaPublicacionNoticia}", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar el contenido de la noticia
                Text(text = it.contenidoNoticia, style = MaterialTheme.typography.bodyMedium)

                // ... mostrar otros campos de la noticia ...
            } ?: run {
                Text("Noticia no encontrada")
            }
        }
    }
}

@Composable
@Preview
fun VistaNoticiaPreview() {
    VistaNoticia(navController = NavHostController(LocalContext.current), innerPadding = PaddingValues(), noticia = null)
}