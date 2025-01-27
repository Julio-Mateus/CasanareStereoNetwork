package com.jcmateus.casanarestereo.screens.usuarios.emisoras

import android.annotation.SuppressLint
import android.net.Uri
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.jcmateus.casanarestereo.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.home.Destinos
import kotlinx.coroutines.delay
import kotlin.random.Random


// Vista del perfil de la emisora
@Composable
fun EmisoraVista(
    navController: NavHostController,
    emisoraViewModel: EmisoraViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
    )
) {
    val emisoraViewModel: EmisoraViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
    )

    val perfilEmisora by emisoraViewModel.perfilEmisora.collectAsState()
    var isPlaying by remember { mutableStateOf(false) }

    val isLoading by emisoraViewModel.isLoading.collectAsState()


    // Botón de reproducción
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    if (isLoading) {
        CircularProgressIndicator() // Mostrar indicador de carga
    } else {
        perfilEmisora?.let { perfil ->
            // Mostrar la información de la emisora
            Text(text = perfil.nombre)
            // ...
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Destinos.FormularioPerfilEmisora.ruta) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar perfil")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Scroll habilitado
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección del perfil (imagen e información)
            if (perfilEmisora.imagenPerfilUri.isNotBlank()) {
                AsyncImage(
                    model = Uri.parse(perfilEmisora.imagenPerfilUri),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Red, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_pre),
                    contentDescription = "Imagen de perfil predeterminada",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(5.dp, Color.Red, CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = perfilEmisora.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = perfilEmisora.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = perfilEmisora.paginaWeb,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = perfilEmisora.departamento,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Divider(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .height(1.dp)
                            .width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = perfilEmisora.ciudad,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = perfilEmisora.frecuencia,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        isPlaying = !isPlaying
                        if (isPlaying) {
                            val mediaItem = MediaItem.fromUri(perfilEmisora.enlace)
                            exoPlayer.setMediaItem(mediaItem)
                            exoPlayer.prepare()
                            exoPlayer.play()
                        } else {
                            exoPlayer.pause()
                        }
                    },
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(8.dp)
                ) {
                    Box {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            modifier = Modifier
                                .size(38.dp)
                                .align(Alignment.Center)
                        )
                        if (isPlaying) {
                            PlaybackWaves(isPlaying, waveSize = 60.dp, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
            // Botones de navegación
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(Destinos.FormularioNoticia.ruta) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Altura del botón
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Color de fondo
                        contentColor = MaterialTheme.colorScheme.onPrimary // Color del texto
                    )
                ) {
                    Text("Subir noticia", fontSize = 16.sp)
                }

                Button(
                    onClick = { navController.navigate(Destinos.FormularioPodcast.ruta) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Subir podcast", fontSize = 16.sp)
                }

                Button(
                    onClick = { navController.navigate(Destinos.FormularioPrograma.ruta) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Subir programa", fontSize = 16.sp)
                }

                Button(
                    onClick = { navController.navigate(Destinos.FormularioBanner.ruta) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Subir banner", fontSize = 16.sp)
                }
            }
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
fun PlaybackWaves(isPlaying: Boolean, waveSize: Dp, modifier: Modifier = Modifier) {
    val waveHeights = remember { mutableStateListOf<Float>() }
    val coroutineScope = rememberCoroutineScope()

    // Inicializar waveHeights con valores aleatorios
    LaunchedEffect(Unit) {
        repeat(10) { // Ajusta el número de palitos
            waveHeights.add(Random.nextFloat() * 40f) // Ajusta la altura máxima
        }
    }

    LaunchedEffect(key1 = isPlaying) {
        if (isPlaying) {
            while (true) {
                waveHeights.forEachIndexed { index, _ ->
                    waveHeights[index] = Random.nextFloat() * 40f
                }
                delay(50) // Ajusta la duración de la animación
            }
        }
    }

    Canvas(modifier = Modifier.size(waveSize)) {
        val barWidth = size.width / waveHeights.size
        waveHeights.forEachIndexed { index, height ->
            drawRect(
                color = Color.White, // Cambia el color si es necesario
                topLeft = Offset(index * barWidth, size.height - height),
                size = Size(barWidth, height)
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun EmisoraVistaPreview() {
    val context = LocalContext.current
    val application = context.applicationContext as HomeApplication
    val emisoraViewModelFactory = application.emisoraViewModelFactory
    val emisoraViewModel = viewModel<EmisoraViewModel>(factory = emisoraViewModelFactory)
    EmisoraVista(
        navController = NavHostController(LocalContext.current), emisoraViewModel = emisoraViewModel
    )
}

