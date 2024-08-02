package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView



@Composable
fun VideosYoutubeView(navController: NavHostController) {
    val videoUrls = listOf(
        "https://www.youtube.com/watch?v=F1sHuSBUECs&list=RD0nKjn2FORAo&index=3",
        "https://www.youtube.com/watch?v=1J7n1pTzRwk&list=RD0nKjn2FORAo&index=2"
        // ... más URLs de videos
    )
    VideoGrid(videoUrls = videoUrls, navController = navController)
}
@Composable
fun VideoGrid(videoUrls: List<String>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(videoUrls) { videoUrl ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 2.dp
            ) {
                VideoPlayer(videoUrl = videoUrl, navController = navController)
            }
        }
    }
}

@Composable
fun VideoPlayer(videoUrl: String, navController: NavHostController) {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    DisposableEffect(
        AndroidView(
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                }
            }, modifier = Modifier.fillMaxSize()
        )
    ) {
        onDispose { exoPlayer.release() }
    }
}


