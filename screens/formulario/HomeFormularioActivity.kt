package com.jcmateus.casanarestereo.screens.formulario


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmergencyRecording
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.filter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ContentAlpha
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.createLoginViewModel
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.joinToString
import kotlin.collections.remove
import kotlin.text.contains

class HomeFormularioActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = application as HomeApplication // Obtener la instancia de HomeApplication
        val navController = application.navController // Obtener navController de la aplicación


        setContent {
            CasanareStereoTheme {
                LaunchedEffect(Unit) {
                    navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                }
            }
        }
    }
}

@Composable
fun PantallaFinalScreen(viewModel: FormularioViewModel, navController: NavHostController) {
    val formularioGuardado = viewModel.formularioGuardado.value
    var segundosRestantes by remember { mutableStateOf(5) } // Inicializamos el contador en 5 segundos

    LaunchedEffect(key1 = formularioGuardado) {
        if (formularioGuardado) {
            while (segundosRestantes > 0) {
                delay(1000) // Espera 1 segundo
                segundosRestantes--
            }
            navController.navigate(Destinos.Pantalla16.ruta) // Navega a VideosYoutubeView
            viewModel._formularioGuardado.value = false // Reiniciar el estado
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF636768)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Éxito",
            tint = Color.Green,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "¡Gracias por participar!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Serás redirigido a la siguiente sección en $segundosRestantes segundos...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun SeleccionRolScreen(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as HomeApplication
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = 0.7f),
                blendMode = BlendMode.Darken
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CASANARE STEREO NETWORK",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                    Text(
                        text = "DONDE LATE EL CORAZÓN DEL LLANO",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seleciona Rol",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.surfaceBright
                )
                Button(
                    onClick = { navController.navigate(PantallaFormulario.Docentes.ruta) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth() // Usar todo el ancho disponible
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.TipsAndUpdates,
                            contentDescription = "Docentes",
                            modifier = Modifier
                                .size(34.dp),
                        )
                        Text(
                            text = "Encuesta Docentes",
                            modifier = Modifier
                                .basicMarquee()
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.scrim
                        )
                    }
                }
                Button(
                    onClick = { navController.navigate(PantallaFormulario.Estudiantes.ruta) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "Estudiantes",
                            modifier = Modifier
                                .size(34.dp),
                        )
                        Text(
                            text = "Encuesta Estudiantes",
                            modifier = Modifier
                                .basicMarquee()
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.scrim
                        )
                    }
                }
                Button(
                    onClick = {
                        application.showScaffold = true
                        navController.navigate(Destinos.Pantalla16.ruta)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmergencyRecording,
                            contentDescription = "Ir a curso",
                            modifier = Modifier
                                .size(34.dp),
                        )
                        Text(
                            text = "Ir a curso",
                            modifier = Modifier
                                .basicMarquee()
                                .weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.scrim
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Estudiantes(viewModel: FormularioViewModel, navController: NavHostController) {
    // Usar los valores del ViewModel si existen, de lo contrario usar cadenas vacías
    var edad by remember { mutableStateOf(viewModel.edad.value) }
    var institucion by remember { mutableStateOf(viewModel.institucion.value) }
    var municipio by remember { mutableStateOf(viewModel.municipioEstudiante.value) }
    var genero by remember { mutableStateOf(viewModel.genero.value) }
    var grado by remember { mutableStateOf(viewModel.grado.value) }
    var expandedGenero by remember { mutableStateOf(false) }
    var expandedGrado by remember { mutableStateOf(false) }
    var otroGenero by remember { mutableStateOf("") }
    var rotationAngle by remember { mutableFloatStateOf(180f) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showSnackbar by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp), // Padding horizontal para centrar el botón
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de Trasmedia Lab
                Image(
                    painter = painterResource(id = R.drawable.transmedia_lab),
                    contentDescription = "Logo Trasmedia Lab",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                // Título
                Text(
                    text = "Percepción de ciencia tecnología e innovación en el departamento de Casanare",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    edad,
                    {
                        edad = it
                        viewModel.agregarDatosFormulario("edad" to it)
                    },
                    "Edad",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    genero,
                    {
                        genero = it
                        viewModel.agregarDatosFormulario("genero" to it)
                    },
                    "Género",
                    listOf("Masculino", "Femenino", "Otro"),
                    expandedGenero,
                    { expandedGenero = it },
                    showTextFieldForOther = true,
                    onOtherValueChange = {
                        otroGenero = it
                        viewModel.agregarDatosFormulario("genero" to "$genero: $otroGenero")
                    },
                    otherValue = otroGenero
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    institucion,
                    {
                        institucion = it
                        viewModel.agregarDatosFormulario("institucion" to it)
                    },
                    "Institución Educativa",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    grado,
                    {
                        grado = it
                        viewModel.agregarDatosFormulario("grado" to it)
                    },
                    "Grado Escolar",
                    listOf(
                        "Sexto",
                        "Séptimo",
                        "Octavo",
                        "Noveno",
                        "Decimo",
                        "Once",
                        "Tecnico",
                        "Tecnólogo",
                        "Universitario"
                    ),
                    expandedGrado,
                    { expandedGrado = it },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    municipio,
                    {
                        municipio = it
                        viewModel.agregarDatosFormulario("municipio" to it)
                    },
                    "Municipio",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                val camposLlenos =
                    edad.isNotBlank() &&
                            institucion.isNotBlank() &&
                            municipio.isNotBlank() &&
                            (genero.isNotBlank() && (genero != "Otro" || otroGenero.isNotBlank())) &&
                            grado.isNotBlank()

                BotonSiguiente(isEnabled = camposLlenos) {
                    if (camposLlenos) {
                        navController.navigate(PantallaFormulario.Estudiantes1.ruta)
                    } else {
                        showSnackbar = true
                    }
                }
                LaunchedEffect(key1 = showSnackbar) {
                    if (showSnackbar) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos.")
                            showSnackbar = false
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        rotationAngle = 0f
    }
}

@Composable
fun Estudiantes1(viewModel: FormularioViewModel, navController: NavHostController) {
    var impacto by remember { mutableStateOf(viewModel.impacto.value) }
    var promocion by remember { mutableStateOf(viewModel.promocion.value) }
    var asignaturas by remember { mutableStateOf(viewModel.asignaturas.value) }
    var participacion by remember { mutableStateOf(viewModel.participacion.value) }
    var interes by remember { mutableStateOf(viewModel.interes.value) }
    var expandedImpacto by remember { mutableStateOf(false) }
    var expandedPromocion by remember { mutableStateOf(false) }
    var selectedAsignaturas by remember { mutableStateOf(emptyList<String>()) }
    var otraAsignatura by remember { mutableStateOf("") }
    var expandedParticipacion by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableFloatStateOf(180f) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showSnackbar by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de Trasmedia Lab
                Image(
                    painter = painterResource(id = R.drawable.transmedia_lab),
                    contentDescription = "Logo Trasmedia Lab",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                MenuDesplegable(
                    valor = impacto,
                    onValueChange = {
                        impacto = it
                        viewModel.agregarDatosFormulario("impacto" to it)
                    },
                    etiqueta = "¿Conoce la diferencia entre ciencia, tecnología e innovación?",
                    opciones = listOf("Si", "No"),
                    expanded = expandedImpacto,
                    onExpandedChange = { expandedImpacto = it }
                )
                Spacer(modifier = Modifier.height(40.dp))
                MenuDesplegable(
                    valor = promocion,
                    onValueChange = {
                        promocion = it
                        viewModel.agregarDatosFormulario("promocion" to it)
                    },
                    etiqueta = "¿Usted considera que el colegio promueve el desarrollo de conocimiento científico?",
                    opciones = listOf("Si", "No"),
                    expanded = expandedPromocion,
                    onExpandedChange = { expandedPromocion = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MultiSelectDropdown(
                    options = listOf(
                        "Química", "Física", "Biología", "Naturales", "Informática", "Sociales",
                        "Español", "Matemáticas", "Filosofía", "Otra _ cual?", "Ninguna"
                    ),
                    selectedOptions = selectedAsignaturas,
                    onOptionSelected = {
                        selectedAsignaturas = it
                    },
                    label = "¿Qué asignaturas relacionadas con la ciencia y tecnología estás cursando actualmente?",
                    otraAsignatura = otraAsignatura,
                    onOtraAsignaturaChange = {
                        otraAsignatura = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                // Guardar las asignaturas seleccionadas en el ViewModel
                LaunchedEffect(selectedAsignaturas, otraAsignatura) {
                    val asignaturasToSave = if (selectedAsignaturas.contains("Otra _ cual?")) {
                        selectedAsignaturas.filter { it != "Otra _ cual?" }
                            .joinToString(", ") + ", $otraAsignatura"
                    } else {
                        selectedAsignaturas.joinToString(", ")
                    }
                    viewModel.agregarDatosFormulario("asignaturas" to asignaturasToSave)
                }

                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    valor = participacion,
                    onValueChange = {
                        participacion = it
                        viewModel.agregarDatosFormulario("participacion" to it)
                    },
                    etiqueta = "¿Has participado en alguna actividad de innovación, ciencia, o creación tecnológica, como hackathons, ferias de ciencias, etc.?",
                    opciones = listOf("Si", "No"),
                    expanded = expandedParticipacion,
                    onExpandedChange = { expandedParticipacion = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    valor = interes,
                    onValueChange = {
                        interes = it
                        viewModel.agregarDatosFormulario("interes" to it)
                    },
                    etiqueta = "En una escala del 1 al 7, siendo 1 el menor y 7 el máximo ¿Cuánto interés tienes en temas de ciencia y la tecnología?",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                val camposLlenos = impacto.isNotBlank() && promocion.isNotBlank() &&
                        (selectedAsignaturas.isNotEmpty() && (!selectedAsignaturas.contains("Otra _ cual?") || otraAsignatura.isNotBlank())) &&
                        participacion.isNotBlank() && interes.isNotBlank()
                BotonSiguiente(isEnabled = camposLlenos) {
                    if (camposLlenos) {
                        navController.navigate(PantallaFormulario.Estudiantes2.ruta)
                    } else {
                        showSnackbar = true
                    }
                }
                LaunchedEffect(key1 = showSnackbar) {
                    if (showSnackbar) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos.")
                            showSnackbar = false
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        rotationAngle = 0f
    }
}

@Composable
fun Estudiantes2(viewModel: FormularioViewModel, navController: NavHostController) {
    var habilidades by remember { mutableStateOf(viewModel.habilidades.value) }
    var impacto by remember { mutableStateOf(viewModel.impactoTerritorio.value) }
    var conoceComunicacion by remember { mutableStateOf(viewModel.conoceComunicacion.value) }
    var interesaCrearContenido by remember { mutableStateOf(viewModel.interesaCrearContenido.value) }
    var calidadInformacion by remember { mutableStateOf(viewModel.calidadInformacion.value) }
    var expandedHabilidades by remember { mutableStateOf(false) }
    var expandedVideo3 by remember { mutableStateOf(false) }
    var expandedImVideo3 by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableFloatStateOf(180f) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showSnackbar by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp), // Padding horizontal para centrar el botón
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de Trasmedia Lab
                Image(
                    painter = painterResource(id = R.drawable.transmedia_lab),
                    contentDescription = "Logo Trasmedia Lab",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                MenuDesplegable(
                    habilidades,
                    {
                        habilidades = it
                        viewModel.agregarDatosFormulario("habilidades" to it)
                    },
                    "¿Crees que la educaciónque recibes en ciencia y tecnología te aporta herramientas y habilidades suficientes para tu formación a futuro?",
                    listOf("Si", "No"),
                    expandedHabilidades,
                    { expandedHabilidades = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    impacto,
                    {
                        impacto = it
                        viewModel.agregarDatosFormulario("impactoTerritorio" to it)
                    },
                    "¿Qué tipos de ciencia y tecnología identifica en su territorio?",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    conoceComunicacion,
                    {
                        conoceComunicacion = it
                        viewModel.agregarDatosFormulario("conoceComunicacion" to it)
                    },
                    "¿Conoces formas de comunicación de ciencia?",
                    listOf("Si", "No"),
                    expandedVideo3,
                    { expandedVideo3 = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    interesaCrearContenido,
                    {
                        interesaCrearContenido = it
                        viewModel.agregarDatosFormulario("interesaCrearContenido" to it)
                    },
                    "¿Te interesaría dedicarte a crear contenidos para divulgar temas de ciencia?",
                    listOf("Si", "No"),
                    expandedImVideo3,
                    { expandedImVideo3 = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    calidadInformacion,
                    {
                        calidadInformacion = it
                        viewModel.agregarDatosFormulario("calidadInformacion" to it)
                    },
                    "En una escala del 1 al 7, siendo 1 el mala y 7 el excelente ¿Qué opinas sobre la calidad de la información científica y tecnológica disponible en redes sociales?",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.padding(5.dp))

                val camposLlenos = habilidades.isNotBlank() && impacto.isNotBlank() &&
                        conoceComunicacion.isNotBlank() && interesaCrearContenido.isNotBlank() && calidadInformacion.isNotBlank()

                BotonSiguiente(isEnabled = camposLlenos) {
                    if (camposLlenos) {
                        navController.navigate(PantallaFormulario.Estudiantes3.ruta)
                    } else {
                        showSnackbar = true
                    }
                }
                LaunchedEffect(key1 = showSnackbar) {
                    if (showSnackbar) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos.")
                            showSnackbar = false
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        rotationAngle = 0f
    }
}

@Composable
fun Estudiantes3(viewModel: FormularioViewModel, navController: NavHostController) {
    var conocimientoDivulgacion by remember { mutableStateOf(viewModel.conocimientoDivulgacion.value) }
    var mediosDigitales by remember { mutableStateOf(viewModel.mediosDigitales.value) }
    var sigueCientificos by remember { mutableStateOf(viewModel.sigueCientificos.value) }
    var interesaCarrera by remember { mutableStateOf(viewModel.interesaCarrera.value) }
    var queEstudiar by remember { mutableStateOf(viewModel.queEstudiar.value) }
    var otroQueEstudiar by remember { mutableStateOf("") }
    var expandedCarrera by remember { mutableStateOf(false) }
    var expandedCientificos by remember { mutableStateOf(false) }
    var expandedDivulgacion by remember { mutableStateOf(false) }
    var expandedMedios by remember { mutableStateOf(false) }
    var expandedGustos by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var rotationAngle by remember { mutableFloatStateOf(180f) }
    var showSnackbar by remember { mutableStateOf(false) }


    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp), // Padding horizontal para centrar el botón
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de Trasmedia Lab
                Image(
                    painter = painterResource(id = R.drawable.transmedia_lab),
                    contentDescription = "Logo Trasmedia Lab",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                MenuDesplegable(
                    conocimientoDivulgacion,
                    {
                        conocimientoDivulgacion = it
                        viewModel.agregarDatosFormulario("conocimientoDivulgacion" to it)
                    },
                    "¿Tienes conocimiento de como realizar una pieza divulgativa?",
                    listOf("Si", "No"),
                    expandedDivulgacion,
                    { expandedDivulgacion = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    mediosDigitales,
                    {
                        mediosDigitales = it
                        viewModel.agregarDatosFormulario("mediosDigitales" to it)
                    },
                    "¿Qué medios digitales usas para informarte de CTeI?",
                    listOf(
                        "Redes sociales", "Plataformas de videos", "Plataformas de consulta",
                        "Medios de comunicación digitales", "Blogs", "Podcast"
                    ),
                    expandedMedios,
                    { expandedMedios = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    sigueCientificos,
                    {
                        sigueCientificos = it
                        viewModel.agregarDatosFormulario("sigueCientificos" to it)
                    },
                    "¿Sigues a científicos, divulgadores científicos o influenciadores nacionales o internacionales sobre ciencia en redes sociales o plataformas de video?",
                    listOf("Si", "No"),
                    expandedCientificos,
                    { expandedCientificos = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    interesaCarrera,
                    {
                        interesaCarrera = it
                        viewModel.agregarDatosFormulario("interesaCarrera" to it)
                    },
                    "¿Te interesa seguir una carrera relacionada con la ciencia o tecnología?",
                    listOf("Si", "No"),
                    expandedCarrera,
                    { expandedCarrera = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    queEstudiar,
                    {
                        queEstudiar = it
                        if (it != "Otro") {
                            otroQueEstudiar = ""
                        }
                        viewModel.agregarDatosFormulario("queEstudiar" to it)
                    },
                    "¿Qué te gustaría estudiar?",
                    listOf(
                        "Ecología",
                        "Medio ambiente",
                        "Ciencias sociales",
                        "Ingenierías",
                        "Ciencias económicas y administrativas",
                        "Ciencias del espacio",
                        "Ciencias básicas (Física, química, biología, matemáticas)",
                        "Ciencias de la salud",
                        "Ciencias agropecuarias",
                        "Otro"
                    ),
                    expandedGustos,
                    { expandedGustos = it },
                    showTextFieldForOther = true,
                    onOtherValueChange = {
                        otroQueEstudiar = it
                        viewModel.agregarDatosFormulario("queEstudiar" to "$queEstudiar: $otroQueEstudiar")
                    },
                    otherValue = otroQueEstudiar
                )
                val isButtonEnabled =
                    conocimientoDivulgacion.isNotBlank() &&
                            mediosDigitales.isNotBlank() &&
                            sigueCientificos.isNotBlank() &&
                            interesaCarrera.isNotBlank() &&
                            (queEstudiar.isNotBlank() && (queEstudiar != "Otro" || otroQueEstudiar.isNotBlank()))

                BotonSiguiente(isEnabled = isButtonEnabled) {
                    if (isButtonEnabled) {
                        viewModel.guardarFormularioEnFirebase("estudiante") { result ->
                            success = result as? Boolean
                            scope.launch {
                                withContext(Dispatchers.Main) {
                                    if (success == true) {
                                        navController.navigate(PantallaFormulario.PantallaFinal.ruta)
                                    } else {
                                        snackbarHostState.showSnackbar(
                                            message = "Error al guardar los datos. Inténtalo de nuevo."
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        showSnackbar = true
                    }
                }
                LaunchedEffect(key1 = showSnackbar) {
                    if (showSnackbar) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos.")
                            showSnackbar = false
                        }
                    }
                }

                // Mostrar Snackbar usando coroutines
                LaunchedEffect(key1 = success) {
                    if (success == true) {
                        snackbarHostState.showSnackbar(
                            message = "Datos guardados con éxito",
                            actionLabel = "OK"
                        )
                    } else if (success == false) {
                        snackbarHostState.showSnackbar(
                            message = "Error al guardar los datos. Inténtalo de nuevo."
                        )
                    }
                }
            }
            /*
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retroceder",
                    tint = MaterialTheme.colorScheme.background
                )
            }
             */
        }
    }

    LaunchedEffect(key1 = Unit) {
        rotationAngle = 0f
    }
}

@Composable
fun Docentes(viewModel: FormularioViewModel, navController: NavHostController) {
    var colegio by remember { mutableStateOf(viewModel.colegio.value) }
    var materia by remember { mutableStateOf(viewModel.materia.value) }
    var municipio by remember { mutableStateOf(viewModel.municipioDocente.value) }
    var nombreDocente by remember { mutableStateOf(viewModel.nombreDocente.value) }
    var telefonoDocente by remember { mutableStateOf(viewModel.telefonoDocente.value) }
    var correoDocente by remember { mutableStateOf(viewModel.correoDocente.value) }
    var politicaDatos by remember { mutableStateOf(viewModel.politicaDatos.value) }
    var expandedPolDatos by remember { mutableStateOf(false) }
    var selectedOptionConsidera by remember { mutableStateOf(viewModel.selectedOptionConsidera.value) }
    var expandedConsidera by remember { mutableStateOf(false) }
    val opcionesConsidera = listOf("Si", "No")
    var selectedOptionConsidera1 by remember { mutableStateOf(viewModel.selectedOptionConsidera1.value) }
    var expandedConsidera1 by remember { mutableStateOf(false) }
    val opcionesConsidera1 = listOf("Si", "No")
    var selectedOptionVocaciones by remember { mutableStateOf(viewModel.selectedOptionVocaciones.value) }
    var expandedVocaciones by remember { mutableStateOf(false) }
    val opcionesVocaciones = listOf("Si", "No")
    var selectedOptionFormacion by remember { mutableStateOf(viewModel.selectedOptionFormacion.value) }
    var expandedFormacion by remember { mutableStateOf(false) }
    val opcionesFormacion = listOf("Si", "No")
    var selectedOptionDesarrollo by remember { mutableStateOf(viewModel.selectedOptionDesarrollo.value) }
    var expandedDesarrollo by remember { mutableStateOf(false) }
    val opcionesDesarrollo = listOf(
        "Ciencias sociales", "Ingenierías", "Ciencias económicas y administrativas",
        "Ciencias del espacio", "Ciencias básicas (Física, química, biología, matemáticas)",
        "Ciencias de la salud", "Ciencias agropecuarias"
    )
    var success by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var rotationAngle by remember { mutableFloatStateOf(180f) }
    var showSnackbar by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de Trasmedia Lab
                Image(
                    painter = painterResource(id = R.drawable.transmedia_lab),
                    contentDescription = "Logo Trasmedia Lab",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                // Título
                Text(
                    text = "Percepción de ciencia tecnología e innovación en el departamento de Casanare",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    politicaDatos,
                    {
                        politicaDatos = it
                        viewModel.agregarDatosFormulario("politicaDatos" to it)
                    },
                    "¿Acepta la política de manejo de uso de los datos?",
                    listOf("Si", "No"),
                    expandedPolDatos,
                    { expandedPolDatos = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    nombreDocente,
                    {
                        nombreDocente = it
                        viewModel.agregarDatosFormulario("nombreDocente" to it)
                    },
                    "¿Cuál es su nombre completo?",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    telefonoDocente,
                    {
                        telefonoDocente = it
                        viewModel.agregarDatosFormulario("telefonoDocente" to it)
                    },
                    "¿Cuál es su número de teléfono?",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    correoDocente,
                    {
                        correoDocente = it
                        viewModel.agregarDatosFormulario("correoDocente" to it)
                    },
                    "¿Cuál es su correo electrónico?",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    colegio,
                    {
                        colegio = it
                        viewModel.agregarDatosFormulario("colegio" to it)
                    },
                    "¿Cuál es el nombre de la institución educativa donde se implementa el proyecto?",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    materia,
                    {
                        materia = it
                        viewModel.agregarDatosFormulario("materia" to it)
                    },
                    "¿En qué área académica se implementa el proyecto \"Casanare es científico ¿y tú?”?",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CampoTexto(
                    municipio,
                    {
                        municipio = it
                        viewModel.agregarDatosFormulario("municipioDocente" to it)
                    },
                    "¿Cuál es el municipio donde se implementa el proyecto?",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    selectedOptionConsidera,
                    {
                        selectedOptionConsidera = it
                        viewModel.agregarDatosFormulario("interesCTeI" to it)
                    },
                    "¿Considera que los estudiantes tiene interés en temas de CTeI?",
                    opcionesConsidera,
                    expandedConsidera,
                    { expandedConsidera = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    selectedOptionConsidera1,
                    {
                        selectedOptionConsidera1 = it
                        viewModel.agregarDatosFormulario("desarrollosCTeI" to it)
                    },
                    "¿Considera que los estudiantes identifican los desarrollos de CTeI que tienen en su región?",
                    opcionesConsidera1,
                    expandedConsidera1,
                    { expandedConsidera1 = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    selectedOptionVocaciones,
                    {
                        selectedOptionVocaciones = it
                        viewModel.agregarDatosFormulario("programaAcademico" to it)
                    },
                    "¿La institución educativa cuenta con un programa académico para desarrollar vocaciones científicas en los jóvenes?",
                    opcionesVocaciones,
                    expandedVocaciones,
                    { expandedVocaciones = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    selectedOptionFormacion,
                    {
                        selectedOptionFormacion = it
                        viewModel.agregarDatosFormulario("cursosHerramientas" to it)
                    },
                    "¿La institución educativa ofrece cursos o herramientas para el desarrollo de vocaciones científicas en los jóvenes?",
                    opcionesFormacion,
                    expandedFormacion,
                    { expandedFormacion = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                MenuDesplegable(
                    selectedOptionDesarrollo,
                    {
                        selectedOptionDesarrollo = it
                        viewModel.agregarDatosFormulario("areasDesarrollo" to it)
                    },
                    "¿Qué áreas de desarrollo considera que se pueden fortalecer en los estudiantes?",
                    opcionesDesarrollo,
                    expandedDesarrollo,
                    { expandedDesarrollo = it }
                )
                Spacer(modifier = Modifier.padding(20.dp))

                val isButtonEnabled = nombreDocente.isNotBlank() && telefonoDocente.isNotBlank() &&
                        correoDocente.isNotBlank() && politicaDatos.isNotBlank() && colegio.isNotBlank() &&
                        materia.isNotBlank() && municipio.isNotBlank() && selectedOptionConsidera.isNotBlank() &&
                        selectedOptionConsidera1.isNotBlank() && selectedOptionVocaciones.isNotBlank() &&
                        selectedOptionFormacion.isNotBlank() && selectedOptionDesarrollo.isNotBlank()

                BotonSiguiente(isEnabled = isButtonEnabled) {
                    if (isButtonEnabled) {
                        viewModel.guardarFormularioEnFirebase("docente") { result ->
                            success = result as? Boolean
                            scope.launch {
                                withContext(Dispatchers.Main) {
                                    if (success == true) {
                                        navController.navigate(PantallaFormulario.PantallaFinal.ruta)
                                    } else {
                                        snackbarHostState.showSnackbar(
                                            message = "Error al guardar los datos. Inténtalo de nuevo."
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        showSnackbar = true
                    }
                }
                LaunchedEffect(key1 = showSnackbar) {
                    if (showSnackbar) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos.")
                            showSnackbar = false
                        }
                    }
                }

                // Mostrar Snackbar usando coroutines
                LaunchedEffect(key1 = success) {
                    if (success == true) {
                        snackbarHostState.showSnackbar(
                            message = "Datos guardados con éxito",
                            actionLabel = "OK"
                        )
                    } else if (success == false) {
                        snackbarHostState.showSnackbar(
                            message = "Error al guardar los datos. Inténtalo de nuevo."
                        )
                    }
                }
            }
            /*
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retroceder",
                    tint = MaterialTheme.colorScheme.background
                )
            }
             */
        }
    }

    LaunchedEffect(key1 = Unit) {
        rotationAngle = 0f
    }
}

@Composable
fun MultiSelectDropdown(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionSelected: (List<String>) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    otraAsignatura: String,
    onOtraAsignaturaChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var expanded by remember { mutableStateOf(false) }
    var showOtraTextField by remember { mutableStateOf(selectedOptions.contains("Otra _ cual?")) }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "Expandir"
                )
            }
        }

        if (expanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val newSelectedOptions = selectedOptions.toMutableList()
                                if (option == "Otra _ cual?") {
                                    showOtraTextField = !showOtraTextField
                                    if (newSelectedOptions.contains(option)) {
                                        newSelectedOptions.remove(option)
                                    } else {
                                        newSelectedOptions.add(option)
                                    }
                                } else {
                                    if (newSelectedOptions.contains(option)) {
                                        newSelectedOptions.remove(option)
                                    } else {
                                        newSelectedOptions.add(option)
                                    }
                                }
                                onOptionSelected(newSelectedOptions)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedOptions.contains(option),
                            onCheckedChange = { isChecked ->
                                val newSelectedOptions = selectedOptions.toMutableList()
                                if (isChecked) {
                                    newSelectedOptions.add(option)
                                    if (option == "Otra _ cual?") {
                                        showOtraTextField = true
                                    }
                                } else {
                                    newSelectedOptions.remove(option)
                                    if (option == "Otra _ cual?") {
                                        showOtraTextField = false
                                    }
                                }
                                onOptionSelected(newSelectedOptions)
                            }
                        )
                        Text(text = option)
                    }
                }
            }
        }

        if (showOtraTextField) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = otraAsignatura,
                onValueChange = {
                    onOtraAsignaturaChange(it)
                },
                label = { Text("Especifique cuál") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions
            )
        }
        if (!expanded && selectedOptions.isNotEmpty()) {
            Text(
                text = "Seleccionado: ${selectedOptions.joinToString(", ")}",
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun CampoTexto(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        modifier = modifier,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.surface,
            fontSize = 18.sp
        ),
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun MenuDesplegable(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    opciones: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(color = MaterialTheme.colorScheme.surface, fontSize = 18.sp),
    showTextFieldForOther: Boolean = false,
    onOtherValueChange: ((String) -> Unit)? = null,
    otherValue: String = ""
) {
    var showOtherTextField by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        TextField(
            value = valor,
            onValueChange = {
                onValueChange(it)
                if (it != "Otro") {
                    showOtherTextField = false
                    onOtherValueChange?.invoke("")
                } else {
                    showOtherTextField = true
                }
            },
            label = { Text(etiqueta) },
            trailingIcon = {
                IconButton(onClick = { onExpandedChange(!expanded) }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = if (expanded) "Ocultar opciones" else "Mostrar opciones"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(!expanded) },
            textStyle = textStyle,
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(text = opcion) },
                    onClick = {
                        onValueChange(opcion)
                        onExpandedChange(false)
                        if (opcion == "Otro") {
                            showOtherTextField = true
                        } else {
                            showOtherTextField = false
                            onOtherValueChange?.invoke("")
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        if (showTextFieldForOther && showOtherTextField) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = otherValue,
                onValueChange = {
                    onOtherValueChange?.invoke(it)
                },
                label = { Text("Especifique cuál") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun BotonSiguiente(isEnabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isEnabled, // Habilitar/deshabilitar el botón
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.12f), // Color del contenedor deshabilitado
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.disabled) // Color del contenido deshabilitado
        ),
    ) {
        Text(
            text = "Siguiente",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 24.sp
        )
    }
}

sealed class PantallaFormulario(val ruta: String) {
    data object SeleccionRol : PantallaFormulario("seleccion_rol")
    data object Estudiantes : PantallaFormulario("estudiantes")
    data object Estudiantes1 : PantallaFormulario("estudiantes_1")
    data object Estudiantes2 : PantallaFormulario("estudiantes_2")
    data object Estudiantes3 : PantallaFormulario("estudiantes_3")
    data object Docentes : PantallaFormulario("docentes")
    data object PantallaFinal : PantallaFormulario("pantalla_final")
}

