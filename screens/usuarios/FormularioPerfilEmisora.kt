package com.jcmateus.casanarestereo.screens.usuarios

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.home.Destinos


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
    val perfilEmisora by emisoraViewModel.perfilEmisora.observeAsState(PerfilEmisora())

    var nombreEmisora by remember { mutableStateOf(perfilEmisora.nombre) }
    var descripcionEmisora by remember { mutableStateOf(perfilEmisora.descripcion) }
    var enlaceEmisora by remember { mutableStateOf(perfilEmisora.enlace) }
    var ciudadEmisora by remember { mutableStateOf(perfilEmisora.ciudad) }
    var imagenPerfilUri by remember { mutableStateOf<Uri?>(null) }

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
                            Toast.makeText(context, "El nombre de la emisora no puede estar vacío", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        // Guardar los cambios en Firestore
                        val db = FirebaseFirestore.getInstance()
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            val emisoraData = hashMapOf(
                                "imagenPerfilUri" to imagenPerfilUri?.toString(),
                                "nombre" to nombreEmisora,
                                "descripcion" to descripcionEmisora,
                                "enlace" to enlaceEmisora,
                                "cuidad" to ciudadEmisora
                            )
                            db.collection("emisoras").document(user.uid)
                                .set(emisoraData)
                                .addOnSuccessListener {
                                    // Los datos se guardaron correctamente
                                    navController.popBackStack() // Regresar a la pantalla anterior
                                }
                                .addOnFailureListener { e ->
                                    // Error al guardar los datos
                                    // Manejar el error aquí
                                    Toast.makeText(context, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                                    Log.w("FormularioPerfilEmisora", "Error al guardar el perfil de la emisora", e)
                                }
                        }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = descripcionEmisora,
                onValueChange = { descripcionEmisora = it },
                label = { Text("Descripción de la emisora") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = enlaceEmisora,
                onValueChange = { enlaceEmisora = it },
                label = { Text("Enlace de la emisora (URL)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            OutlinedTextField(
                value = ciudadEmisora,
                onValueChange = { ciudadEmisora = it },
                label = { Text("Ciudad de la emisora") },
                modifier = Modifier.fillMaxWidth()
            )
            // ... otros campos de entrada ...

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar los cambios
            Button(onClick = {
                emisoraViewModel.actualizarPerfil(
                    PerfilEmisora(
                        nombreEmisora,
                        descripcionEmisora,
                        enlaceEmisora,
                        imagenPerfilUri.toString(),
                        ciudadEmisora
                    ),
                    navController
                )
            }) {
                Text("Guardar")
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
