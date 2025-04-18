package com.jcmateus.casanarestereo.screens.formulario

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class FormularioViewModel : ViewModel() {
    private val _formularioData = mutableStateListOf<Pair<String, Any>>()
    val _formularioGuardado = mutableStateOf(false)
    val formularioGuardado = _formularioGuardado

    fun guardarFormularioEnFirebase(tipoFormulario: String, param: (Any) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val formulariosRef = database.reference.child("formularios")
        val nuevoFormularioRef = formulariosRef.push()

        val formularioMap = _formularioData.toMap().toMutableMap()
        formularioMap["tipo"] = tipoFormulario

        nuevoFormularioRef.setValue(formularioMap)
            .addOnSuccessListener {
                viewModelScope.launch { // Lanzar una coroutine en el viewModelScope
                    _formularioGuardado.value = true
                    param(true) // Llamar al callback en el hilo principal
                }
            }
            .addOnFailureListener {
                viewModelScope.launch { // Lanzar una coroutine en el viewModelScopete
                    param(false) // Llamar al callback en el hilo principal
                }
            }
    }

    // Otras funciones auxiliares que podrías necesitar

    fun agregarDatosFormulario(datos: Pair<String, String>) {
        _formularioData.add(datos)
    }

    // ... otras funciones para manejar la lógica del formulario
}