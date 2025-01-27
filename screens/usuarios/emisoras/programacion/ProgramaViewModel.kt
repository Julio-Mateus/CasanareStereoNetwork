package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import android.net.Uri
import androidx.compose.foundation.layout.add
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProgramaUiState {
    data object Loading : ProgramaUiState()
    data object Success : ProgramaUiState()
    data class Error(val message: String) : ProgramaUiState()
}

class ProgramaViewModel(
    private val repository: ProgramaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _programaUiState = MutableStateFlow<ProgramaUiState>(ProgramaUiState.Loading)
    val programaUiState: StateFlow<ProgramaUiState> = _programaUiState.asStateFlow()

    private val _programaGuardado = MutableStateFlow(false)
    val programaGuardado: StateFlow<Boolean> = _programaGuardado.asStateFlow()

    private val _errorGuardandoPrograma = MutableStateFlow<String?>(null)
    val errorGuardandoPrograma: StateFlow<String?> = _errorGuardandoPrograma.asStateFlow()

    // New StateFlow for the profile image URI
    private val _imagenProgramaUri = MutableStateFlow<Uri?>(null)
    val imagenProgramaUri: StateFlow<Uri?> = _imagenProgramaUri.asStateFlow()

    // New StateFlow for the list of programs
    private val _listaProgramas = MutableStateFlow<List<Contenido.Programa>>(emptyList())
    val listaProgramas: StateFlow<List<Contenido.Programa>> = _listaProgramas.asStateFlow()


    // New StateFlow for errorEliminandoPrograma
    private val _errorEliminandoPrograma = MutableStateFlow<Boolean>(false)
    val errorEliminandoPrograma: StateFlow<Boolean> = _errorEliminandoPrograma.asStateFlow()

    // New StateFlow for the current program
    private val _programa = MutableStateFlow<Contenido.Programa?>(null)
    val programa: StateFlow<Contenido.Programa?> = _programa.asStateFlow()

    fun guardarPrograma(programa: Contenido.Programa, userId: String) {
        _programaUiState.value = ProgramaUiState.Loading
        viewModelScope.launch {
            try {
                val emisoraId = userId // Assuming userId is the emisoraId
                db.collection("emisoras")
                    .document(emisoraId)
                    .collection("programas") // Store in "programas" subcollection
                    .add(programa)
                    .addOnSuccessListener {
                        _programaGuardado.value = true
                        _programaUiState.value = ProgramaUiState.Success
                    }
                    .addOnFailureListener { e ->
                        _programaUiState.value = ProgramaUiState.Error(e.message ?: "Error al guardar programa")
                        _errorGuardandoPrograma.value = e.message
                    }
            } catch (e: Exception) {
                _programaUiState.value = ProgramaUiState.Error(e.message ?: "Error al guardar programa")
                _errorGuardandoPrograma.value = e.message
            }
        }
    }

    fun restablecerProgramaGuardado() {
        _programaGuardado.value = false
        _errorGuardandoPrograma.value = null
    }

    fun obtenerProgramas(userId: String) {
        viewModelScope.launch {
            _programaUiState.value = ProgramaUiState.Loading
            try {
                val programas = repository.obtenerProgramas(userId)
                _listaProgramas.value = programas
                _programaUiState.value = ProgramaUiState.Success
            } catch (e: Exception) {
                _programaUiState.value = ProgramaUiState.Error(e.message ?: "Error al obtener programas")
                _errorGuardandoPrograma.value = e.message
            }
        }
    }

    fun obtenerPrograma(programaId: String) {
        viewModelScope.launch {
            _programaUiState.value = ProgramaUiState.Loading
            try {
                val programa = repository.obtenerPrograma(programaId)
                _programa.value = programa
                _programaUiState.value = ProgramaUiState.Success
            } catch (e: Exception) {
                _programaUiState.value = ProgramaUiState.Error(e.message ?: "Error al obtener programa")
                _errorGuardandoPrograma.value = e.message
            }
        }
    }

    fun eliminarPrograma(programa: Contenido.Programa, userId: String) {
        viewModelScope.launch {
            _programaUiState.value = ProgramaUiState.Loading
            try {
                repository.eliminarPrograma(programa.id)
                _errorEliminandoPrograma.value = false
                _programaUiState.value = ProgramaUiState.Success
            } catch (e: Exception) {
                _errorEliminandoPrograma.value = true
                _programaUiState.value = ProgramaUiState.Error(e.message ?: "Error al eliminar programa")
                _errorGuardandoPrograma.value = e.message
            }
        }
    }

    fun actualizarPrograma(programaId: String, programa: Contenido.Programa) {
        viewModelScope.launch {
            try {
                repository.actualizarPrograma(programaId, programa)
            } catch (e: Exception) {
                _programaUiState.value = ProgramaUiState.Error(e.message ?: "Error al actualizar programa")
                _errorGuardandoPrograma.value = e.message
            }
        }
    }

    // New function to update the profile image URI
    fun actualizarImagenPrograma(uri: Uri) {
        _imagenProgramaUri.value = uri
    }

    fun restablecerErrorEliminandoPrograma() {
        _errorEliminandoPrograma.value = false
    }

    fun restablecerErrorGuardandoPrograma() {
        _errorGuardandoPrograma.value = null
    }
}