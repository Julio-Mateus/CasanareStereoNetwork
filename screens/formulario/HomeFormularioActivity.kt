package com.jcmateus.casanarestereo.screens.formulario

//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.AnimatedSoundWave
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.formulario.ui.theme.CasanareStereoTheme
import com.jcmateus.casanarestereo.screens.home.Destinos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFormularioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CasanareStereoTheme {
                val navController = rememberNavController()
                val viewModel: FormularioViewModel = viewModel()
                LaunchedEffect(Unit) {
                    navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                }
                NavigationHost(navController = navController, innerPadding = PaddingValues())
            }
        }
    }
}

@Composable
fun PantallaFinalScreen(viewModel: FormularioViewModel, navController: NavHostController) {
    val formularioGuardado = viewModel.formularioGuardado.value // Accede al valor directamente

    LaunchedEffect(key1 = formularioGuardado) {
        if(formularioGuardado){
            delay(5000) // Retraso de 1 segundo (opcional)
            navController.navigate(Destinos.Pantalla16.ruta) // Navega a VideosYoutubeView
            viewModel._formularioGuardado.value = false // Reiniciar el estado
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2F7)), // Color de fondo verde suave,
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
                .padding(bottom = 16.dp)


        )
    }
}

@Composable
fun SeleccionRolScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Usamos el color de fondo del tema
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = 0.7f), // Oscurecemos la imagen con un filtro de color
                blendMode = BlendMode.Darken
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), //Padding consistente
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "BIENVENIDO A:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 44.sp,
                    color = MaterialTheme.colorScheme.onBackground, // Color del texto sobre el fondo
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))
                AnimatedSoundWave()
                /*
                var angle by remember { mutableStateOf(0f) }
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val angleAnim by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = ""
                )
                angle = angleAnim
                Column(
                    // ...
                ){
                    Image(
                        painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu imagen
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(120.dp) // Establece el tamaño del logo
                            .graphicsLayer {
                                rotationZ = angle
                            }

                    )
                }
                */
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "CASANARE STEREO NETWORK",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
                    )
                    Text(
                        text = "DONDE LATE EL CORAZÓN DEL LLANO",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp), // Espaciado entre botones
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Seleciona Rol",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.surface
                )
                Button(
                    onClick = { navController.navigate(PantallaFormulario.Docentes.ruta) },
                    modifier = Modifier
                        .padding(16.dp)
                        .width(300.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.TipsAndUpdates,
                        contentDescription = "Docentes",
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .size(34.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = "Docentes",
                        modifier = Modifier
                            .padding(end = 45.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.scrim
                    )
                }
                Button(
                    onClick = { navController.navigate(PantallaFormulario.Estudiantes.ruta)},
                    modifier = Modifier
                        .padding(16.dp)
                        .width(300.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.School,
                        contentDescription = "Estudiantes",
                        modifier = Modifier
                            .padding(end = 26.dp)
                            .size(34.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = "Estudiantes",
                        modifier = Modifier
                            .padding(end = 25.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.scrim
                    )
                }
            }

        }
    }
}

@Composable
fun Estudiantes(viewModel: FormularioViewModel, navController: NavHostController) {
    var edad by remember { mutableStateOf("") }
    var institucion by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var grado by remember { mutableStateOf("") }
    var expandedGenero by remember { mutableStateOf(false) } // Declaración de expandedGenero
    var expandedGrado by remember { mutableStateOf(false) } // Declaración de expandedGrado


    val camposLlenos = edad.isNotBlank() && institucion.isNotBlank() && municipio.isNotBlank() && genero.isNotBlank() && grado.isNotBlank()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { CampoTexto(edad, { edad = it }, "Edad") }
        item {
            MenuDesplegable(
                genero,
                { genero = it },
                "Género",
                listOf("Masculino", "Femenino", "Otro"),
                expandedGenero,
                { expandedGenero = it },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            )
        }
        item { CampoTexto(institucion, { institucion = it }, "Institución Educativa") }
        item {
            MenuDesplegable(
                grado,
                { grado = it },
                "Grado Escolar",
                listOf("Sexto", "Séptimo", "Octavo", "Noveno", "Decimo", "Once"),
                expandedGrado,
                { expandedGrado = it },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            )
        }
        item { CampoTexto(municipio, { municipio = it }, "Municipio") }
        item {
            BotonSiguiente(enabled = camposLlenos) { // Habilitar botón solo si los campos están llenos
                viewModel.agregarDatosFormulario("edad" to edad)
                viewModel.agregarDatosFormulario("institucion" to institucion)
                viewModel.agregarDatosFormulario("municipio" to municipio)
                viewModel.agregarDatosFormulario("genero" to genero)
                viewModel.agregarDatosFormulario("grado" to grado)
                navController.navigate(PantallaFormulario.Estudiantes1.ruta)
            }
        }
    }

}

@Composable
fun Estudiantes1(viewModel: FormularioViewModel, navController: NavHostController) {
    var impacto by remember { mutableStateOf("") }
    var promocion by remember { mutableStateOf("") }
    var asignaturas by remember { mutableStateOf("") }
    var participacion by remember { mutableStateOf("") }
    var interes by remember { mutableStateOf("") }
    var expandedImpacto by remember { mutableStateOf(false) } // Declaración de expandedGenero
    var expandedPromocion by remember { mutableStateOf(false) } // Declaración de expandedGrado
    var expandedAsignaturas by remember { mutableStateOf(false) } // Declaración de expandedGenero
    var expandedParticipacion by remember { mutableStateOf(false) } // Declaración de expandedGrado

    Column {
        MenuDesplegable(
            impacto,
            { impacto = it },
            "¿Conoce la diferencia entre ciencia, tecnología e innovación?",
            listOf("Si", "No"),
            expandedImpacto,
            { expandedImpacto = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuDesplegable(
            promocion,
            { promocion = it },
            "¿Usted considera que el colegio promueve el desarrollo de conocimiento científico?",
            listOf("Si", "No"),
            expandedPromocion,
            { expandedPromocion = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuDesplegable(
            asignaturas,
            { asignaturas = it },
            "¿Qué asignaturas relacionadas con la ciencia y tecnología estás cursando actualmente?",
            listOf(
                "Química", "Física", "Biología", "Naturales", "Informática", "Sociales",
                "Español", "Matemáticas", "Filosofía", "Otra _ cual?", "Ninguna"
            ),
            expandedAsignaturas,
            { expandedAsignaturas = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuDesplegable(
            participacion,
            { participacion = it },
            "¿Has participado en alguna actividad de innovación, ciencia, o creación tecnológica, como hackathons, ferias de ciencias, etc.?",
            listOf("Si", "No"),
            expandedParticipacion,
            { expandedParticipacion = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CampoTexto(
            interes,
            { interes = it },
            "En una escala del 1 al 7, siendo 1 el menor y 7 el máximo ¿Cuánto interés tienes en temas de ciencia y la tecnología?"
        )
        Spacer(modifier = Modifier.height(16.dp))

        BotonSiguiente {
            viewModel.agregarDatosFormulario("impacto" to impacto)
            viewModel.agregarDatosFormulario("promocion" to promocion)
            viewModel.agregarDatosFormulario("asignaturas" to asignaturas)
            viewModel.agregarDatosFormulario("participacion" to participacion)
            viewModel.agregarDatosFormulario("interes" to interes)
            navController.navigate(PantallaFormulario.Estudiantes2.ruta) // Navega a la siguiente pantalla
        }
    }
}

@Composable
fun Estudiantes2(viewModel: FormularioViewModel, navController: NavHostController) {
    var habilidades by remember { mutableStateOf("") }
    var impacto by remember { mutableStateOf("") }
    var conoceComunicacion by remember { mutableStateOf("") }
    var interesaCrearContenido by remember { mutableStateOf("") }
    var calidadInformacion by remember { mutableStateOf("") }
    var expandedHabilidades by remember { mutableStateOf(false) }
    var expandedVideo3 by remember { mutableStateOf(false) }
    var expandedImVideo3 by remember { mutableStateOf(false) }


    Column {
        MenuDesplegable(
            habilidades,
            { habilidades = it },
            "¿Crees que la educación que recibes en ciencia y tecnología te aporta herramientas y habilidades suficientes para tu formación a futuro?",
            listOf("Si", "No"),
            expandedHabilidades,
            { expandedHabilidades = it }
        )
        Spacer(modifier = Modifier.padding(20.dp))
        CampoTexto(
            impacto,
            { impacto = it },
            "¿Qué tipos de ciencia y tecnología identifica en su territorio?"
        )
        Spacer(modifier = Modifier.padding(20.dp))
        MenuDesplegable(
            conoceComunicacion,
            { conoceComunicacion = it },
            "¿Conoces formas de comunicación de ciencia?",
            listOf("Si", "No"),
            expandedVideo3,
            { expandedVideo3 = it }
        )
        Spacer(modifier = Modifier.padding(20.dp))
        MenuDesplegable(
            interesaCrearContenido,
            { interesaCrearContenido = it },
            "¿Te interesaría dedicarte a crear contenidos para divulgar temas de ciencia?",
            listOf("Si", "No"),
            expandedImVideo3,
            { expandedImVideo3 = it }
        )
        Spacer(modifier = Modifier.padding(20.dp))
        CampoTexto(
            calidadInformacion,
            { calidadInformacion = it },
            "En una escala del 1 al 7, siendo 1 el mala y 7 el excelente ¿Qué opinas sobre la calidad de la información científica y tecnológica disponible en redes sociales?"
        )
        Spacer(modifier = Modifier.padding(20.dp))
        BotonSiguiente {
            Log.d("Docentes", "onClick BotonSiguiente")
            viewModel.agregarDatosFormulario("habilidades" to habilidades)
            viewModel.agregarDatosFormulario("impacto" to impacto)
            viewModel.agregarDatosFormulario("conoceComunicacion" to conoceComunicacion)
            viewModel.agregarDatosFormulario("interesaCrearContenido" to interesaCrearContenido)
            viewModel.agregarDatosFormulario("calidadInformacion" to calidadInformacion)
            navController.navigate(PantallaFormulario.Estudiantes3.ruta) // Navega a la siguiente pantalla
        }
    }

}

@Composable
fun Estudiantes3(viewModel: FormularioViewModel, navController: NavHostController) {
    var conocimientoDivulgacion by remember { mutableStateOf("") }
    var mediosDigitales by remember { mutableStateOf("") }
    var sigueCientificos by remember { mutableStateOf("") }
    var interesaCarrera by remember { mutableStateOf("") }
    var queEstudiar by remember { mutableStateOf("") }
    var expandedMedios by remember { mutableStateOf(false) }
    var expandedGustos by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column {
        CampoTexto(
            conocimientoDivulgacion,
            { conocimientoDivulgacion = it },
            "¿Tienes conocimiento de como realizar una pieza divulgativa?"
        )
        Spacer(modifier = Modifier.padding(20.dp))
        MenuDesplegable(
            mediosDigitales,
            { mediosDigitales = it },
            "¿Qué medios digitales usas para informarte de CTeI?",
            listOf(
                "Redes sociales", "Plataformas de videos", "Plataformas de consulta",
                "Medios de comunicación digitales", "Blogs", "Podcast"
            ),
            expandedMedios,
            { expandedMedios = it }
        )
        Spacer(modifier = Modifier.padding(20.dp))
        CampoTexto(
            sigueCientificos,
            { sigueCientificos = it },
            "¿Sigues a científicos, divulgadores científicos o influenciadores nacionales o internacionales sobre ciencia en redes sociales o plataformas de video?"
        )
        Spacer(modifier = Modifier.padding(20.dp))
        CampoTexto(
            interesaCarrera,
            { interesaCarrera = it },
            "¿Te interesa seguir una carrera relacionada con la ciencia o tecnología?"
        )
        Spacer(modifier = Modifier.padding(20.dp))
        MenuDesplegable(
            queEstudiar,
            { queEstudiar = it },
            "¿Qué te gustaría estudiar?",
            listOf(
                "Cienciassociales", "Ingenierías", "Ciencias económicas y administrativas",
                "Ciencias del espacio", "Ciencias básicas (Física, química, biología, matemáticas",
                "Ciencias de la salud", "Ciencias agropecuarias"
            ),
            expandedGustos,
            { expandedGustos = it }
        )
        Spacer(modifier = Modifier.padding(20.dp))
        BotonSiguiente {
            viewModel.agregarDatosFormulario("conocimientoDivulgacion" to conocimientoDivulgacion)
            viewModel.agregarDatosFormulario("mediosDigitales" to mediosDigitales)
            viewModel.agregarDatosFormulario("sigueCientificos" to sigueCientificos)
            viewModel.agregarDatosFormulario("interesaCarrera" to interesaCarrera)
            viewModel.agregarDatosFormulario("queEstudiar" to queEstudiar)
            viewModel.guardarFormularioEnFirebase("estudiante") { result ->
                    success = result as? Boolean
                    scope.launch {
                        if (success == true) {// Lanzar una coroutine para mostrar el Snackbar
                            navController.navigate(PantallaFormulario.PantallaFinal.ruta) // Navega a PantallaFinal
                    }else {
                            // Manejar error al guardar

                    }
                }
            }
           // navController.navigate(Destinos.Pantalla16.ruta)
        }
        // Mostrar Snackbar usando coroutines
        LaunchedEffect(key1 = success) {
            if (success == true) {
                snackbarHostState.showSnackbar(
                    message = "Datos guardados con éxito",
                    actionLabel = "OK"
                )
            }else if (success == false) {
                snackbarHostState.showSnackbar(
                    message = "Error al guardar los datos. Inténtalo de nuevo."
                )
            }
        }
    }

}

@Composable
fun Docentes(viewModel: FormularioViewModel, navController: NavHostController) {
    var colegio by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var selectedOptionConsidera by remember { mutableStateOf("") }
    var expandedConsidera by remember { mutableStateOf(false) }
    val opcionesConsidera = listOf("Si", "No")
    var selectedOptionConsidera1 by remember { mutableStateOf("") }
    var expandedConsidera1 by remember { mutableStateOf(false) }
    val opcionesConsidera1 = listOf("Si", "No")
    var selectedOptionVocaciones by remember { mutableStateOf("") }
    var expandedVocaciones by remember { mutableStateOf(false) }
    val opcionesVocaciones = listOf("Si", "No")
    var selectedOptionFormacion by remember { mutableStateOf("") }
    var expandedFormacion by remember { mutableStateOf(false) }
    val opcionesFormacion = listOf("Si", "No")
    var selectedOptionDesarrollo by remember { mutableStateOf("") }
    var expandedDesarrollo by remember { mutableStateOf(false) }
    val opcionesDesarrollo = listOf(
        "Ciencias sociales", "Ingenierías", "Ciencias económicas y administrativas",
        "Ciencias del espacio", "Ciencias básicas (Física, química, biología, matemáticas)",
        "Ciencias de la salud", "Ciencias agropecuarias"
    )
    var success by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold{ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            CampoTexto(colegio, { colegio = it }, "¿Cuál es el nombre de la institución educativa donde se implementa el proyecto?")
            Spacer(modifier = Modifier.padding(20.dp))
            CampoTexto(materia, { materia = it }, "¿En qué área académica se implementa el proyecto \"Casanare es científico ¿y tú?”?")
            Spacer(modifier = Modifier.padding(20.dp))
            CampoTexto(municipio, { municipio = it }, "¿Cuál es el municipio donde se implementa el proyecto?")
            Spacer(modifier = Modifier.padding(20.dp))
            MenuDesplegable(
                selectedOptionConsidera,
                { selectedOptionConsidera = it },
                "¿Considera que los estudiantes tiene interés en temas de CTeI?",
                opcionesConsidera,
                expandedConsidera,
                { expandedConsidera = it }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            MenuDesplegable(
                selectedOptionConsidera1,
                { selectedOptionConsidera1 = it },
                "¿Considera que los estudiantes identifican los desarrollos de CTeI que tienen en su región?",
                opcionesConsidera1,
                expandedConsidera1,
                { expandedConsidera1 = it }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            MenuDesplegable(
                selectedOptionVocaciones,
                { selectedOptionVocaciones = it },
                "¿La institución educativa cuenta con un programa académico para desarrollar vocaciones científicas en los jóvenes?",
                opcionesVocaciones,
                expandedVocaciones,
                { expandedVocaciones = it }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            MenuDesplegable(
                selectedOptionFormacion,
                { selectedOptionFormacion = it },
                "¿En la I.E se imparten cursos para enseñar a los jóvenes sobre el uso de herramientas tecnológicas para crear y divulgar contenidos?",
                opcionesFormacion,
                expandedFormacion,
                { expandedFormacion = it }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            MenuDesplegable(
                selectedOptionDesarrollo,
                { selectedOptionDesarrollo = it },
                "¿Cuáles son las áreas de mayor desarrollo profesional de los estudiantes de la I.E?",
                opcionesDesarrollo,
                expandedDesarrollo,
                { expandedDesarrollo = it }
            )
            // Variable para controlar si el botón está habilitado
            val isButtonEnabled = colegio.isNotBlank() && materia.isNotBlank() && municipio.isNotBlank() &&
                    selectedOptionConsidera.isNotBlank() && selectedOptionConsidera1.isNotBlank() &&
                    selectedOptionVocaciones.isNotBlank() && selectedOptionFormacion.isNotBlank() &&
                    selectedOptionDesarrollo.isNotBlank()
            Spacer(modifier = Modifier.padding(20.dp))
            BotonSiguiente(isEnabled = isButtonEnabled) {
                viewModel.agregarDatosFormulario("colegio" to colegio)
                viewModel.agregarDatosFormulario("materia" to materia)
                viewModel.agregarDatosFormulario("municipio" to municipio)
                viewModel.agregarDatosFormulario("interesCTeI" to selectedOptionConsidera)
                viewModel.agregarDatosFormulario("desarrollosCTeI" to selectedOptionConsidera1)
                viewModel.agregarDatosFormulario("programaAcademico" to selectedOptionVocaciones)
                viewModel.agregarDatosFormulario("cursosHerramientas" to selectedOptionFormacion)
                viewModel.agregarDatosFormulario("areasDesarrollo" to selectedOptionDesarrollo)
                viewModel.guardarFormularioEnFirebase("docente") { result ->
                        success =result as? Boolean
                        scope.launch {
                            withContext(Dispatchers.Main){
                                if (success == true) {
                                    navController.navigate(PantallaFormulario.PantallaFinal.ruta) // Navega a PantallaFinal
                                }else {
                                    // Manejar error al guardar
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Error al guardar los datos. Inténtalo de nuevo."
                                        )
                                    }
                                }
                            }

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
                }else if (success == false) {
                    snackbarHostState.showSnackbar(
                        message = "Error al guardar los datos. Inténtalo de nuevo."
                    )
                }
            }
        }
    }

}

@Composable
fun CampoTexto(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        //.size(width = Dp.Unspecified,
            //height = 60.dp),
    //textStyle: TextStyle = TextStyle(color = Color.Black)
) {
    TextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        modifier = modifier,
        enabled = enabled,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
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
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .size(width = Dp.Unspecified, height = 60.dp),
    textStyle: TextStyle = TextStyle(color = Color.Black)
) {
    Column( // <-- Cambiar Box por Column
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally // <-- Añadir centrado horizontal
    ){
        TextField(
            value = valor,
            onValueChange = onValueChange,
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
                .fillMaxWidth(),
            textStyle = textStyle
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(onClick = {
                    onValueChange(opcion)
                    onExpandedChange(false)
                }) {
                    Text(text = opcion)
                }
            }
        }
    }
}
@Composable
fun BotonSiguiente(isEnabled: Boolean = true, enabled: Boolean = true, onClick:  () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isEnabled, // Habilitar/deshabilitar el botón
        modifier = Modifier.padding(16.dp)
            .width(300.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.surface
        )
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
    data object Estudiantes1: PantallaFormulario("estudiantes_1")
    data object Estudiantes2 : PantallaFormulario("estudiantes_2")
    data object Estudiantes3 : PantallaFormulario("estudiantes_3")
    data object Docentes : PantallaFormulario("docentes")
    data object PantallaFinal : PantallaFormulario("pantalla_final")
}

@Preview(showBackground = true)
@Composable
fun CasanareStereoTheme() {
    SeleccionRolScreen(navController = rememberNavController())
}
