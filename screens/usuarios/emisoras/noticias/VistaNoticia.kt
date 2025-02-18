package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.tasks.await
import kotlin.io.path.exists
import kotlin.text.get
import kotlin.text.set

@Composable
fun VistaNoticia(
    noticiaJson: String? = null,
    innerPadding: PaddingValues,
    navController: NavController
) {

    // Obtener el userId
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    // Obtener la fábrica de ViewModel
    val factory =
        (LocalContext.current.applicationContext as HomeApplication).noticiaViewModelFactory

    // Obtener el ViewModel utilizando la fábrica
    val noticiaViewModel: NoticiaViewModel = viewModel(factory = factory)
    val errorEliminandoNoticia by noticiaViewModel.errorEliminandoNoticia.collectAsState()
    val context = LocalContext.current

    // Convertir la cadena JSON a un objeto Noticia
    val gson = Gson()
    val noticia = try {
        noticiaJson?.let { Gson().fromJson(it, Contenido.Noticia::class.java) }
    } catch (e: JsonSyntaxException) {
        Log.e("VistaNoticia", "Error al analizar JSON: ${e.message}")
        null // o maneja el error de otra manera
    }

    //val imageUri = navController.previousBackStackEntry?.savedStateHandle?.get<String>("imageUri")
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = errorEliminandoNoticia) {
        if (errorEliminandoNoticia != null) {
            Toast.makeText(
                context,
                "Error al eliminar la noticia: $errorEliminandoNoticia",
                Toast.LENGTH_LONG
            ).show()
            noticiaViewModel.restablecerErrorEliminandoNoticia()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar Noticia") },
            text = { Text("¿Estás seguro de que quieres eliminar esta noticia?") },
            confirmButton = {
                TextButton(onClick = {
                    // Llamar a eliminarNoticia solo con noticia.id
                    noticia?.id?.let {
                        noticiaViewModel.eliminarNoticia(it, userId)
                        showDialog = false
                        navController.popBackStack()
                    } ?: run {
                        Toast.makeText(context, "Error al eliminar la noticia", Toast.LENGTH_LONG)
                            .show()
                    }
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            noticia?.let {
                // Mostrar la imagen de la noticia con fondo y tamaño ajustado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Aumentar la altura de la imagen
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                ) {
                    if (noticia?.imagenUriNoticia != null && Uri.parse(noticia.imagenUriNoticia).isAbsolute) { // Comprueba si la URI es válida
                        AsyncImage(
                            model = noticia.imagenUriNoticia,
                            contentDescription = "Imagen de la noticia",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Muestra un marcador de posición o un mensaje de error
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                        ) {
                            Text(
                                "Imagen no disponible",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
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
                Text(
                    text = "Enlace: ${it.enlaceNoticia}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )

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
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
            FloatingActionButton(onClick = {
                noticia?.let {
                    //navController.currentBackStackEntry?.savedStateHandle?.set("noticia", it)
                    //navController.currentBackStackEntry?.savedStateHandle?.set("userId", userId)
                    navController.navigate("${Destinos.FormularioNoticia.ruta}/${it.id}")
                }
            }) {
                Icon(Icons.Filled.Edit, contentDescription = "Modificar")
            }
        }
    }
}

@Composable
@Preview
fun VistaNoticiaPreview() {
    VistaNoticia(
        navController = NavHostController(LocalContext.current),
        innerPadding = PaddingValues(),
        noticiaJson = null
    )
}