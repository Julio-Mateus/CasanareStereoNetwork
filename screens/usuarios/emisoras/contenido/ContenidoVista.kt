package com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlin.reflect.KClass

@Composable
fun SubirContenido(navController: NavHostController) {
    val viewModel: ContenidoViewModel = viewModel()
    var tipoContenido by remember { mutableStateOf<Contenido?>(null) }
    var selectedOption by remember { mutableStateOf(Contenido::class.sealedSubclasses[0]) }

    Column {
        // RadioGroup para seleccionar el tipo de contenido
        RadioGroup(
            selectedOption = selectedOption,
            options = listOf(Contenido.Noticia::class, Contenido.Podcast::class, Contenido.Programa::class, Contenido.Banner::class),
            optionTransform = { it.simpleName!! },
            onOptionSelected = {
                tipoContenido = when (it) {
                    Contenido.Noticia::class -> Contenido.Noticia("", "", "", "", "", "", "", "", "")
                    Contenido.Podcast::class -> Contenido.Podcast("", "", "", "", "", "", "", "", "", "", "")
                    Contenido.Programa::class -> Contenido.Programa("", "", "", "", "", "", "", "", "")
                    Contenido.Banner::class -> Contenido.Banner("", "", "", "", "", "", "", "", "")
                    else -> null
                }
            }
        )

        // Mostrar el formulario correspondiente al tipo de contenido seleccionado
        tipoContenido?.let { contenido ->
            FormularioContenido(contenido, viewModel) { contenidoActualizado ->
                viewModel.guardarContenido(contenidoActualizado)
            }
        }
    }
}

@Composable
fun RadioGroup(
    selectedOption: KClass<out Contenido>,
    options: List<KClass<out Contenido>>,
    optionTransform: (KClass<out Contenido>) -> String,
    onOptionSelected: (KClass<out Contenido>) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Text(optionTransform(option))
            }
        }
    }
}

@Composable
@Preview
fun SubirContenidoPreview() {
    SubirContenido(navController = NavHostController(LocalContext.current))
}

