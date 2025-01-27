package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@Composable
fun VistaNoticiaUsuario(noticiaJson: String?, innerPadding: PaddingValues, navController: NavController) {

    // Convertir la cadena JSON a un objeto Noticia
    val gson = Gson()
    val noticia = try {
        noticiaJson?.let { Gson().fromJson(it, Contenido.Noticia::class.java) }
    } catch (e: JsonSyntaxException) {
        Log.e("VistaNoticiaUsuario", "Error al analizar JSON: ${e.message}")
        null // o maneja el error de otra manera
    }

    val imageUri = navController.previousBackStackEntry?.savedStateHandle?.get<String>("imageUri")

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageUri != null && Uri.parse(imageUri).isAbsolute) { // Comprueba si la URI es válida
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Imagen de la noticia",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Muestra un marcador de posición o un mensaje de error
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)) {
                    Text("Imagen no disponible", modifier = Modifier.align(Alignment.Center))
                }
            }
            noticia?.let {
                // Mostrar la imagen de la noticia con fondo y tamaño ajustado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Aumentar la altura de la imagen
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                ) {
                    AsyncImage(
                        model = it.imagenUriNoticia,
                        contentDescription = "Imagen de la noticia",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Aumentar el espaciado

                // Mostrar el título de la noticia con tamaño y color ajustados
                Text(
                    text = it.tituloNoticia,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(12.dp)) // Aumentar el espaciado

                // Mostrar la fuente y la fecha de publicación con color ajustado
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // Agregar padding horizontal
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fuente: ${it.fuenteNoticia}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Fecha: ${it.fechaPublicacionNoticia}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Aumentar el espaciado

                // Mostrar el contenido de la noticia con padding y tamaño de texto ajustado
                Text(
                    text = it.contenidoNoticia,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp) // Agregar padding horizontal
                )

                // Mostrar otros campos de la noticia con estilos similares
                Spacer(modifier = Modifier.height(16.dp))

                // Autor
                Text(
                    text = "Autor: ${it.autorNoticia}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Enlace
                if (it.enlaceNoticia.isNotBlank()) {
                    Text(
                        text = "Enlace: ${it.enlaceNoticia}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ubicación
                Text(
                    text = "Ubicación: ${it.ubicacionNoticia}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Etiqueta
                Text(
                    text = "Etiqueta: ${it.etiquetaNoticia}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp)) // Aumentar el espaciado final
            } ?: run {
                Text("Noticia no encontrada")
            }
        }
    }
}