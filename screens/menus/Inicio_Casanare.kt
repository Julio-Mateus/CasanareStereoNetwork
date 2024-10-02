package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jcmateus.casanarestereo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Inicio() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ImageCarousel( images = listOf(
            R.drawable.carru1 ,
            R.drawable.carru2,
            R.drawable.carru3,
            R.drawable.carru2,
            R.drawable.carru1,
            R.drawable.carru3
        )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Acción del botón */ }) {
            Text("Yopal")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Emisoras Locales", fontWeight = FontWeight.Bold)

        EmisorasCarousel(
            emisoras = listOf(
            R.drawable.emilocal1,
                R.drawable.emilocal2,
            R.drawable.emilocal3,
            R.drawable.emilocal1,
            R.drawable.emilocal3,
            R.drawable.emilocal2
        ))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Emisoras Nacionales", fontWeight = FontWeight.Bold)

        EmisorasCarousel(emisoras = listOf(
            R.drawable.eminacio1,
            R.drawable.eminacio2,
            R.drawable.eminacio3,
            R.drawable.eminacio1,
            R.drawable.eminacio3,
            R.drawable.eminacio2
        ))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Todas las Emisoras", fontWeight = FontWeight.Bold)

        var searchText by remember { mutableStateOf("") }
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar emisora") }
        )

        // Aquí puedes mostrar la lista de emisoras filtradas según searchText
    }

}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<Int>, modifier: Modifier = Modifier) {
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
            modifier = Modifier.fillMaxSize(), // Ajusta la imagen para que ocupe todo el espacio disponible
            contentScale = ContentScale.Crop // Recorta la imagen para que se ajuste al espacio
        )
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmisorasCarousel(emisoras: List<Int>) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = emisoras.size,
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        Image(
            painter = painterResource(id = emisoras[page]), // Use painterResource for images
            contentDescription = "Emisora",
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp) // Adjust size as needed
        )
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