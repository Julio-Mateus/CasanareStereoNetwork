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
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
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
fun FormularioNoticia(
    innerPadding: PaddingValues,
    navController: NavHostController,
    noticiaId: String? = null
) {
    // Obtener la fábrica de ViewModel
    val factory =
        (LocalContext.current.applicationContext as HomeApplication).noticiaViewModelFactory

    // Obtener el ViewModel utilizando la fábrica
    val noticiaViewModel: NoticiaViewModel = viewModel(factory = factory)
    val errorGuardandoNoticia by noticiaViewModel.errorGuardandoNoticia.collectAsState()
    val noticiaUiState by noticiaViewModel.noticiaUiState.collectAsState()
    val noticiaCargada by noticiaViewModel.noticiaCargada.collectAsState()


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
    val categorias = listOf("Regional", "Nacional", "Internacional")
    var expanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val userId = currentUser?.uid ?: ""

    LaunchedEffect(key1 = userId) {
        noticiaViewModel.obtenerNoticias(userId)
    }
    LaunchedEffect(key1 = noticiaId) {
        if (noticiaId != null) {
            noticiaViewModel.obtenerNoticia(noticiaId, userId) // Pasamos userId
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
                            Toast.makeText(context, "El título es obligatorio", Toast.LENGTH_SHORT)
                                .show()
                            return@IconButton
                        }
                        if (fuente.isBlank()) {
                            Toast.makeText(context, "La fuente es obligatoria", Toast.LENGTH_SHORT)
                                .show()
                            return@IconButton
                        }
                        if (fechaPublicacion.isBlank()) {
                            Toast.makeText(
                                context,
                                "La fecha de publicación es obligatoria",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (autor.isBlank()) {
                            Toast.makeText(context, "El autor es obligatorio", Toast.LENGTH_SHORT)
                                .show()
                            return@IconButton
                        }
                        if (enlace.isBlank()) {
                            Toast.makeText(context, "El enlace es obligatorio", Toast.LENGTH_SHORT)
                                .show()
                            return@IconButton
                        }
                        if (contenido.isBlank()) {
                            Toast.makeText(
                                context,
                                "El contenido es obligatorio",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (ubicacion.isBlank()) {
                            Toast.makeText(
                                context,
                                "La ubicación es obligatoria",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (etiqueta.isBlank()) {
                            Toast.makeText(
                                context,
                                "La etiqueta es obligatoria",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (categoria.isBlank()) {
                            Toast.makeText(
                                context,
                                "La categoría es obligatoria",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (imagenUri == null) {
                            Toast.makeText(context, "La imagen es obligatoria", Toast.LENGTH_SHORT)
                                .show()
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

                        if (isEditing) {
                            // Call actualizarNoticia when editing
                            noticiaViewModel.actualizarNoticia(noticiaId!!, noticia, userId)
                        } else {
                            // Call guardarNoticia when creating a new noticia
                            noticiaViewModel.guardarNoticia(noticia)
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
                if (isEditing) {
                    // Call actualizarNoticia when editing
                    noticiaViewModel.actualizarNoticia(noticiaId!!, noticia, userId)
                } else {
                    // Call guardarNoticia when creating a new noticia
                    noticiaViewModel.guardarNoticia(noticia)
                }
            }) {
                Text("Guardar")
            }


        }
        LaunchedEffect(key1 = noticiaUiState) {
            Log.d(
                "FormularioNoticia",
                "LaunchedEffect(noticiaUiState): noticiaUiState = $noticiaUiState"
            )
            when (val state = noticiaUiState) {
                is NoticiaUiState.Success -> {
                    Log.d(
                        "FormularioNoticia",
                        "LaunchedEffect(noticiaUiState): Noticia guardada correctamente"
                    )
                    Toast.makeText(
                        context,
                        if (isEditing) "Noticia actualizada correctamente" else "Noticia guardada correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is NoticiaUiState.Error -> {
                    Log.d(
                        "FormularioNoticia",
                        "LaunchedEffect(noticiaUiState): Error al guardar la noticia: ${state.message}"
                    )
                    Toast.makeText(
                        context,
                        "Error al guardar la noticia: ${state.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    noticiaViewModel.restablecerErrorGuardandoNoticia() // Restablecer el estado
                }

                is NoticiaUiState.Loading -> {
                    Log.d("FormularioNoticia", "LaunchedEffect(noticiaUiState): Cargando...")
                    // Puedes agregar un indicador de carga aquí si lo deseas
                }
            }
        }
        LaunchedEffect(key1 = noticiaCargada) {
            Log.d(
                "FormularioNoticia",
                "LaunchedEffect(noticiaCargada): noticiaCargada = $noticiaCargada"
            )
            noticiaCargada?.let {
                Log.d(
                    "FormularioNoticia",
                    "LaunchedEffect(noticiaCargada): Navegando a VistaNoticia"
                )
                val noticiaJson = Gson().toJson(it)
                navController.navigate(Destinos.VistaNoticia(noticiaJson).ruta + "?noticiaJson=$noticiaJson")
                noticiaViewModel.restablecerNoticiaGuardada() // Restablecer el estado
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