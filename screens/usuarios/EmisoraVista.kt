package com.jcmateus.casanarestereo.screens.usuarios

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jcmateus.casanarestereo.screens.home.Destinos

// Vista del perfil de la emisora
@Composable

fun EmisoraVista(
    navController: NavHostController,
    viewModel: EmisoraViewModel
) {
    val perfilEmisoraState = viewModel.perfilEmisora.observeAsState(PerfilEmisora())
    var perfilEmisora by remember { mutableStateOf(perfilEmisoraState.value) }

    LaunchedEffect(key1 = perfilEmisoraState.value) { // Observar cambios en perfilEmisoraState.value
        perfilEmisora = perfilEmisoraState.value // Actualizar perfilEmisora cuando cambie
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección del perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (perfilEmisora.imagenPerfilUri != null) {
                    AsyncImage(
                        model = perfilEmisora.imagenPerfilUri,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }else {
                    // Mostrar una imagen predeterminada si no hay imagen de perfil
                    Image(
                        painter = painterResource(id = com.jcmateus.casanarestereo.R.drawable.user_pre), // Reemplaza con tu imagen predeterminada
                        contentDescription = "Imagen de perfil predeterminada",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = perfilEmisora.nombre,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = perfilEmisora.ciudad,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = perfilEmisora.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(
                    listOf(
                        "Subir noticia" to "ruta_formulario_noticia",
                        "Subir podcast" to "ruta_formulario_podcast",
                        "Subir programa" to "ruta_formulario_programa",
                        "Subir banner" to "ruta_formulario_banner"
                    )
                ) { (text, route) ->
                    var isHovered by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInteropFilter {
                                isHovered = it.action == MotionEvent.ACTION_HOVER_ENTER
                                true
                            }
                            .clickable { navController.navigate(route) },
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isHovered) 9.dp else 8.dp)
                    ) {
                        Text(text = text, modifier = Modifier.padding(18.dp))
                    }
                }
            }
            // Agrega más cards para podcast, programa, banner, etc.
            // ...

            Spacer(modifier = Modifier.height(17.dp))

            // Sección para editar la información
            Button(onClick = { navController.navigate(Destinos.FormularioPerfilEmisora.ruta) }) {
                Text("Editar perfil")
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun EmisoraVistaPreview() {
    EmisoraVista(
        navController = NavHostController(LocalContext.current),
        viewModel = EmisoraViewModel()
    )
}

