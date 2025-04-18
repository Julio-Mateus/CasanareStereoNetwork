package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.add
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.Rol
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.io.path.exists

class NoticiaViewModel(
    private val repository: NoticiaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val authService: AuthService
) : ViewModel() {


    private val _noticiaUiState = MutableStateFlow<NoticiaUiState>(NoticiaUiState.Loading)
    val noticiaUiState: StateFlow<NoticiaUiState> = _noticiaUiState.asStateFlow()

    private val _errorGuardandoNoticia = MutableStateFlow<String?>(null)
    val errorGuardandoNoticia: StateFlow<String?> = _errorGuardandoNoticia.asStateFlow()

    // StateFlow for the noticia image URI
    private val _imagenNoticiaUri = MutableStateFlow<Uri?>(null)
    val imagenNoticiaUri: StateFlow<Uri?> = _imagenNoticiaUri.asStateFlow()

    // StateFlow for the list of noticias
    private val _listaNoticias = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticias: StateFlow<List<Contenido.Noticia>> = _listaNoticias.asStateFlow()

    private val _noticiaCargada = MutableStateFlow<Contenido.Noticia?>(null)
    val noticiaCargada: StateFlow<Contenido.Noticia?> = _noticiaCargada.asStateFlow()

    // StateFlow for errorEliminandoNoticia
    private val _errorEliminandoNoticia = MutableStateFlow<String?>(null)
    val errorEliminandoNoticia: StateFlow<String?> = _errorEliminandoNoticia

    // StateFlow for the current noticia
    private val _noticia = MutableStateFlow<Contenido.Noticia?>(null)
    val noticia: StateFlow<Contenido.Noticia?> = _noticia.asStateFlow()

    // StateFlow for the list of noticias filtered by category
    private val _listaNoticiasFiltradas = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasFiltradas: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasFiltradas.asStateFlow()

    // StateFlow for the list of noticias internacionales
    private val _listaNoticiasInternacionales =
        MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasInternacionales: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasInternacionales.asStateFlow()

    // StateFlow for the list of noticias nacionales
    private val _listaNoticiasNacionales = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasNacionales: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasNacionales.asStateFlow()

    // StateFlow for the list of noticias regionales
    private val _listaNoticiasRegionales = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasRegionales: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasRegionales.asStateFlow()


    fun guardarNoticia(noticia: Contenido.Noticia) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                // Obtener el userId del usuario autenticado
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                // Obtener el emisoraId del usuario autenticado
                val emisoraId = repository.obtenerEmisoraId(userId)
                // Generar un ID único para la noticia
                val noticiaId = UUID.randomUUID().toString()
                // Crear una nueva noticia con el ID generado
                val nuevaNoticia = noticia.copy(id = noticiaId)
                // Guardar la noticia en la subcolección noticias dentro del documento de la emisora
                repository.guardarNoticia(emisoraId, nuevaNoticia)
                _noticiaUiState.value = NoticiaUiState.Success
                _noticiaCargada.value = nuevaNoticia
            } catch (e: Exception) {
                _noticiaUiState.value = NoticiaUiState.Error(e.message ?: "Error desconocido")
                _errorGuardandoNoticia.value = e.message ?: "Error desconocido"
            }
        }
    }
    fun obtenerNoticias(userId: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias = repository.obtenerNoticias(userId) // Usamos userId
                _listaNoticias.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticias")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticiasPorCategoria(userId: String, categoria: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias =
                    repository.obtenerNoticiasPorCategoria(userId, categoria) // Usamos userId
                _listaNoticiasFiltradas.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticias por categoría")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticia(noticiaId: String, userId: String) { // Usamos userId
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val rol = authService.obtenerRolUsuario(userId)
                    if (rol == Rol.EMISORA) {
                        val noticia = repository.obtenerNoticia(noticiaId, userId) // Usamos userId
                        _noticia.value = noticia
                        _noticiaCargada.value = noticia
                        _noticiaUiState.value = NoticiaUiState.Success
                    } else {
                        _errorGuardandoNoticia.value = "El usuario no tiene el rol EMISORA."
                        _noticiaUiState.value =
                            NoticiaUiState.Error("El usuario no tiene el rol EMISORA.")
                    }
                } else {
                    _errorGuardandoNoticia.value = "No hay un usuario autenticado."
                    _noticiaUiState.value = NoticiaUiState.Error("No hay un usuario autenticado.")
                }
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun eliminarNoticia(noticiaId: String, userId: String) { // Usamos userId
        _noticiaUiState.value = NoticiaUiState.Loading
        viewModelScope.launch {
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val rol = authService.obtenerRolUsuario(userId)
                    if (rol == Rol.EMISORA) {
                        repository.eliminarNoticia(noticiaId, userId) // Usamos userId
                        _errorEliminandoNoticia.value = null
                        _noticiaUiState.value = NoticiaUiState.Success
                    } else {
                        _errorEliminandoNoticia.value = "El usuario no tiene el rol EMISORA."
                        _noticiaUiState.value =
                            NoticiaUiState.Error("El usuario no tiene el rol EMISORA.")
                    }
                } else {
                    _errorEliminandoNoticia.value = "No hay un usuario autenticado."
                    _noticiaUiState.value = NoticiaUiState.Error("No hay un usuario autenticado.")
                }
            } catch (e: Exception) {
                _errorEliminandoNoticia.value =
                    e.message ?: "Error al eliminar noticia"
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al eliminar noticia")
            }
        }
    }

    fun actualizarNoticia(
        noticiaId: String,
        noticia: Contenido.Noticia,
        userId: String
    ) { // Usamos userId
        _noticiaUiState.value = NoticiaUiState.Loading
        viewModelScope.launch {
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val rol = authService.obtenerRolUsuario(userId)
                    if (rol == Rol.EMISORA) {
                        repository.actualizarNoticia(noticiaId, noticia, userId) // Usamos userId
                        obtenerNoticia(noticiaId, userId) // Usamos userId
                    } else {
                        _errorGuardandoNoticia.value = "El usuario no tiene el rol EMISORA."
                        _noticiaUiState.value =
                            NoticiaUiState.Error("El usuario no tiene el rol EMISORA.")
                    }
                } else {
                    _errorGuardandoNoticia.value = "No hay un usuario autenticado."
                    _noticiaUiState.value = NoticiaUiState.Error("No hay un usuario autenticado.")
                }
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al actualizar noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticiasInternacionales(userId: String) {
        obtenerNoticiasPorCategoria(userId, "Internacional")
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias =
                    repository.obtenerNoticiasPorCategoria(userId, "Internacional") // Usamos userId
                _listaNoticiasInternacionales.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticias internacionales")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticiasNacionales(userId: String) {
        obtenerNoticiasPorCategoria(userId, "Nacional")
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias =
                    repository.obtenerNoticiasPorCategoria(userId, "Nacional") // Usamos userId
                _listaNoticiasNacionales.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticias nacionales")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticiasRegionales(userId: String) {
        obtenerNoticiasPorCategoria(userId, "Regional")
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val noticias =
                    repository.obtenerNoticiasPorCategoria(userId, "Regional") // Usamos userId
                _listaNoticiasRegionales.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticias regionales")
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
    fun restablecerNoticiaGuardada() {
        _noticiaCargada.value = null
    }


}

// Sealed class for Noticia UI states
sealed class NoticiaUiState {
    object Loading : NoticiaUiState()
    object Success : NoticiaUiState()
    data class Error(val message: String) : NoticiaUiState()
}