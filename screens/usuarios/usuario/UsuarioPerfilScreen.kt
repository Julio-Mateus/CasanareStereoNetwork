package com.jcmateus.casanarestereo.screens.usuarios.usuario

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.menus.emisoras.EmisoraItem
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioPerfilScreen(viewModel: UsuarioPerfilViewModel, navController: NavHostController, uid: String) {
    // Estados del ViewModel
    val usuario by viewModel.usuario.collectAsState()
    val estaCargando by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val hayExito by viewModel.isSuccess.collectAsState()

    // Estados locales para los campos del perfil
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var estaEditando by remember { mutableStateOf(false) }
    var frase by remember { mutableStateOf(usuario?.frase ?: "") }
    var profesion by remember { mutableStateOf(usuario?.profesion ?: "") }
    var ciudad by remember { mutableStateOf(usuario?.ciudad ?: "") }
    var departamento by remember { mutableStateOf(usuario?.departamento ?: "") }
    var emisorasFavoritas by remember { mutableStateOf<List<PerfilEmisora>>(emptyList()) }

    // Lanzador para seleccionar imágenes
    val lanzadorDeImagenes = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri
    }
    val navController2 = rememberNavController()

    // Efecto para cargar el usuario al iniciar la pantalla
    LaunchedEffect(key1 = uid) {
        viewModel.getUsuario(uid)
    }

    // Efecto para actualizar los campos cuando el usuario cambia
    LaunchedEffect(key1 = usuario) {
        nombre = usuario?.nombre ?: ""
        frase = usuario?.frase ?: ""
        profesion = usuario?.profesion ?: ""
        ciudad = usuario?.ciudad ?: ""
        departamento = usuario?.departamento ?: ""
        if (usuario != null) {
            viewModel.viewModelScope.launch {
                val emisoras = usuario!!.emisorasFavoritas.mapNotNull { emisoraId ->
                    viewModel.obtenerEmisoraPorId(emisoraId)
                }
                emisorasFavoritas = emisoras
            }
        }
    }

    // Efecto para navegar hacia atrás si hay éxito
    LaunchedEffect(key1 = hayExito) {
        if (hayExito) {
            viewModel.resetIsSuccess()
            navController.popBackStack()
        }
    }

    // Contenido de la pantalla
    Scaffold(
        topBar = { PerfilTopAppBar(navController) }
    ) { innerPadding ->
        PerfilContent(
            estaCargando = estaCargando,
            innerPadding = innerPadding,
            error = error,
            usuario = usuario,
            fotoUri = fotoUri,
            lanzadorDeImagenes = lanzadorDeImagenes,
            nombre = nombre,
            estaEditando = estaEditando,
            onNombreChange = { nombre = it },
            onEditarClick = { estaEditando = !estaEditando },
            frase = frase,
            onFraseChange = { frase = it },
            profesion = profesion,
            onProfesionChange = { profesion = it },
            ciudad = ciudad,
            onCiudadChange = { ciudad = it },
            departamento = departamento,
            onDepartamentoChange = { departamento = it },
            onGuardarClick = {
                viewModel.actualizarPerfilUsuario(nombre, fotoUri, frase, profesion, ciudad, departamento)
            },
            hayExito = hayExito,
            emisorasFavoritas = emisorasFavoritas,
            navController2 = navController2
        )
    }
}

@Composable
fun PerfilContent(
    estaCargando: Boolean,
    innerPadding: PaddingValues,
    error: String?,
    usuario: Usuario?,
    fotoUri: Uri?,
    lanzadorDeImagenes: ManagedActivityResultLauncher<String, Uri?>,
    nombre: String,
    estaEditando: Boolean,
    onNombreChange: (String) -> Unit,
    onEditarClick: () -> Unit,
    frase: String,
    onFraseChange: (String) -> Unit,
    profesion: String,
    onProfesionChange: (String) -> Unit,
    ciudad: String,
    onCiudadChange: (String) -> Unit,
    departamento: String,
    onDepartamentoChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    hayExito: Boolean,
    emisorasFavoritas: List<PerfilEmisora>,
    navController2: NavHostController
) {
    if (estaCargando) {
        CargandoContent(innerPadding)
    } else {
        PerfilListContent(
            innerPadding = innerPadding,
            error = error,
            usuario = usuario,
            fotoUri = fotoUri,
            lanzadorDeImagenes = lanzadorDeImagenes,
            nombre = nombre,
            estaEditando = estaEditando,
            onNombreChange = onNombreChange,
            onEditarClick = onEditarClick,
            frase = frase,
            onFraseChange = onFraseChange,
            profesion = profesion,
            onProfesionChange = onProfesionChange,
            ciudad = ciudad,
            onCiudadChange = onCiudadChange,
            departamento = departamento,
            onDepartamentoChange = onDepartamentoChange,
            onGuardarClick = onGuardarClick,
            hayExito = hayExito,
            emisorasFavoritas = emisorasFavoritas,
            navController2 = navController2
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilTopAppBar(navController: NavHostController) {
    TopAppBar(
        title = { Text("Perfil de Usuario") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, "Atrás")
            }
        }
    )
}
@Composable
fun CargandoContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
@Composable
fun PerfilListContent(
    innerPadding: PaddingValues,
    error: String?,
    usuario: Usuario?,
    fotoUri: Uri?,
    lanzadorDeImagenes: ManagedActivityResultLauncher<String, Uri?>,
    nombre: String,
    estaEditando: Boolean,
    onNombreChange: (String) -> Unit,
    onEditarClick: () -> Unit,
    frase: String,
    onFraseChange: (String) -> Unit,
    profesion: String,
    onProfesionChange: (String) -> Unit,
    ciudad: String,
    onCiudadChange: (String) -> Unit,
    departamento: String,
    onDepartamentoChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    hayExito: Boolean,
    emisorasFavoritas: List<PerfilEmisora>,
    navController2: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),

    ) {
        item {
            ImagenDePerfil(
                fotoUri = fotoUri,
                avatarUrl = usuario?.avatarUrl,
                alHacerClickEnImagen = { lanzadorDeImagenes.launch("image/*") }
            )
        }

        item {
            if (error != null) {
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            }
        }

        item {
            NombreDePerfil(
                nombre = nombre,
                estaEditando = estaEditando,
                alCambiarNombre = onNombreChange,
                alHacerClickEnEditar = onEditarClick
            )
        }
        item {
            CampoDeTextoDePerfil(
                valor = frase,
                alCambiarValor = onFraseChange,
                etiqueta = "Frase",
                estaEditando = estaEditando
            )
        }
        item {
            CampoDeTextoDePerfil(
                valor = profesion,
                alCambiarValor = onProfesionChange,
                etiqueta = "Profesión",
                estaEditando = estaEditando
            )
        }
        item {
            CampoDeTextoDePerfil(
                valor = ciudad,
                alCambiarValor = onCiudadChange,
                etiqueta = "Ciudad",
                estaEditando = estaEditando
            )
        }
        item {
            CampoDeTextoDePerfil(
                valor = departamento,
                alCambiarValor = onDepartamentoChange,
                etiqueta = "Departamento",
                estaEditando = estaEditando
            )
        }

        item {
            if (estaEditando) {
                Button(onClick = onGuardarClick) {
                    Text("Guardar")
                }
            }
        }

        if (hayExito) {
            item {
                Text(text = "Perfil actualizado con éxito", color = MaterialTheme.colorScheme.primary)
            }
        }

        item {
            Text(text = "Emisoras Favoritas", fontWeight = FontWeight.Bold)
        }
        // Usar la lista de emisoras favoritas que ya hemos cargado
        items(emisorasFavoritas) { emisora ->
            EmisoraItem(emisora = emisora, navController = navController2, onEmisoraClick = {})
        }
    }
}
@Composable
fun ImagenDePerfil(fotoUri: Uri?, avatarUrl: String?, alHacerClickEnImagen: () -> Unit) {
    Image(
        painter = if (fotoUri != null) {
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = fotoUri)
                    .build()
            )
        } else if (avatarUrl != null) {
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = avatarUrl)
                    .build()
            )
        } else {
            painterResource(R.drawable.user_pre)
        },
        contentDescription = "Foto de perfil",
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .clickable(onClick = alHacerClickEnImagen),
        contentScale = ContentScale.Crop
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NombreDePerfil(
    nombre: String,
    estaEditando: Boolean,
    alCambiarNombre: (String) -> Unit,
    alHacerClickEnEditar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (estaEditando) {
            OutlinedTextField(
                value = nombre,
                onValueChange = alCambiarNombre,
                label = { Text("Nombre") },
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(text = nombre, style = MaterialTheme.typography.headlineSmall)
        }
        IconButton(onClick = alHacerClickEnEditar) {
            Icon(Icons.Filled.Edit, "Editar Nombre")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDeTextoDePerfil(
    valor: String,
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    estaEditando: Boolean
) {
    if (estaEditando) {
        OutlinedTextField(
            value = valor,
            onValueChange = alCambiarValor,
            label = { Text(etiqueta) },
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Text(text = "$etiqueta: $valor")
    }
}
