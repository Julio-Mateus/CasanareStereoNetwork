package com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido

import androidx.compose.runtime.Composable
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias.FormularioNoticia
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast.FormularioPodcast

@Composable
fun <T : Contenido> FormularioContenido(
    contenido: T,
    viewModel: ContenidoViewModel,
    onGuardar: (T) -> Unit
) {
    when (contenido) {
        is Contenido.Noticia -> {
            FormularioNoticia(
                onGuardar = { noticia ->
                    onGuardar(noticia as T)
                },
                navController = TODO()
            )
        }
        is Contenido.Podcast -> {
            FormularioPodcast(
                onGuardar = { podcast ->
                    onGuardar(podcast as T)
                },
                navController = TODO()
            )
        }
        is Contenido.Programa -> {
            // Formulario para programas
        }
        is Contenido.Banner -> {
            // Formulario para banners
        }
    }
}