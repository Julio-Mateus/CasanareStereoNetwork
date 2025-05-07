package com.jcmateus.casanarestereo.screens.formulario

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class FormularioViewModel : ViewModel() {
    // Estados para los campos del formulario de Estudiantes
    var edad = mutableStateOf("")
        private set
    var institucion = mutableStateOf("")
        private set
    var municipioEstudiante = mutableStateOf("")
        private set
    var genero = mutableStateOf("")
        private set
    var grado = mutableStateOf("")
        private set
    var impacto = mutableStateOf("")
        private set
    var promocion = mutableStateOf("")
        private set
    var asignaturas = mutableStateOf<List<String>>(emptyList())
        private set
    var participacion = mutableStateOf("")
        private set
    var interes = mutableStateOf("")
        private set
    var habilidades = mutableStateOf("")
        private set
    var impactoTerritorio = mutableStateOf("")
        private set
    var conoceComunicacion = mutableStateOf("")
        private set
    var interesaCrearContenido = mutableStateOf("")
        private set
    var calidadInformacion = mutableStateOf("")
        private set
    var conocimientoDivulgacion = mutableStateOf("")
        private set
    var mediosDigitales = mutableStateOf("")
        private set
    var sigueCientificos = mutableStateOf("")
        private set
    var interesaCarrera = mutableStateOf("")
        private set
    var queEstudiar = mutableStateOf("")
        private set

    // Estados para los campos del formulario de Docentes
    var nombreDocente = mutableStateOf("")
        private set
    var telefonoDocente = mutableStateOf("")
        private set
    var correoDocente = mutableStateOf("")
        private set
    var politicaDatos = mutableStateOf("")
        private set
    var colegio = mutableStateOf("")
        private set
    var materia = mutableStateOf("")
        private set
    var municipioDocente = mutableStateOf("")
        private set
    var selectedOptionConsidera = mutableStateOf("")
        private set
    var selectedOptionConsidera1 = mutableStateOf("")
        private set
    var selectedOptionVocaciones = mutableStateOf("")
        private set
    var selectedOptionFormacion = mutableStateOf("")
        private set
    var selectedOptionDesarrollo = mutableStateOf("")
        private set

    // Lista para almacenar los datos del formulario (como antes)
    private val _formularioData = mutableStateListOf<Pair<String, Any>>()
    val _formularioGuardado = mutableStateOf(false)
    val formularioGuardado = _formularioGuardado

    fun guardarFormularioEnFirebase(tipoFormulario: String, param: (Any) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val formulariosRef = database.reference.child("formularios")
        val nuevoFormularioRef = formulariosRef.push()

        // Clear the list before saving
        val formularioMap = _formularioData.toMap().toMutableMap()
        formularioMap["tipo"] = tipoFormulario

        nuevoFormularioRef.setValue(formularioMap)
            .addOnSuccessListener {
                viewModelScope.launch { // Lanzar una coroutine en el viewModelScope
                    _formularioGuardado.value = true
                    param(true) // Llamar al callback en el hilo principal
                    clearFormularioData() // Limpiar los datos del formulario
                    if (tipoFormulario == "docente") {
                        clearDocenteData() // Limpiar los datos de docente
                    } else {
                        clearEstudianteData() // Limpiar los datos de estudiante
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch { // Lanzar una coroutine en el viewModelScopete
                    param(false) // Llamar al callback en el hilo principal
                    clearFormularioData() // Limpiar los datos del formulario
                    if (tipoFormulario == "docente") {
                        clearDocenteData() // Limpiar los datos de docente
                    } else {
                        clearEstudianteData() // Limpiar los datos de estudiante
                    }
                }
            }
    }

    fun agregarDatosFormulario(datos: Pair<String, String>) {
        // Actualizar las variables de estado
        when (datos.first) {
            //Estudiantes
            "edad" -> edad.value = datos.second
            "institucion" -> institucion.value = datos.second
            "municipio" -> municipioEstudiante.value = datos.second
            "genero" -> genero.value = datos.second
            "grado" -> grado.value = datos.second
            "impacto" -> impacto.value = datos.second
            "promocion" -> promocion.value = datos.second
            "asignaturas" -> asignaturas.value = datos.second.split(",").map { it.trim() }
            "participacion" -> participacion.value = datos.second
            "interes" -> interes.value = datos.second
            "habilidades" -> habilidades.value = datos.second
            "impactoTerritorio" -> impactoTerritorio.value = datos.second
            "conoceComunicacion" -> conoceComunicacion.value = datos.second
            "interesaCrearContenido" -> interesaCrearContenido.value = datos.second
            "calidadInformacion" -> calidadInformacion.value = datos.second
            "conocimientoDivulgacion" -> conocimientoDivulgacion.value = datos.second
            "mediosDigitales" -> mediosDigitales.value = datos.second
            "sigueCientificos" -> sigueCientificos.value = datos.second
            "interesaCarrera" -> interesaCarrera.value = datos.second
            "queEstudiar" -> queEstudiar.value = datos.second
            //Docentes
            "nombreDocente" -> nombreDocente.value = datos.second
            "telefonoDocente" -> telefonoDocente.value = datos.second
            "correoDocente" -> correoDocente.value = datos.second
            "politicaDatos" -> politicaDatos.value = datos.second
            "colegio" -> colegio.value = datos.second
            "materia" -> materia.value = datos.second
            "municipioDocente" -> municipioDocente.value = datos.second
            "interesCTeI" -> selectedOptionConsidera.value = datos.second
            "desarrollosCTeI" -> selectedOptionConsidera1.value = datos.second
            "programaAcademico" -> selectedOptionVocaciones.value = datos.second
            "cursosHerramientas" -> selectedOptionFormacion.value = datos.second
            "areasDesarrollo" -> selectedOptionDesarrollo.value = datos.second
        }
        // Agregar a la lista (como antes)
        _formularioData.add(datos)
    }

    fun clearFormularioData() {
        _formularioData.clear()
    }

    fun clearEstudianteData() {
        edad.value = ""
        institucion.value = ""
        municipioEstudiante.value = ""
        genero.value = ""
        grado.value = ""
        impacto.value = ""
        promocion.value = ""
        asignaturas.value = emptyList()
        participacion.value = ""
        interes.value = ""
        habilidades.value = ""
        impactoTerritorio.value = ""
        conoceComunicacion.value = ""
        interesaCrearContenido.value = ""
        calidadInformacion.value = ""
        conocimientoDivulgacion.value = ""
        mediosDigitales.value = ""
        sigueCientificos.value = ""
        interesaCarrera.value = ""
        queEstudiar.value = ""
    }

    fun clearDocenteData() {
        nombreDocente.value = ""
        telefonoDocente.value = ""
        correoDocente.value = ""
        politicaDatos.value = ""
        colegio.value = ""
        materia.value = ""
        municipioDocente.value = ""
        selectedOptionConsidera.value = ""
        selectedOptionConsidera1.value = ""
        selectedOptionVocaciones.value = ""
        selectedOptionFormacion.value = ""
        selectedOptionDesarrollo.value = ""
    }
}