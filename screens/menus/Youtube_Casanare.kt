package com.jcmateus.casanarestereo.screens.menus

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jcmateus.casanarestereo.R

@Composable
fun Youtube_Casanare(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Youtube Casanare",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(getYoutubeVideos()) { video ->
                YoutubeVideoCard(video = video)
            }
        }
    }
}

@Composable
fun YoutubeVideoCard(video: YoutubeVideo) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = video.imagen),
                contentDescription = "Miniatura del video ${video.titulo}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = video.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = video.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

// Data class para representar un video de YouTube
data class YoutubeVideo(
    val titulo: String,
    val descripcion: String,
    val url: String,
    val imagen: Int
)

// Función para obtener una lista de videos de YouTube (reemplaza con tu lógica real)
fun getYoutubeVideos(): List<YoutubeVideo> {
    return listOf(
        YoutubeVideo(
            "Video 1",
            "Descripción del video 1.",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            R.drawable.carru1
        ),
        YoutubeVideo(
            "Video 2",
            "Descripción del video 2.",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            R.drawable.carru2
        ),
        YoutubeVideo(
            "Video 3",
            "Descripción del video 3.",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            R.drawable.carru3
        ),
        YoutubeVideo(
            "Video 4",
            "Descripción del video 4.",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            R.drawable.carru1
        ),
        YoutubeVideo(
            "Video 5",
            "Descripción del video 5.",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            R.drawable.carru2
        )
    )
}