package com.jcmateus.casanarestereo.screens.usuarios.emisoras

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.usuarios.usuario.MyLocationManager


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPerfilEmisora(
    navController: NavHostController,
) {
    val emisoraViewModel: EmisoraViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as HomeApplication).emisoraViewModelFactory
    )
    val context = LocalContext.current
    val perfilEmisora by emisoraViewModel.perfilEmisora.collectAsState()

    var nombreEmisora by remember { mutableStateOf("") } // Inicializar con valor vacío
    var descripcionEmisora by remember { mutableStateOf("") } // Inicializar con valor vacío
    var enlaceEmisora by remember { mutableStateOf("") } // Inicializar con valor vacío
    var paginaWebEmisora by remember { mutableStateOf("") } // Inicializar con valor vacío
    var ciudadEmisora by remember { mutableStateOf("") } // Inicializar con valor vacío
    var departamento by remember { mutableStateOf("") } // Inicializar con valor vacío
    var frecuencia by remember { mutableStateOf("") } // Inicializar con valor vacío
    var imagenPerfilUri by remember { mutableStateOf<Uri?>(null) }
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }
    val myLocationManager = MyLocationManager(context)
    LaunchedEffect(key1 = true) {
        val location = myLocationManager.getLastKnownLocation()
        if (location != null) {
            latitud = location.latitude
            longitud = location.longitude
        }
    }

    LaunchedEffect(key1 = perfilEmisora) {
        nombreEmisora = perfilEmisora.nombre
        descripcionEmisora = perfilEmisora.descripcion
        enlaceEmisora = perfilEmisora.enlace
        paginaWebEmisora = perfilEmisora.paginaWeb
        ciudadEmisora = perfilEmisora.ciudad
        departamento = perfilEmisora.departamento
        frecuencia = perfilEmisora.frecuencia
        imagenPerfilUri = if (perfilEmisora.imagenPerfilUri.isNotBlank()) {
            Uri.parse(perfilEmisora.imagenPerfilUri)
        } else {
            Uri.parse("android.resource://${context.packageName}/${R.drawable.user_pre}")
        }
        // Actualizar latitud y longitud si el perfil ya existe
        latitud = perfilEmisora.latitud
        longitud = perfilEmisora.longitud
    }

    // Launcher para la selección de imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Manejar la Uri de la imagen seleccionada
        if (uri != null) {
            // Actualizar la imagen de perfil en el ViewModel
            emisoraViewModel.actualizarImagenPerfil(uri) // Asegúrate de que esta función esté definida en tu ViewModel
            imagenPerfilUri = uri // Actualizar la variable de estado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de la emisora") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {

                        // Validación de datos (ejemplo: nombre no vacío)
                        if (nombreEmisora.isBlank()) {
                            // Mostrar un mensaje de error al usuario
                            Toast.makeText(
                                context,
                                "El nombre de la emisora no puede estar vacío",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

                        // Guardar los cambios en Firestore
                        // Guardar los cambios utilizando la función actualizarPerfil del ViewModel
                        emisoraViewModel.actualizarPerfil(
                            PerfilEmisora(
                                nombre = nombreEmisora, // Asignar nombreEmisora al campo nombre
                                descripcion = descripcionEmisora, // Asignar descripcionEmisora al campo descripcion
                                imagenPerfilUri = imagenPerfilUri?.toString()
                                    ?: "", // Asignar imagenPerfilUri al campo imagenPerfilUri
                                enlace = enlaceEmisora,
                                paginaWeb = paginaWebEmisora,
                                ciudad = ciudadEmisora,
                                departamento = departamento,
                                frecuencia = frecuencia,
                                latitud = latitud, // Pasar la latitud
                                longitud = longitud // Pasar la longitud
                            ),
                            navController
                        )
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección para la imagen de perfil
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                // Manejar la Uri de la imagen seleccionada
                if (uri != null) {
                    // Actualizar la imagen de perfil en el ViewModel
                    emisoraViewModel.actualizarImagenPerfil(uri) // Asegúrate de que esta función esté definida en tu ViewModel
                    imagenPerfilUri = uri // Actualizar la variable de estado
                }
            }
            Box(modifier = Modifier.size(128.dp)) {
                if (imagenPerfilUri != null) {
                    AsyncImage(
                        // O usa Coil si lo prefieres
                        model = imagenPerfilUri, // Usa la variable de estado imagenPerfilUri
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        // Puedes agregar un error y un placeholder si lo deseas
                    )
                } else {
                    // Mostrar una imagen predeterminada si no hay imagen seleccionada
                    Image(
                        painter = painterResource(id = R.drawable.user_pre), // Reemplaza con tu imagen predeterminada
                        contentDescription = "Imagen de perfil predeterminada",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                }
                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(48.dp)
                        .background(Color.LightGray, CircleShape)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar imagen")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Campos de entrada para la información de la emisora
            OutlinedTextField(
                value = nombreEmisora,
                onValueChange = { nombreEmisora = it },
                label = { Text("Nombre de la emisora") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next // Flecha de "siguiente"
                )
            )
            OutlinedTextField(
                value = descripcionEmisora,
                onValueChange = { descripcionEmisora = it },
                label = { Text("Descripción de la emisora") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next // Flecha de "siguiente"
                )
            )
            OutlinedTextField(
                value = paginaWebEmisora,
                onValueChange = { paginaWebEmisora = it },
                label = { Text("Enlace de la emisora (URL)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next // Flecha de "siguiente"
                )
            )
            OutlinedTextField(
                value = enlaceEmisora,
                onValueChange = { enlaceEmisora = it },
                label = { Text("Enlace de la trasmision vivo") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next // Flecha de "siguiente"
                )
            )
            OutlinedTextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Departamento") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next // Flecha de "siguiente"
                )
            )
            OutlinedTextField(
                value = ciudadEmisora,
                onValueChange = { ciudadEmisora = it },
                label = { Text("Ciudad de la emisora") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next // Flecha de "siguiente"
                )
            )
            OutlinedTextField(
                value = frecuencia,
                onValueChange = { frecuencia = it },
                label = { Text("Frecuencia", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,

                    ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done // Botón de "listo"
                )
            )
            // ... otros campos de entrada ...

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar los cambios
            Button(
                onClick = {
                    emisoraViewModel.actualizarPerfil(
                        PerfilEmisora(
                            nombre = nombreEmisora, // Asignar nombreEmisora al campo nombre
                            descripcion = descripcionEmisora, // Asignar descripcionEmisora al campo descripcion
                            imagenPerfilUri = imagenPerfilUri?.toString()
                                ?: "", // Asignar imagenPerfilUri al campo imagenPerfilUri
                            enlace = enlaceEmisora,
                            paginaWeb = paginaWebEmisora,
                            ciudad = ciudadEmisora,
                            departamento = departamento,
                            frecuencia = frecuencia,
                            latitud = latitud, // Pasar la latitud
                            longitud = longitud // Pasar la longitud
                        ),
                        navController
                    )
                }, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun PerfilEmisoraPreview() {
    val firebaseAuth = FirebaseAuth.getInstance() // Crear instancia de FirebaseAuth
    FormularioPerfilEmisora(navController = NavHostController(LocalContext.current))
}
