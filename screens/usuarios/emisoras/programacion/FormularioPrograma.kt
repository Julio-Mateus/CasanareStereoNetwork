package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.setSelection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.text.format
import kotlin.times

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPrograma(innerPadding: PaddingValues, navController: NavHostController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid ?: ""
    val programaViewModel: ProgramaViewModel = viewModel(
        factory = (context.applicationContext as HomeApplication).programaViewModelFactory
    )
    val programaUiState by programaViewModel.programaUiState.collectAsState()
    val programaGuardado by programaViewModel.programaGuardado.collectAsState()
    val errorGuardandoPrograma by programaViewModel.errorGuardandoPrograma.collectAsState()
    val imagenProgramaUri by programaViewModel.imagenProgramaUri.collectAsState()

    // Estado para el selector de fecha
    var selectedDate by remember { mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault())) }

    // Diálogo del selector de fecha
    val datePicker = remember {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona una fecha")
            .setSelection((selectedDate.toEpochDays() * 24 * 60 * 60 * 1000).toLong())
            .build()
    }

    // Lanzador para el diálogo del selector de fecha
    val datePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val timestamp = datePicker.selection
            if (timestamp != null) {
                selectedDate = Instant.fromEpochMilliseconds(timestamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
            }
        }
    }
    val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager

    var nombrePrograma by remember { mutableStateOf("") }
    var descripcionPrograma by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var diaSemana by remember { mutableStateOf("") }
    var locutor by remember { mutableStateOf("") }

    // New variable to hold the selected image URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // LaunchedEffect to handle the success state
    LaunchedEffect(key1 = programaUiState) {
        if (programaUiState is ProgramaUiState.Success) {
            Toast.makeText(context, "Programa guardado", Toast.LENGTH_SHORT).show()
            programaViewModel.restablecerProgramaGuardado()
        }
    }

    // LaunchedEffect to update the selectedImageUri
    LaunchedEffect(key1 = imagenProgramaUri) {
        selectedImageUri = imagenProgramaUri
    }

    val launcher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            programaViewModel.actualizarImagenPrograma(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Programa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (nombrePrograma.isBlank()) {
                            Toast.makeText(
                                context,
                                "El nombre del programa no puede estar vacío",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        programaViewModel.guardarPrograma(
                            Contenido.Programa(
                                nombrePrograma = nombrePrograma,
                                descripcionPrograma = descripcionPrograma,
                                imagenUriPrograma = selectedImageUri?.toString() ?: "",
                                horarioPrograma = "$diaSemana $horaInicio - $horaFin",
                                enlacePrograma = "", // Add the link here if needed
                                fechaPrograma = "", // Add the date here if needed
                                autorPrograma = locutor,
                                etiquetaPrograma = "", // Add the tag here if needed
                                duracionPrograma = "", // Add the duration here if needed
                                id = "" // Add the ID here if needed
                            ), userId
                        )
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar")
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección para la imagen de perfil
            Box(modifier = Modifier.size(128.dp)) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    // Mostrar una imagen predeterminada si no hay imagen seleccionada
                    Image(
                        painter = painterResource(id = R.drawable.user_pre),
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
            // Campos de entrada para la información del programa
            OutlinedTextField(
                value = nombrePrograma,
                onValueChange = { nombrePrograma = it },
                label = { Text("Nombre del programa") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = descripcionPrograma,
                onValueChange = { descripcionPrograma = it },
                label = { Text("Descripción del programa") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = locutor,
                onValueChange = { locutor = it },
                label = { Text("Locutor") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            // Campo del selector de fecha
            OutlinedTextField(
                value = selectedDate.toString(),
                onValueChange = { /* No hacer nada, manejado por el selector de fecha */ },
                label = { Text("Fecha del programa") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true, // Hacer el campo de solo lectura
                trailingIcon = {
                    IconButton(onClick = { fragmentManager?.let {
                        datePicker.show(it, "datePicker")
                    } }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Seleccionar fecha")
                    }
                }
            )
            OutlinedTextField(
                value = horaInicio,
                onValueChange = { horaInicio = it },
                label = { Text("Hora de inicio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = horaFin,
                onValueChange = { horaFin = it },
                label = { Text("Hora de fin") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = diaSemana,
                onValueChange = { diaSemana = it },
                label = { Text("Día de la semana") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Botón para guardar los cambios
            Button(
                onClick = {
                    if (nombrePrograma.isBlank()) {
                        Toast.makeText(
                            context,
                            "El nombre del programa no puede estar vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    programaViewModel.guardarPrograma(
                        Contenido.Programa(
                            nombrePrograma = nombrePrograma,
                            descripcionPrograma = descripcionPrograma,
                            imagenUriPrograma = selectedImageUri?.toString() ?: "",
                            horarioPrograma = "$diaSemana $horaInicio - $horaFin",
                            enlacePrograma = "", // Add the link here if needed
                            fechaPrograma = "", // Add the date here if needed
                            autorPrograma = locutor,
                            etiquetaPrograma = "", // Add the tag here if needed
                            duracionPrograma = "", // Add the duration here if needed
                            id = "" // Add the ID here if needed
                        ), userId
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}