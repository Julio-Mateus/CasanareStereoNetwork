package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlin.collections.set
import kotlin.text.set
import kotlin.toString

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNoticia(innerPadding: PaddingValues, navController: NavHostController, noticiaId: String? = null) {
    // Obtener la fábrica de ViewModel
    val factory =
        (LocalContext.current.applicationContext as HomeApplication).noticiaViewModelFactory

    // Obtener el ViewModel utilizando la fábrica
    val noticiaViewModel: NoticiaViewModel = viewModel(factory = factory)
    val noticiaGuardada by noticiaViewModel.noticiaGuardada.collectAsState()
    val errorGuardandoNoticia by noticiaViewModel.errorGuardandoNoticia.collectAsState()
    val noticiaUiState by noticiaViewModel.noticiaUiState.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var fuente by remember { mutableStateOf("") }
    var fechaPublicacion by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var enlace by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var etiqueta by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val categorias = listOf("Regional", "Nacional", "Internacional")
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = noticiaId) {
        if (noticiaId != null) {
            noticiaViewModel.obtenerNoticia(noticiaId)
            isEditing = true
        }
    }

    val noticia by noticiaViewModel.noticia.collectAsState()
    LaunchedEffect(key1 = noticia) {
        noticia?.let {
            titulo = it.tituloNoticia
            imagenUri = Uri.parse(it.imagenUriNoticia)
            fuente = it.fuenteNoticia
            fechaPublicacion = it.fechaPublicacionNoticia
            autor = it.autorNoticia
            enlace = it.enlaceNoticia
            contenido = it.contenidoNoticia
            ubicacion = it.ubicacionNoticia
            etiqueta = it.etiquetaNoticia
            categoria = it.categoria
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
        // Call actualizarImagenNoticia when a new image is selected
        if (uri != null) {
            noticiaViewModel.actualizarImagenNoticia(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Noticia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Validación de datos (ejemplo: título no vacío)
                        if (titulo.isBlank()) {
                            // Mostrar un mensaje de error al usuario
                            Toast.makeText(
                                context,
                                "El título de la noticia no puede estar vacío",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

                        // Guardar los cambios
                        val noticia = Contenido.Noticia(
                            titulo,
                            imagenUri?.toString() ?: "",
                            fuente,
                            fechaPublicacion,
                            autor,
                            enlace,
                            contenido,
                            ubicacion,
                            etiqueta,
                            id = noticia?.id ?: "",
                            categoria = categoria

                        )
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                        if (isEditing) {
                            // Call actualizarNoticia when editing
                            noticiaViewModel.actualizarNoticia(noticiaId!!, noticia)
                        } else {
                            // Call guardarNoticia when creating a new noticia
                            noticiaViewModel.guardarNoticia(noticia, userId)
                        }
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (imagenUri != null) {
                AsyncImage(
                    model = imagenUri,
                    contentDescription = "Imagen de la noticia",
                    modifier = Modifier
                        .size(200.dp, 150.dp) // Ajustar las dimensiones según sea necesario
                        .clip(RoundedCornerShape(8.dp)) // Agregar bordes redondeados si lo deseas
                )
            }
            // Campo para la imagen (se abre un selector de archivos)
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Seleccionar imagen")
            }
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fuente,
                onValueChange = { fuente = it },
                label = { Text("Fuente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaPublicacion,
                onValueChange = { fechaPublicacion = it },
                label = { Text("Fecha de publicación") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = enlace,
                onValueChange = { enlace = it },
                label = { Text("Enlace") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contenido,
                onValueChange = { contenido = it },
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = etiqueta,
                onValueChange = { etiqueta = it },
                label = { Text("Etiqueta") },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { selectedCategoria ->
                        DropdownMenuItem(
                            text = { Text(text = selectedCategoria) },
                            onClick = {
                                categoria = selectedCategoria
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(onClick = {
                val noticia = Contenido.Noticia(
                    titulo,
                    imagenUri?.toString() ?: "",
                    fuente,
                    fechaPublicacion,
                    autor,
                    enlace,
                    contenido,
                    ubicacion,
                    etiqueta,
                    id = noticia?.id ?: "",
                    categoria = categoria
                )
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                if (isEditing) {
                    // Call actualizarNoticia when editing
                    noticiaViewModel.actualizarNoticia(noticiaId!!, noticia)
                } else {
                    // Call guardarNoticia when creating a new noticia
                    noticiaViewModel.guardarNoticia(noticia, userId)
                }
            }) {
                Text("Guardar")
            }
        }

        // Utilizar un LaunchedEffect para la navegación
        LaunchedEffect(key1 = noticiaGuardada) {
            if (noticiaGuardada) {
                // La noticia se guardó correctamente
                // Restablecer el estado de noticiaGuardada
                noticiaViewModel.restablecerNoticiaGuardada()

                // Navegar a la vista de detalle
                navController.navigate(Destinos.VistaNoticia.ruta)
            }
        }
        // Handle error state
        LaunchedEffect(key1 = errorGuardandoNoticia) {
            if (errorGuardandoNoticia != null) {
                Toast.makeText(
                    context,
                    "Error al guardar la noticia: $errorGuardandoNoticia", // Usar la variable errorGuardandoNoticia
                    Toast.LENGTH_LONG
                ).show()
                noticiaViewModel.restablecerErrorGuardandoNoticia()
            }
        }
        // Handle UI state changes
        when (noticiaUiState) { // Usar la variable noticiaUiState
            is NoticiaUiState.Loading -> {
                // Show loading indicator
            }

            is NoticiaUiState.Success -> {
                // Handle success state
            }

            is NoticiaUiState.Error -> {
                // Show error message
                Toast.makeText(
                    context,
                    "Error: ${(noticiaUiState as NoticiaUiState.Error).message}", // Usar la variable noticiaUiState
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}

@Composable
@Preview
fun FormularioNoticiaPreview() {
    FormularioNoticia(
        innerPadding = PaddingValues(),
        navController = rememberNavController()
    )
}