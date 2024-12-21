package com.jcmateus.casanarestereo.screens.usuarios

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter

// Vista del perfil de la emisora
@Composable

fun EmisoraVista(
    navController: NavHostController,
    viewModel: EmisoraViewModel
) {
    MaterialTheme {
        EmisoraVista(navController, viewModel)
    }
    val perfilEmisoraState = viewModel.perfilEmisora.observeAsState(PerfilEmisora())
    val perfilEmisora = perfilEmisoraState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Sección para el logo de la emisora
        AsyncImage(
            model = perfilEmisora.logoUrl,
            contentDescription = "Logo de la emisora",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección para la información de la emisora
        Text(
            text = perfilEmisora.nombre,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = perfilEmisora.ciudad, // Mostrar la ciudad
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = perfilEmisora.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección para el enlace de la emisora
        ClickableText(
            text = AnnotatedString(perfilEmisora.enlace),
            onClick = { offset ->
                // Abre el enlace en el navegador web
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección para editar la información
        Button(onClick = { navController.navigate("perfil_emisora") }) {
            Text("Editar perfil")
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun EmisoraVistaPreview() {
    EmisoraVista(navController = NavHostController(LocalContext.current), viewModel = EmisoraViewModel())
}

