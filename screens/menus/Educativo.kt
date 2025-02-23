package com.jcmateus.casanarestereo.screens.menus

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

// Clase para almacenar los datos de los videos
data class VideoData(
    val url: String,
    val name: String,
    val description: String,
    val activityLink: String // Enlace a la actividad
)

@Composable
fun VideosYoutubeView(navController: NavHostController, innerPadding: PaddingValues) {
    val videos = listOf(
        VideoData(
            "https://youtu.be/yY9ljVUh2uE",
            "CIENCIA, TECNOLOGÍA E INNOVACIÓN",
            "En este primer video encontrarás definiciones y conceptos básicos sobre ciencia, tecnología e innovación, que te permitirán crear contenidos entorno a estos temas.",
            "https://docs.google.com/forms/d/e/1FAIpQLScdcG4hU5R6XJubt6QaHxGyqd9X-DtzF2nNhzfM6B_UwRbrWA/viewform?usp=sharing"
        ),
        VideoData(
            "https://youtu.be/znbFx_oI2DA",
            "CT+I EN EL TERRITORIO",
            "En el segundo video busca despertar el interés sobre los tipos de tecnología, cómo interactúan en la vida del hombre y cómo identificarlas en tu territorio.",
            "https://docs.google.com/forms/d/e/1FAIpQLSdyzN0CNohPQbTlTarf66vJ2fEp1v07abqZUWaJd24mrI4glw/viewform?usp=sharing"
        ),
        VideoData(
            "https://youtu.be/C7cs3O42Sy8",
            "COMUNICACIÓN",
            "Este módulo aprenderás sobre las diferentes forma de comunicar.",
            "https://docs.google.com/forms/d/e/1FAIpQLSdbE4ye-U6RVukc_oRpzbBVyIaabkJuPv5e8UyEntqEDVD1lA/viewform?usp=sharing"
        ),
        VideoData(
            "https://youtu.be/ZVgEW4s6MN4",
            "MECANISMOS PARA LA COMUNICACIÓN - ¿CÓMO CONTAR EL CUENTO?",
            "Este último módulo te orientará sobre el “cómo” comunicar y las diferentes herramientas para construir contenidos de ciencia, tecnología e innovación. Particularmente, te enseñará que la ciencia no es aburrida y existen muchas formas de contarla.",
            "https://docs.google.com/forms/d/e/1FAIpQLSezhWuqmezlTTvXorp40KfJ02x3i2BxJP6TchB4Vihldlq1Vg/viewform?usp=sharing"
        )
    )
    val userLoggedIn by remember { mutableStateOf(false) } // Variable de estado para el inicio de sesión
    Scaffold(
        /*
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (userLoggedIn) {
                    navController.navigate(Destinos.HomeCasanareVista.ruta) // Navegar a home si el usuario ha iniciado sesión
                } else {
                    navController.navigate(Destinos.CasanareLoginScreen.ruta) // Navegar a la creación de cuenta si no
                }
            }) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "Account")
            }
        }
                */
    ) { paddingValues ->
        // Contenido de la pantalla (VideoGrid) con paddingValues
        VideoGrid(
            videos = videos,
            navController = navController,
            modifier = Modifier.padding(paddingValues) // Aplicar padding
        )
    }
}

@Composable
fun VideoGrid(videos: List<VideoData>, navController: NavHostController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Introducción
            IntroductionCard()
        }
        items(
            items = videos,
            key = { video -> video.url },
            contentType = { _ -> "video" }
        ) { video ->
            val videoId = extractVideoId(video.url)
            if (videoId != null) {
                VideoItem(video = video, videoId = videoId)
            } else {
                Text(text = "Invalid video URL")
            }
        }
        item {
            // Sección de subir contenido
            //UploadSection()
        }
    }
}
@Composable
fun UploadSection() {
    val context = LocalContext.current
    var institution by remember { mutableStateOf("") }
    var studentNames by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var pieceTitle by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Subir Pieza Divulgativa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor, sube tu pieza divulgativa y completa la siguiente información:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campos de información
            OutlinedTextField(
                value = institution,
                onValueChange = { institution = it },
                label = { Text("Institución") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = studentNames,
                onValueChange = { studentNames = it },
                label = { Text("Nombre(s) estudiante(s)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = course,
                onValueChange = { course = it },
                label = { Text("Curso(s)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pieceTitle,
                onValueChange = { pieceTitle = it },
                label = { Text("Título de la pieza") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Enlaces para subir archivos
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSezhWuqmezlTTvXorp40KfJ02x3i2BxJP6TchB4Vihldlq1Vg/viewform?usp=sharing")) // Reemplaza con el enlace correcto
                    ContextCompat.startActivity(context, intent, null)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = "Subir Pieza Divulgativa")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSezhWuqmezlTTvXorp40KfJ02x3i2BxJP6TchB4Vihldlq1Vg/viewform?usp=sharing")) // Reemplaza con el enlace correcto
                    ContextCompat.startActivity(context, intent, null)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = "Subir Video")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSezhWuqmezlTTvXorp40KfJ02x3i2BxJP6TchB4Vihldlq1Vg/viewform?usp=sharing")) // Reemplaza con el enlace correcto
                    ContextCompat.startActivity(context, intent, null)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = "Subir PDF o Imagen")
            }
        }
    }
}
@Composable
fun IntroductionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Casanare es científico, ¿y tú?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Casanare es científico, ¿y tú?, es un programa de formación virtual diseñado para que los jóvenes casanereños identifiquen las diferentes iniciativas que hay en su entorno sobre Ciencia, Tecnología e Innovación. Busca desarrollar competencias y habilidades en comunicación que incentiven el espíritu investigativo y los motive a que en un futuro se inclinen por estudiar, trabajar y divulgar en estas áreas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nota: Como resultado de todo el curso, realizarás una pieza divulgativa, según indicaciones del docente.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Composable
fun VideoItem(video: VideoData, videoId: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            YouTubePlayer(videoId = videoId)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = video.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.activityLink))
                    ContextCompat.startActivity(context, intent, null)
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = "Actividades ${video.name}")
            }
        }
    }
}
@Composable
fun YouTubePlayer(videoId: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var playerState by remember { mutableStateOf(PlayerState.LOADING) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val youTubePlayerView = remember { YouTubePlayerView(context) }

    AndroidView(
        factory = { youTubePlayerView },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f), // Aumentar el tamaño del reproductor
        update = { view ->
            view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    view.release()
                }
            })

            view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                    playerState = PlayerState.READY
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                    playerState = PlayerState.ERROR
                    errorMessage = when (error) {
                        PlayerConstants.PlayerError.INVALID_PARAMETER_IN_REQUEST -> "Parámetro inválido en la solicitud"
                        PlayerConstants.PlayerError.VIDEO_NOT_FOUND -> "Video no encontrado"
                        else -> "Error desconocido"
                    }
                }
            })
        }
    )

    when (playerState) {
        PlayerState.LOADING -> CircularProgressIndicator()
        PlayerState.ERROR -> errorMessage?.let { Text("Error: $it") }
        else -> {}
    }
}
enum class PlayerState { LOADING, READY, ERROR }

// Función para extraer el ID del video de YouTube
fun extractVideoId(youtubeUrl: String): String? {
    val regex = "(?<=v=|youtu.be/|embed/)[^#&?]*".toRegex()
    return regex.find(youtubeUrl)?.value
}


