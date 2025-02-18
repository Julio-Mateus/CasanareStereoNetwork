package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jcmateus.casanarestereo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Noticias_Regionales(innerPadding: PaddingValues, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Noticias Regionales",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ImageCarousel2(
            images = listOf(
                R.drawable.carru1,
                R.drawable.carru2,
                R.drawable.carru3,
                R.drawable.emilocal1,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Últimas Noticias",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        NoticiasCarousel(
            noticias = listOf(
                R.drawable.carru1,
                R.drawable.carru2,
                R.drawable.carru3,
                R.drawable.emilocal1
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Noticias Destacadas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        NoticiasCarousel(
            noticias = listOf(
                R.drawable.carru1,
                R.drawable.carru2,
                R.drawable.carru3,
                R.drawable.emilocal1
            )
        )
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel3(images: List<Int>, modifier: Modifier = Modifier) {
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
fun NoticiasCarousel2(noticias: List<Int>) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    HorizontalPager(
        count = noticias.size,
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        Image(
            painter = painterResource(id = noticias[page]),
            contentDescription = "Noticia",
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            scope.launch {
                pagerState.animateScrollToPage(
                    (pagerState.currentPage - 1).coerceAtLeast(0)
                )
            }
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Anterior")
        }

        IconButton(onClick = {
            scope.launch {
                pagerState.animateScrollToPage(
                    (pagerState.currentPage + 1).coerceAtMost(noticias.size - 1)
                )
            }
        }) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente")

        }
    }
}