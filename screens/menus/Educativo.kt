package com.jcmateus.casanarestereo.screens.menus

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideosYoutubeView(navController: NavHostController) {
    val videoUrls = listOf(
        "https://youtu.be/yY9ljVUh2uE",
        "https://youtu.be/znbFx_oI2DA",
        "https://youtu.be/C7cs3O42Sy8",
        "https://youtu.be/ZVgEW4s6MN4"
    )
    val videoNames = listOf(
        "Proyecto Casanare 1",
        "video 2 Casanare",
        "video 3",
        "video 4 Garza"
    )
    val userLoggedIn by remember { mutableStateOf(false)} // Variable de estado para el inicio de sesión
    Scaffold(
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
        }) { paddingValues ->
        // Contenido de la pantalla (VideoGrid) con paddingValues
        VideoGrid(
            videoUrls = videoUrls,
            videoNames = videoNames,
            navController = navController,
            modifier = Modifier.padding(paddingValues) // Aplicar padding
        )
    }
}

@Composable
fun VideoGrid(videoUrls: List<String>, videoNames: List<String>, navController: NavHostController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = videoUrls.zip(videoNames),
            key = { (videoUrl, _) -> videoUrl },
            contentType = { (_, _) -> "video" }
        ) {
                (videoUrl, videoName) ->
            val videoId = extractVideoId(videoUrl)
            if (videoId != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 2.dp
                ) {
                    Column {
                        YouTubePlayer(videoId = videoId)
                        Text(videoName)
                    }
                }
            } else {
                Text(text = "Invalid video URL")
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
            .aspectRatio(16f / 9f),
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


