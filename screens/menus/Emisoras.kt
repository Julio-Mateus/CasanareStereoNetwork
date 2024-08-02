package com.jcmateus.casanarestereo.screens.menus

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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Emisoras(){
    val navController = rememberNavController()

    val pagerState = rememberPagerState(pageCount = { 6 })
    val coroutineScope = rememberCoroutineScope()
    var showFirstViewButtons by remember { mutableStateOf(true) }

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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Formulario de Percepción ACPC Estudiantes",
                        modifier = Modifier
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center

                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Estudiantes()
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .padding(50.dp)
                            .width(300.dp)
                            .height(50.dp),
                        //.absoluteOffset(0.dp, 650.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            text = "Siguiente",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 30.sp

                        )
                    }
                }
            }

            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Formulario de Percepción ACPC Estudiantes",
                        modifier = Modifier
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center

                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Estudiantes1()
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .padding(50.dp)
                            .width(300.dp)
                            .height(50.dp),
                        //.absoluteOffset(0.dp, 650.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            text = "Siguiente",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 30.sp

                        )
                    }
                }
            }

            3 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Formulario de Percepción ACPC Estudiantes",
                        modifier = Modifier
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center

                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Estudiantes2()
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .padding(50.dp)
                            .width(300.dp)
                            .height(50.dp),
                        //.absoluteOffset(0.dp, 650.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            text = "Siguiente",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 30.sp

                        )
                    }

                }
            }

            4 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Formulario de Percepción ACPC Estudiantes",
                        modifier = Modifier
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center

                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Estudiantes3()
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .padding(50.dp)
                            .width(300.dp)
                            .height(50.dp),
                        //.absoluteOffset(0.dp, 650.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            text = "Siguiente",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 30.sp

                        )
                    }

                }
            }

            5 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Formulario de Percepción ACPC Estudiantes",
                        modifier = Modifier
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center

                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Docentes()
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .padding(50.dp)
                            .width(300.dp)
                            .height(50.dp),
                        //.absoluteOffset(0.dp, 650.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text(
                            text = "Siguiente",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 30.sp

                        )
                    }

                }
            }
        }
    }

}
@Composable
fun Estudiantes() {
    var edad by remember { mutableStateOf("") }
    var institucion by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    val opcionesGenero = listOf("Masculino", "Femenino", "Otro")
    var selectedOptionGenero by remember { mutableStateOf("") }
    var expandedGenero by remember { mutableStateOf(false) }
    val opcionesGrado = listOf("Sexto", "Séptimo", "Octavo", "Noveno", "Decimo", "Once")
    var selectedOptionGrado by remember { mutableStateOf("") }
    var expandedGrado by remember { mutableStateOf(false) }
    // val rotationAngle by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    var textFieldSizeGrado by remember { mutableStateOf(IntSize.Zero) }


    TextField(
        value = edad,
        onValueChange = { edad = it },
        label = { Text("Edad") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(30.dp))
    Box {
        TextField(
            value = selectedOptionGenero,
            onValueChange = { selectedOptionGenero = it },
            label = { Text("Genero") },
            trailingIcon = {
                IconButton(onClick = { expandedGenero = !expandedGenero })
                {
                    Icon(
                        imageVector = if (expandedGenero)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true, // Para evitar que el usuario escriba directamente
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSize = it }
                .padding(bottom = 8.dp) // Agregar margen inferior

        )
        DropdownMenu(
            expanded = expandedGenero,
            onDismissRequest = { expandedGenero = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesGenero.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionGenero = option
                    expandedGenero = false
                }) {
                    Text(text = option)
                }
            }
        }

    }
    Spacer(modifier = Modifier.padding(30.dp))
    TextField(
        value = institucion,
        onValueChange = { institucion = it },
        label = { Text("Institución Educativa") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(30.dp))
    Box {
        TextField(
            value = selectedOptionGrado,
            onValueChange = { selectedOptionGrado = it },
            label = { Text("Grado Escolar") },
            trailingIcon = {
                IconButton(onClick = { expandedGrado = !expandedGrado })
                {
                    Icon(
                        imageVector = if (expandedGrado)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true, // Para evitar que el usuario escriba directamente
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeGrado = it }
                .padding(bottom = 8.dp) // Agregar margen inferior

        )
        DropdownMenu(
            expanded = expandedGrado,
            onDismissRequest = { expandedGrado = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesGrado.forEach { option1 ->
                DropdownMenuItem(onClick = {
                    selectedOptionGrado = option1
                    expandedGrado = false
                }) {
                    Text(text = option1)
                }
            }
        }

    }
    Spacer(modifier = Modifier.padding(30.dp))
    TextField(
        value = municipio,
        onValueChange = { municipio = it },
        label = { Text("Municipio") },
        modifier = Modifier
            .width(380.dp)
    )
}

@Composable
fun Estudiantes1() {
    val opcionesPromocion = listOf("Si", "No")
    var selectedOptionPromocion by remember { mutableStateOf("") }
    var expandedPromocion by remember { mutableStateOf(false) }
    var textFieldSizePromocion by remember { mutableStateOf(IntSize.Zero) }
    val opcionesParticipacion = listOf("Si", "No")
    var selectedOptionParticipacion by remember { mutableStateOf("") }
    var expandedParticipacion by remember { mutableStateOf(false) }
    var textFieldSizeParticipacion by remember { mutableStateOf(IntSize.Zero) }
    var interes by remember { mutableStateOf("") }
    val opcionesImpacto = listOf("Si", "No")
    var selectedOptionImpacto by remember { mutableStateOf("") }
    var expandedImpacto by remember { mutableStateOf(false) }
    var textFieldSizeImpacto by remember { mutableStateOf(IntSize.Zero) }
    var genero by remember { mutableStateOf("") }
    val opcionesAsignaturas = listOf(
        "Química",
        "Física",
        "Biología",
        "Naturales",
        "Informática",
        "Sociales",
        "Español",
        "Matemáticas",
        "Filosofía",
        "Otra _ cual?",
        "Ninguna"
    )
    var selectedOptionAsignaturas by remember { mutableStateOf("") }
    var expandedAsignaturas by remember { mutableStateOf(false) }
    var textFieldSizeAsignatura by remember { mutableStateOf(IntSize.Zero) }

    Box {
        TextField(
            value = selectedOptionImpacto,
            onValueChange = { selectedOptionImpacto = it },
            label = { Text("¿Conoce la diferencia entre ciencia, tecnología e innovación? ") },
            trailingIcon = {
                IconButton(onClick = { expandedImpacto = !expandedImpacto })
                {
                    Icon(
                        imageVector = if (expandedImpacto)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeImpacto = it }
        )
        DropdownMenu(
            expanded = expandedImpacto,
            onDismissRequest = { expandedImpacto = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesImpacto.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionImpacto = option
                    expandedImpacto = false
                }) {
                    Text(text = option)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionPromocion,
            onValueChange = { selectedOptionPromocion = it },
            label = { Text("¿Usted considera que el colegio promueve el desarrollo de conocimiento científico?") },
            trailingIcon = {
                IconButton(onClick = { expandedPromocion = !expandedPromocion })
                {
                    Icon(
                        imageVector = if (expandedPromocion)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizePromocion = it }
        )
        DropdownMenu(
            expanded = expandedPromocion,
            onDismissRequest = { expandedPromocion = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesPromocion.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionPromocion = option
                    expandedPromocion = false
                }) {
                    Text(text = option)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionAsignaturas,
            onValueChange = { selectedOptionAsignaturas = it },
            label = { Text("¿Qué asignaturas relacionadas con la ciencia y tecnología estás cursando actualmente?") },
            trailingIcon = {
                IconButton(onClick = { expandedAsignaturas = !expandedAsignaturas })
                {
                    Icon(
                        imageVector = if (expandedAsignaturas)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeAsignatura = it }
        )
        DropdownMenu(
            expanded = expandedAsignaturas,
            onDismissRequest = { expandedAsignaturas = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesAsignaturas.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionAsignaturas = option
                    expandedAsignaturas = false
                }) {
                    Text(text = option)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionParticipacion,
            onValueChange = { selectedOptionParticipacion = it },
            label = { Text("¿Has participado en alguna actividad de innovación, ciencia, o creación tecnológica, como hackathons, ferias de ciencias, etc.?") },
            trailingIcon = {
                IconButton(onClick = { expandedParticipacion = !expandedParticipacion })
                {
                    Icon(
                        imageVector = if (expandedParticipacion)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeParticipacion = it }
        )
        DropdownMenu(
            expanded = expandedParticipacion,
            onDismissRequest = { expandedParticipacion = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesParticipacion.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionParticipacion = option
                    expandedParticipacion = false
                }) {
                    Text(text = option)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = interes,
        onValueChange = { interes = it },
        label = { Text("En una escala del 1 al 7, siendo 1 el menor y 7 el máximo ¿Cuánto interés tienes en temas de ciencia y la tecnología?") },
        modifier = Modifier
            .width(380.dp)
    )
}

@Composable
fun Estudiantes2() {
    val opcionesHabilidades = listOf("Si", "No")
    var selectedOptionHabilidades by remember { mutableStateOf("") }
    var expandedHabilidades by remember { mutableStateOf(false) }
    var textFieldSizeHabilidades by remember { mutableStateOf(IntSize.Zero) }
    var impacto by remember { mutableStateOf("") }
    val opcionesVideo3 = listOf("Si", "No")
    var selectedOptionVideo3 by remember { mutableStateOf("") }
    var expandedVideo3 by remember { mutableStateOf(false) }
    var textFieldSizeVideo3 by remember { mutableStateOf(IntSize.Zero) }
    val opcionesImVideo3 = listOf("Si", "No")
    var selectedOptionImVideo3 by remember { mutableStateOf("") }
    var expandedImVideo3 by remember { mutableStateOf(false) }
    var textFieldSizeImVideo3 by remember { mutableStateOf(IntSize.Zero) }
    var impactoVideo3 by remember { mutableStateOf("") }


    Box {
        TextField(
            value = selectedOptionHabilidades,
            onValueChange = { selectedOptionHabilidades = it },
            label = { Text("¿Crees que la educación que recibes en ciencia y tecnología te aporta herramientas y habilidades suficientes para tu formación a futuro?") },
            trailingIcon = {
                IconButton(onClick = { expandedHabilidades = !expandedHabilidades })
                {
                    Icon(
                        imageVector = if (expandedHabilidades)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeHabilidades = it }
        )
        DropdownMenu(
            expanded = expandedHabilidades,
            onDismissRequest = { expandedHabilidades = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesHabilidades.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionHabilidades = option
                    expandedHabilidades = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = impacto,
        onValueChange = { impacto = it },
        label = { Text("¿Qué tipos de ciencia y tecnología identifica en su territorio?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionVideo3,
            onValueChange = { selectedOptionVideo3 = it },
            label = { Text("¿Conoces formas de comunicación de ciencia?") },
            trailingIcon = {
                IconButton(onClick = { expandedVideo3 = !expandedVideo3 })
                {
                    Icon(
                        imageVector = if (expandedVideo3)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeVideo3 = it }
        )
        DropdownMenu(
            expanded = expandedVideo3,
            onDismissRequest = { expandedVideo3 = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesVideo3.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionVideo3 = option
                    expandedVideo3 = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionImVideo3,
            onValueChange = { selectedOptionImVideo3 = it },
            label = { Text("¿Te interesaría dedicarte a crear contenidos para divulgar temas de ciencia?") },
            trailingIcon = {
                IconButton(onClick = { expandedImVideo3 = !expandedImVideo3 })
                {
                    Icon(
                        imageVector = if (expandedImVideo3)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeImVideo3 = it }
        )
        DropdownMenu(
            expanded = expandedImVideo3,
            onDismissRequest = { expandedImVideo3 = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesImVideo3.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionImVideo3 = option
                    expandedImVideo3 = false
                }) {
                    Text(text = option)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = impactoVideo3,
        onValueChange = { impactoVideo3 = it },
        label = { Text("En una escala del 1 al 7, siendo 1 el mala y 7 el excelente ¿Qué opinas sobre la calidad de la información científica y tecnológica disponible en redes sociales?") },
        modifier = Modifier
            .width(380.dp)
    )
}

@Composable
fun Estudiantes3() {
    var text by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    val opcionesMedios = listOf(
        "Redes sociales",
        "Plataformas de videos",
        "Plataformas de consulta",
        "Medios de comunicación digitales",
        "Blogs",
        "Podcast"
    )
    var selectedOptionMedios by remember { mutableStateOf("") }
    var expandedMedios by remember { mutableStateOf(false) }
    var textFieldSizeMedios by remember { mutableStateOf(IntSize.Zero) }
    val opcionesGustos = listOf(
        "Ciencias sociales",
        "Ingenierías",
        "Ciencias económicas y administrativas",
        "Ciencias del espacio",
        "Ciencias básicas (Física, química, biología, matemáticas",
        "Ciencias de la salud",
        "Ciencias agropecuarias"
    )
    var selectedOptionGustos by remember { mutableStateOf("") }
    var expandedGustos by remember { mutableStateOf(false) }
    var textFieldSizeGustos by remember { mutableStateOf(IntSize.Zero) }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("¿Tienes conocimiento de como realizar una pieza divulgativa?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = selectedOptionMedios,
        onValueChange = { selectedOptionMedios = it },
        label = { Text("¿Qué medios digitales usas para informarte de CTeI?") },
        trailingIcon = {
            IconButton(onClick = { expandedMedios = !expandedMedios })
            {
                Icon(
                    imageVector = if (expandedMedios)
                        Icons.Filled.ArrowDropDown else
                        Icons.Filled.ArrowDropDown,
                    "mostrar opciones"
                )
            }
        },
        modifier = Modifier
            .width(380.dp)
            .onSizeChanged { textFieldSizeMedios = it }
    )
    DropdownMenu(
        expanded = expandedMedios,
        onDismissRequest = { expandedMedios = false },
        /*modifier = Modifier
            .offset { IntOffset(0, textFieldSize.height - 10) }
            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
    )
    {
        opcionesMedios.forEach { option ->
            DropdownMenuItem(onClick = {
                selectedOptionMedios = option
                expandedMedios = false
            }) {
                Text(text = option)
            }
        }
    }
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("¿Sigues a científicos, divulgadores científicos o influenciadores nacionales o internacionales sobre ciencia en redes sociales o plataformas de video?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("¿Te interesa seguir una carrera relacionada con la ciencia o tecnología?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = selectedOptionGustos,
        onValueChange = { selectedOptionGustos = it },
        label = { Text("¿Qué te gustaría estudiar?") },
        trailingIcon = {
            IconButton(onClick = { expandedGustos = !expandedGustos })
            {
                Icon(
                    imageVector = if (expandedGustos)
                        Icons.Filled.ArrowDropDown else
                        Icons.Filled.ArrowDropDown,
                    "mostrar opciones"
                )
            }
        },
        modifier = Modifier
            .width(380.dp)
            .onSizeChanged { textFieldSizeGustos = it }
    )
    DropdownMenu(
        expanded = expandedGustos,
        onDismissRequest = { expandedGustos = false },
        /*modifier = Modifier
            .offset { IntOffset(0, textFieldSize.height - 10) }
            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
    )
    {
        opcionesGustos.forEach { option1 ->
            DropdownMenuItem(onClick = {
                selectedOptionGustos = option1
                expandedGustos = false
            }) {
                Text(text = option1)
            }
        }
    }
}

@Composable
fun Docentes() {
    var colegio by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    val opcionesConsidera = listOf("Si", "No")
    var selectedOptionConsidera by remember { mutableStateOf("") }
    var expandedConsidera by remember { mutableStateOf(false) }
    var textFieldSizeConsidera by remember { mutableStateOf(IntSize.Zero) }
    val opcionesConsidera1 = listOf("Si", "No")
    var selectedOptionConsidera1 by remember { mutableStateOf("") }
    var expandedConsidera1 by remember { mutableStateOf(false) }
    var textFieldSizeConsidera1 by remember { mutableStateOf(IntSize.Zero) }
    val opcionesVocaciones = listOf("Si", "No")
    var selectedOptionVocaciones by remember { mutableStateOf("") }
    var expandedVocaciones by remember { mutableStateOf(false) }
    var textFieldSizeVocaciones by remember { mutableStateOf(IntSize.Zero) }
    val opcionesFormacion = listOf("Si", "No")
    var selectedOptionFormacion by remember { mutableStateOf("") }
    var expandedFormacion by remember { mutableStateOf(false) }
    var textFieldSizeFormacion by remember { mutableStateOf(IntSize.Zero) }
    val opcionesDesarrollo = listOf(" Estudios universitario", "Técnico", "Operario")
    var selectedOptionDesarrollo by remember { mutableStateOf("") }
    var expandedDesarrollo by remember { mutableStateOf(false) }
    var textFieldSizeDesarrollo by remember { mutableStateOf(IntSize.Zero) }

    TextField(
        value = colegio,
        onValueChange = { colegio = it },
        label = { Text("¿Cuál es el nombre de la institución educativa donde se implementa el proyecto?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = materia,
        onValueChange = { materia = it },
        label = { Text("¿En qué área académica se implementa el proyecto \"Casanare es científico ¿y tú?”?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    TextField(
        value = municipio,
        onValueChange = { municipio = it },
        label = { Text("¿Cuál es el municipio donde se implementa el proyecto?") },
        modifier = Modifier
            .width(380.dp)
    )
    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionConsidera,
            onValueChange = { selectedOptionConsidera = it },
            label = { Text("¿Considera que los estudiantes tiene interés en temas de CTeI?") },
            trailingIcon = {
                IconButton(onClick = { expandedConsidera = !expandedConsidera })
                {
                    Icon(
                        imageVector = if (expandedConsidera)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeConsidera = it }
        )
        DropdownMenu(
            expanded = expandedConsidera,
            onDismissRequest = { expandedConsidera = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesConsidera.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOptionConsidera = option
                    expandedConsidera = false
                }) {
                    Text(text = option)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionConsidera1,
            onValueChange = { selectedOptionConsidera1 = it },
            label = { Text("¿Considera que los estudiantes identifican los desarrollos de CTeI que tienen en su región?") },
            trailingIcon = {
                IconButton(onClick = { expandedConsidera1 = !expandedConsidera1 })
                {
                    Icon(
                        imageVector = if (expandedConsidera1)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeConsidera1 = it }
        )
        DropdownMenu(
            expanded = expandedConsidera1,
            onDismissRequest = { expandedConsidera1 = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesConsidera1.forEach { option1 ->
                DropdownMenuItem(onClick = {
                    selectedOptionConsidera1 = option1
                    expandedConsidera1 = false
                }) {
                    Text(text = option1)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionVocaciones,
            onValueChange = { selectedOptionVocaciones = it },
            label = { Text("¿La institución educativa cuenta con un programa académico para desarrollar vocaciones científicas en los jóvenes?") },
            trailingIcon = {
                IconButton(onClick = { expandedVocaciones = !expandedVocaciones })
                {
                    Icon(
                        imageVector = if (expandedVocaciones)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeVocaciones = it }
        )
        DropdownMenu(
            expanded = expandedVocaciones,
            onDismissRequest = { expandedVocaciones = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesVocaciones.forEach { option2 ->
                DropdownMenuItem(onClick = {
                    selectedOptionVocaciones = option2
                    expandedVocaciones = false
                }) {
                    Text(text = option2)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionFormacion,
            onValueChange = { selectedOptionFormacion = it },
            label = { Text("¿En la I.E se imparten cursos para enseñar a los jóvenes sobre el uso de herramientas tecnológicas para crear y divulgar contenidos?") },
            trailingIcon = {
                IconButton(onClick = { expandedFormacion = !expandedFormacion })
                {
                    Icon(
                        imageVector = if (expandedFormacion)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeFormacion = it }
        )
        DropdownMenu(
            expanded = expandedFormacion,
            onDismissRequest = { expandedFormacion = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesFormacion.forEach { option3 ->
                DropdownMenuItem(onClick = {
                    selectedOptionFormacion = option3
                    expandedFormacion = false
                }) {
                    Text(text = option3)
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(20.dp))
    Box {
        TextField(
            value = selectedOptionDesarrollo,
            onValueChange = { selectedOptionDesarrollo = it },
            label = { Text("¿Cuáles son las áreas de mayor desarrollo profesional de los estudiantes de la I.E?") },
            trailingIcon = {
                IconButton(onClick = { expandedDesarrollo = !expandedDesarrollo })
                {
                    Icon(
                        imageVector = if (expandedDesarrollo)
                            Icons.Filled.ArrowDropDown else
                            Icons.Filled.ArrowDropDown,
                        "mostrar opciones"
                    )
                }
            },
            readOnly = true,
            modifier = Modifier
                .width(380.dp)
                .onSizeChanged { textFieldSizeDesarrollo = it }
        )
        DropdownMenu(
            expanded = expandedDesarrollo,
            onDismissRequest = { expandedDesarrollo = false },
            /*modifier = Modifier
                .offset { IntOffset(0, textFieldSize.height - 10) }
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })*/
        )
        {
            opcionesDesarrollo.forEach { option4 ->
                DropdownMenuItem(onClick = {
                    selectedOptionDesarrollo = option4
                    expandedDesarrollo = false
                }) {
                    Text(text = option4)
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun CasanareStereoTheme() {
    Emisoras()
}