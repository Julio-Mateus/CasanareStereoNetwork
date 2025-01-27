package com.jcmateus.casanarestereo.screens.usuarios.usuario

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jcmateus.casanarestereo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioPerfilScreen(viewModel: UsuarioPerfilViewModel, navController: NavHostController) {
    val usuario by viewModel.usuario.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri
    }

    LaunchedEffect(key1 = usuario) {
        nombre = usuario?.nombre ?: ""
    }
    LaunchedEffect(key1 = isSuccess) {
        if (isSuccess) {
            viewModel.resetIsSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (error != null) {
                Text(text = "Error: $error")
            } else {
                Image(
                    painter = if (fotoUri != null) {
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = fotoUri)
                                .build()
                        )
                    } else if (usuario?.avatarUrl != null) {
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = usuario!!.avatarUrl)
                                .build()
                        )
                    } else {
                        rememberAsyncImagePainter(R.drawable.user_pre)
                    },
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    viewModel.actualizarPerfilUsuario(nombre, fotoUri)
                }) {
                    Text("Guardar")
                }
                if (isSuccess) {
                    Text(text = "Perfil actualizado con éxito")
                }
            }
        }
    }
}