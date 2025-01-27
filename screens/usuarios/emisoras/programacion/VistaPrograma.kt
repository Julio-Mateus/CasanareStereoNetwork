package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaPrograma(navController: NavHostController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid ?: ""
    val programaViewModel: ProgramaViewModel = viewModel(
        factory = (context.applicationContext as HomeApplication).programaViewModelFactory
    )
    val programaUiState by programaViewModel.programaUiState.collectAsState()
    val listaProgramas by programaViewModel.listaProgramas.collectAsState()
    val errorEliminandoPrograma by programaViewModel.errorEliminandoPrograma.collectAsState()

    // Add this line to get the list of programs
    LaunchedEffect(key1 = userId) {
        programaViewModel.obtenerProgramas(userId)
    }

    LaunchedEffect(key1 = programaUiState) {
        if (programaUiState is ProgramaUiState.Error) {
            Toast.makeText(context, "Error al eliminar programa", Toast.LENGTH_SHORT).show()
            programaViewModel.restablecerErrorEliminandoPrograma()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Programas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (programaUiState is ProgramaUiState.Loading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /*items(listaProgramas) { programa ->
                        ProgramaItem(
                            programa = programa,
                            programaViewModel = programaViewModel,
                            userId = userId
                        )
                    }
                     */
                }
            }
        }
    }
}

@Composable
fun ProgramaItem(programa: Contenido.Programa, programaViewModel: ProgramaViewModel, userId: String) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Aquí puedes agregar la lógica para navegar a la pantalla de detalle del programa
                // Por ejemplo: navController.navigate("programaDetalle/${programa.id}")
            }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(64.dp)) {
                AsyncImage(
                    model = programa.imagenUriPrograma,
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = programa.nombrePrograma, fontWeight = FontWeight.Bold)
                Text(text = programa.horarioPrograma)
                Text(text = programa.descripcionPrograma)
            }
            Spacer(modifier = Modifier.weight(0.1f))
            IconButton(
                onClick = {
                    programaViewModel.eliminarPrograma(programa, userId)
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}