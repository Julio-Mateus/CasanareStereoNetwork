package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import android.net.Uri
import android.util.Log
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

class NoticiaViewModel(
    private val repository: NoticiaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _noticiaUiState = MutableStateFlow<NoticiaUiState>(NoticiaUiState.Loading)
    val noticiaUiState: StateFlow<NoticiaUiState> = _noticiaUiState.asStateFlow()

    private val _noticiaGuardada = MutableStateFlow(false)
    val noticiaGuardada: StateFlow<Boolean> = _noticiaGuardada.asStateFlow()

    private val _errorGuardandoNoticia = MutableStateFlow<String?>(null)
    val errorGuardandoNoticia: StateFlow<String?> = _errorGuardandoNoticia.asStateFlow()

    // StateFlow for the noticia image URI
    private val _imagenNoticiaUri = MutableStateFlow<Uri?>(null)
    val imagenNoticiaUri: StateFlow<Uri?> = _imagenNoticiaUri.asStateFlow()

    // StateFlow for the list of noticias
    private val _listaNoticias = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticias: StateFlow<List<Contenido.Noticia>> = _listaNoticias.asStateFlow()

    // StateFlow for errorEliminandoNoticia
    private val _errorEliminandoNoticia = MutableStateFlow<String?>(null) // Cambiado a String?
    val errorEliminandoNoticia: StateFlow<String?> = _errorEliminandoNoticia

    // StateFlow for the current noticia
    private val _noticia = MutableStateFlow<Contenido.Noticia?>(null)
    val noticia: StateFlow<Contenido.Noticia?> = _noticia.asStateFlow()

    // StateFlow for the list of noticias filtered by category
    private val _listaNoticiasFiltradas = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasFiltradas: StateFlow<List<Contenido.Noticia>> = _listaNoticiasFiltradas.asStateFlow()

    fun guardarNoticia(noticia: Contenido.Noticia, userId: String) {
        _noticiaUiState.value = NoticiaUiState.Loading
        viewModelScope.launch {
            try {
                val emisoraId = userId // Assuming userId is the emisoraId
                db.collection("emisoras")
                    .document(emisoraId)
                    .collection("noticias") // Store in "noticias" subcollection
                    .add(noticia)
                    .addOnSuccessListener {
                        _noticiaGuardada.value = true
                        _noticiaUiState.value = NoticiaUiState.Success
                    }
                    .addOnFailureListener { e ->
                        _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al guardar noticia")
                        _errorGuardandoNoticia.value = e.message
                    }
            } catch (e: Exception) {
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al guardar noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun restablecerNoticiaGuardada() {
        _noticiaGuardada.value = false
        _errorGuardandoNoticia.value = null
    }

    fun obtenerNoticias(userId: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias = repository.obtenerNoticias(userId)
                _listaNoticias.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al obtener noticias")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticiasPorCategoria(userId: String, categoria: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias = repository.obtenerNoticiasPorCategoria(userId, categoria)
                _listaNoticiasFiltradas.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al obtener noticias por categoría")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticia(noticiaId: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticia = repository.obtenerNoticia(noticiaId)
                _noticia.value = noticia
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al obtener noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun eliminarNoticia(noticiaId: String, userId: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                repository.eliminarNoticia(noticiaId, userId)
                _errorEliminandoNoticia.value = null // Se asigna null cuando se elimina correctamente
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _errorEliminandoNoticia.value = e.message ?: "Error al eliminar noticia" // Se asigna el error
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al eliminar noticia")
            }
        }
    }

    fun actualizarNoticia(noticiaId: String, noticia: Contenido.Noticia) {
        viewModelScope.launch {
            try {
                repository.actualizarNoticia(noticiaId, noticia)
            } catch (e: Exception) {
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error al actualizar noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    // Function to update the noticia image URI
    fun actualizarImagenNoticia(uri: Uri) {
        _imagenNoticiaUri.value = uri
    }

    fun restablecerErrorEliminandoNoticia() {
        _errorEliminandoNoticia.value = false.toString()
    }

    fun restablecerErrorGuardandoNoticia() {
        _errorGuardandoNoticia.value = null
    }
}

// Sealed class for Noticia UI states
sealed class NoticiaUiState {
    object Loading : NoticiaUiState()
    object Success : NoticiaUiState()
    data class Error(val message: String) : NoticiaUiState()
}