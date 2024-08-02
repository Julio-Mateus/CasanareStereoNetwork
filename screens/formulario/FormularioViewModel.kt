package com.jcmateus.casanarestereo.screens.formulario

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class FormularioViewModel : ViewModel() {
    private val _formularioData = mutableStateListOf<Pair<String, Any>>()
    val formularioData: List<Pair<String, Any>> = _formularioData

    fun guardarFormularioEnFirebase(tipoFormulario: String, param: (Any) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val formulariosRef = database.reference.child("formularios")
        val nuevoFormularioRef = formulariosRef.push()

        val formularioMap = _formularioData.toMap().toMutableMap()
        formularioMap["tipo"] = tipoFormulario

        nuevoFormularioRef.setValue(formularioMap)
            .addOnSuccessListener {
                // Mostrar mensaje de éxito al usuario (Snackbar o Toast)
                _formularioData.clear() // Limpiar el HashMap después de guardar
            }
            .addOnFailureListener {
                // Mostrar mensaje de error al usuario (Snackbar o Toast)
                // Registrar el error (opcional)
            }
    }

    // Otras funciones auxiliares que podrías necesitar

    fun limpiarFormulario() {
        _formularioData.clear()
    }
    fun agregarDatosFormulario(datos: Pair<String, String>) {
        _formularioData.add(datos)
    }

    // ... otras funciones para manejar la lógica del formulario
}