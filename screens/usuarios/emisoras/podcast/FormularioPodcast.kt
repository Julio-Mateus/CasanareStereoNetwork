package com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPodcast(navController: NavHostController, onGuardar: (Contenido.Podcast) -> Unit) {
    var tituloPodcast by remember { mutableStateOf("") }
    var descripcionPodcast by remember { mutableStateOf("") }
    var audioUriPodcast by remember { mutableStateOf("") }
    var fechaPodcast by remember { mutableStateOf("") }
    var autorPodcast by remember { mutableStateOf("") }
    var enlacePodcast by remember { mutableStateOf("") }
    var imagenUriPodcast by remember { mutableStateOf("") }
    var etiquetaPodcast by remember { mutableStateOf("") }
    var duracionPodcast by remember { mutableStateOf("") }
    var numeroEpisodioPodcast by remember { mutableStateOf("") }
    var numeroTemporadaPodcast by remember { mutableStateOf("") }

    val context = LocalContext.current

    val audioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        audioUriPodcast = uri?.toString() ?: ""
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUriPodcast = uri?.toString() ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Podcast") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Validación de datos (ejemplo: título no vacío)
                        if (tituloPodcast.isBlank()) {
                            // Mostrar un mensaje de error al usuario
                            Toast.makeText(
                                context,
                                "El título de la podcast no puede estar vacío",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

                        // Guardar los cambios
                        val podcast = Contenido.Podcast(
                            tituloPodcast,
                            descripcionPodcast,
                            audioUriPodcast,
                            fechaPodcast,
                            autorPodcast,
                            enlacePodcast,
                            imagenUriPodcast,
                            etiquetaPodcast,
                            duracionPodcast,
                            numeroEpisodioPodcast,
                            numeroTemporadaPodcast
                        )
                        onGuardar(podcast)
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar")
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = tituloPodcast,
                onValueChange = { tituloPodcast = it },
                label = { Text("Título del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcionPodcast,
                onValueChange = { descripcionPodcast = it },
                label = { Text("Descripción del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo para el audio (se abre un selector de archivos)
            Button(onClick = { audioLauncher.launch("audio/*") }) {
                Text("Seleccionar audio")
            }
            Text("Audio seleccionado: $audioUriPodcast")

            OutlinedTextField(
                value = fechaPodcast,
                onValueChange = { fechaPodcast = it },
                label = { Text("Fecha del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = autorPodcast,
                onValueChange = { autorPodcast = it },
                label = { Text("Autor del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = enlacePodcast,
                onValueChange = { enlacePodcast = it },
                label = { Text("Enlace del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo para la imagen (se abre un selector de archivos)
            Button(onClick = { imageLauncher.launch("image/*") }) {
                Text("Seleccionar imagen")
            }
            Text("Imagen seleccionada: $imagenUriPodcast")

            OutlinedTextField(
                value = etiquetaPodcast,
                onValueChange = { etiquetaPodcast = it },
                label = { Text("Etiqueta del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = duracionPodcast,
                onValueChange = { duracionPodcast = it },
                label = { Text("Duración del Podcast") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = numeroEpisodioPodcast,
                onValueChange = { numeroEpisodioPodcast = it },
                label = { Text("Número de Episodio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = numeroTemporadaPodcast,
                onValueChange = { numeroTemporadaPodcast = it },
                label = { Text("Número de Temporada") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                val podcast = Contenido.Podcast(
                    tituloPodcast,
                    descripcionPodcast,
                    audioUriPodcast,
                    fechaPodcast,
                    autorPodcast,
                    enlacePodcast,
                    imagenUriPodcast,
                    etiquetaPodcast,
                    duracionPodcast,
                    numeroEpisodioPodcast,
                    numeroTemporadaPodcast
                )
                onGuardar(podcast)
            }) {
                Text("Guardar")
            }
        }
    }


}

@Composable
@Preview
fun FormularioPodcastPreview() {
    FormularioPodcast(navController = rememberNavController(), onGuardar = {})
}