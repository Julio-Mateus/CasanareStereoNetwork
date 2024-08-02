package com.jcmateus.casanarestereo.screens.formulario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.screens.formulario.ui.theme.CasanareStereoTheme
import com.jcmateus.casanarestereo.screens.home.Destinos
import kotlinx.coroutines.launch

class HomeFormularioActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CasanareStereoTheme {
                val viewModel: FormularioViewModel = viewModel()
                FormularioScreen( viewModel = viewModel, navController = rememberNavController())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FormularioScreen(viewModel: FormularioViewModel, navController: NavHostController) {
    val viewModel: FormularioViewModel = viewModel()
    val pagerState = rememberPagerState(pageCount = { 7 })
    val coroutineScope = rememberCoroutineScope()
    var showFirstViewButtons by remember { mutableStateOf(true) }
    var showSnackbar by remember { mutableStateOf(false) }

    HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
        when (page) {
            0 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Seleciona El Rol",
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (showFirstViewButtons) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(5)
                                }
                                showFirstViewButtons = false
                            },
                            modifier = Modifier
                                .padding(50.dp)
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
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.scrim
                            )
                        }
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                                showFirstViewButtons = false
                            },
                            modifier = Modifier
                                .padding(50.dp)
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

            1 -> {
                // Estudiantes - Parte 1
                Estudiantes(viewModel) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }

            2 -> {
                // Estudiantes - Parte 2
                Estudiantes1(viewModel) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }

            3 -> {
                // Estudiantes - Parte 3
                Estudiantes2(viewModel) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }

            4 -> {
                // Estudiantes - Parte 4
                Estudiantes3(viewModel, navController) {
                    viewModel.guardarFormularioEnFirebase("estudiante") { success ->
                        if (success as Boolean) {
                            showSnackbar = true
                            navController.navigate(Destinos.Pantalla16.ruta)
                        } else {
                            // Manejar error al guardar
                        }
                    }
                    navController.navigate(Destinos.Pantalla16.ruta) // Navegar a VideosYoutubeView}
                }
            }
//
            5 -> {
                // Docentes
                Docentes(viewModel, navController) {
                    viewModel.guardarFormularioEnFirebase("docente") { success ->
                        if (success as Boolean) {
                            showSnackbar = true
                            navController.navigate(Destinos.Pantalla16.ruta)
                        } else {
                            // Manejar error al guardar
                        }
                    }
                    navController.navigate(Destinos.Pantalla16.ruta) // Navegar a VideosYoutubeView
                }
            }
        }
    }


}

@Composable
fun Estudiantes(viewModel: FormularioViewModel, onSiguiente: () -> Unit) {
    var edad by remember { mutableStateOf("") }
    var institucion by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var grado by remember { mutableStateOf("") }
    var expandedGenero by remember { mutableStateOf(false) } // Declaración de expandedGenero
    var expandedGrado by remember { mutableStateOf(false) } // Declaración de expandedGrado
    Column{
        CampoTexto(edad, { edad = it }, "Edad")
        Spacer(modifier = Modifier.height(16.dp))
        MenuDesplegable(
            genero,
            { genero = it },
            "Género",
            listOf("Masculino", "Femenino", "Otro"),
            expandedGenero,
            { expandedGenero = it }
        )
        Spacer(modifier = Modifier.padding(30.dp))
        CampoTexto(institucion, { institucion = it }, "Institución Educativa")
        Spacer(modifier = Modifier.height(16.dp))
        MenuDesplegable(
            grado,
            { grado = it },
            "Grado Escolar",
            listOf("Sexto", "Séptimo", "Octavo", "Noveno", "Decimo", "Once"),
            expandedGrado,
            { expandedGrado = it }
        )
        Spacer(modifier = Modifier.padding(30.dp))
        CampoTexto(municipio, { municipio = it }, "Municipio")
        Spacer(modifier = Modifier.height(16.dp))
        BotonSiguiente {
            viewModel.agregarDatosFormulario("edad" to edad)
            viewModel.agregarDatosFormulario("institucion" to institucion)
            viewModel.agregarDatosFormulario("municipio" to municipio)
            viewModel.agregarDatosFormulario("genero" to genero)
            viewModel.agregarDatosFormulario("grado" to grado)
            onSiguiente()
        }
    }

}

@Composable
fun Estudiantes1(viewModel: FormularioViewModel, onSiguiente: () -> Unit) {
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
            onSiguiente()
        }
    }
}

@Composable
fun Estudiantes2(viewModel: FormularioViewModel, onSiguiente: () -> Unit) {
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
            viewModel.agregarDatosFormulario("habilidades" to habilidades)
            viewModel.agregarDatosFormulario("impacto" to impacto)
            viewModel.agregarDatosFormulario("conoceComunicacion" to conoceComunicacion)
            viewModel.agregarDatosFormulario("interesaCrearContenido" to interesaCrearContenido)
            viewModel.agregarDatosFormulario("calidadInformacion" to calidadInformacion)
            onSiguiente()
        }
    }

}

@Composable
fun Estudiantes3(viewModel: FormularioViewModel, navController: NavHostController, function: () -> Unit) {
    var conocimientoDivulgacion by remember { mutableStateOf("") }
    var mediosDigitales by remember { mutableStateOf("") }
    var sigueCientificos by remember { mutableStateOf("") }
    var interesaCarrera by remember { mutableStateOf("") }
    var queEstudiar by remember { mutableStateOf("") }
    var expandedMedios by remember { mutableStateOf(false) }
    var expandedGustos by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

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
            viewModel.guardarFormularioEnFirebase("estudiante") { success ->
                if (success as Boolean) {
                    showSnackbar = true
                    navController.navigate(Destinos.Pantalla16.ruta)
                } else {
                    // Manejar error al guardar
                }
            }
           // navController.navigate(Destinos.Pantalla16.ruta)
        }
        if (showSnackbar) {
            Snackbar(
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("OK")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) { Text("Datos guardados con éxito") }
        }
    }

}

@Composable
fun Docentes(viewModel: FormularioViewModel, scaffoldState: ScaffoldState = rememberScaffoldState(), navController: NavHostController, function: () -> Unit) {
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
    var showSnackbar by remember { mutableStateOf(false) }

    Scaffold(){ innerPadding ->
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
            Spacer(modifier = Modifier.padding(20.dp))
            BotonSiguiente {
                viewModel.agregarDatosFormulario("colegio" to colegio)
                viewModel.agregarDatosFormulario("materia" to materia)
                viewModel.agregarDatosFormulario("municipio" to municipio)
                viewModel.agregarDatosFormulario("interesCTeI" to selectedOptionConsidera)
                viewModel.agregarDatosFormulario("desarrollosCTeI" to selectedOptionConsidera1)
                viewModel.agregarDatosFormulario("programaAcademico" to selectedOptionVocaciones)
                viewModel.agregarDatosFormulario("cursosHerramientas" to selectedOptionFormacion)
                viewModel.agregarDatosFormulario("areasDesarrollo" to selectedOptionDesarrollo)
                viewModel.guardarFormularioEnFirebase("docente") { success ->
                    if (success) {
                        showSnackbar = true
                        navController.navigate(Destinos.Pantalla16.ruta)
                    } else {
                        // Manejar error al guardar
                    }
                }
                // Mostrar Snackbar usando coroutines
                LaunchedEffect(key1 = showSnackbar) {
                    if (showSnackbar) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Datos guardados con éxito",
                            actionLabel = "OK"
                        )
                        showSnackbar = false
                    }
                }

            }
        }
    }

}

@Composable
fun CampoTexto(valor: String, onValueChange: (String) -> Unit, etiqueta: String) {
    TextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        modifier = Modifier.width(380.dp)
    )
}
@Composable
fun MenuDesplegable(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    opciones: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Box {
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
            modifier = Modifier.width(380.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
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
fun BotonSiguiente(onClick:  () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(50.dp)
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
            fontSize = 30.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CasanareStereoTheme() {
    FormularioScreen(viewModel = FormularioViewModel(), navController = rememberNavController())
}
