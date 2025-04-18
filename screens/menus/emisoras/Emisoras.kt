package com.jcmateus.casanarestereo.screens.menus.emisoras

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
@Composable
fun EmisorasScreen(innerPadding: PaddingValues, navController: NavController) {
    val emisoraViewModel: EmisoraViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
    )
    val emisorasCercanas by emisoraViewModel.emisorasCercanas.collectAsState()
    val isLoading by emisoraViewModel.isLoading.collectAsState()
    var emisoraSeleccionada by remember { mutableStateOf<PerfilEmisora?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (emisoraSeleccionada != null) {
                // Mostrar detalles de la emisora seleccionada
                EmisoraDetalles(
                    emisora = emisoraSeleccionada!!,
                    navController = navController,
                    //onPodcastClick = { /* TODO: Navegar a la pantalla de podcasts */ },
                    //onNoticiasClick = { /* TODO: Navegar a la pantalla de noticias */ },
                    //onProgramasClick = { /* TODO: Navegar a la pantalla de programas */ }
                )
            } else {
                if (emisorasCercanas.isEmpty()) {
                    Text(text = "No hay emisoras cercanas", style = MaterialTheme.typography.bodyLarge)
                } else {
                    EmisorasList(emisoras = emisorasCercanas, navController = navController) { emisora ->
                        emisoraSeleccionada = emisora
                    }
                }
            }
        }
    }
}

@Composable
fun EmisorasList(emisoras: List<PerfilEmisora>, navController: NavController, onEmisoraClick: (PerfilEmisora) -> Unit) {
    LazyColumn {
        items(emisoras) { emisora ->
            EmisoraItem(emisora = emisora, navController = navController, onEmisoraClick = onEmisoraClick)
        }
    }
}
 */
@Composable
fun EmisorasScreen(innerPadding: PaddingValues,navController: NavController ) {
    val emisoraViewModel: EmisoraViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
    )
    val emisorasCercanas by emisoraViewModel.emisorasCercanas.collectAsState()
    val isLoading by emisoraViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Emisoras",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (emisorasCercanas.isEmpty()) {
                Text(
                    text = "No hay emisoras cercanas",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                ImageCarouselEmisoras(
                    images = listOf(
                        R.drawable.carru1,
                        R.drawable.carru2,
                        R.drawable.carru3,
                        R.drawable.emilocal1,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Emisoras Cercanas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                EmisorasCarousel(emisoras = emisorasCercanas, navController = navController)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Emisoras Destacadas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                EmisorasCarousel(emisoras = emisorasCercanas, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarouselEmisoras(images: List<Int>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            scope.launch {
                if (images.isNotEmpty()) {
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % images.size)
                }
            }
        }
    }

    HorizontalPager(
        count = images.size,
        state = pagerState,
        modifier = modifier
    ) { page ->
        Image(
            painter = painterResource(id = images[page]),
            contentDescription = "Imagen del carrusel",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmisorasCarousel(emisoras: List<PerfilEmisora>, navController: NavController) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = emisoras.size,
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        EmisoraItem(emisora = emisoras[page], navController = navController)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* Acción para flecha izquierda */ }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Anterior")
        }

        IconButton(onClick = { /* Acción para flecha derecha */ }) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente")
        }
    }
}

@Composable
fun EmisoraItem(emisora: PerfilEmisora, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                //TODO: Navegar a la pantalla de detalles de la emisora
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
                painter = painterResource(id = R.drawable.emilocal1),
                contentDescription = "Logo de ${emisora.nombre}",
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
                    text = emisora.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = emisora.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
