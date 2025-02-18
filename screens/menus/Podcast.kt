package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jcmateus.casanarestereo.R

@Composable
fun Podcast(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Podcasts",
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
            items(getPodcasts()) { podcast ->
                PodcastCard(podcast = podcast)
            }
        }
    }
}

@Composable
fun PodcastCard(podcast: Podcast) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
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
                painter = painterResource(id = podcast.imagen),
                contentDescription = "Imagen del podcast ${podcast.titulo}",
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
                    text = podcast.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = podcast.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Data class para representar un podcast
data class Podcast(
    val titulo: String,
    val descripcion: String,
    val imagen: Int
)

// Función para obtener una lista de podcasts (reemplaza con tu lógica real)
fun getPodcasts(): List<Podcast> {
    return listOf(
        Podcast(
            "Podcast 1",
            "Descripción del podcast 1. Esta es una descripción más larga para mostrar cómo se expande la tarjeta. Aquí se puede agregar más información sobre el podcast.",
            R.drawable.carru1
        ),
        Podcast(
            "Podcast 2",
            "Descripción del podcast 2. Esta es una descripción más larga para mostrar cómo se expande la tarjeta. Aquí se puede agregar más información sobre el podcast.",
            R.drawable.carru2
        ),
        Podcast(
            "Podcast 3",
            "Descripción del podcast 3. Esta es una descripción más larga para mostrar cómo se expande la tarjeta. Aquí se puede agregar más información sobre el podcast.",
            R.drawable.carru3
        ),
        Podcast(
            "Podcast 4",
            "Descripción del podcast 4. Esta es una descripción más larga para mostrar cómo se expande la tarjeta. Aquí se puede agregar más información sobre el podcast.",
            R.drawable.carru1
        ),
        Podcast(
            "Podcast 5",
            "Descripción del podcast 5. Esta es una descripción más larga para mostrar cómo se expande la tarjeta. Aquí se puede agregar más información sobre el podcast.",
            R.drawable.carru2
        )
    )
}