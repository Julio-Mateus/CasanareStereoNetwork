package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.add
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.DataStoreManager
import com.jcmateus.casanarestereo.screens.login.Rol
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.io.path.exists

class NoticiaViewModel(
    private val repository: NoticiaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val authService: AuthService,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {


    private val _noticiaUiState = MutableStateFlow<NoticiaUiState>(NoticiaUiState.Loading)
    val noticiaUiState: StateFlow<NoticiaUiState> = _noticiaUiState.asStateFlow()

    private val _errorGuardandoNoticia = MutableStateFlow<String?>(null)
    val errorGuardandoNoticia: StateFlow<String?> = _errorGuardandoNoticia.asStateFlow()

    private val _imagenNoticiaUri = MutableStateFlow<Uri?>(null)
    val imagenNoticiaUri: StateFlow<Uri?> = _imagenNoticiaUri.asStateFlow()

    private val _listaNoticias = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticias: StateFlow<List<Contenido.Noticia>> = _listaNoticias.asStateFlow()

    private val _noticiaCargada = MutableStateFlow<Contenido.Noticia?>(null)
    val noticiaCargada: StateFlow<Contenido.Noticia?> = _noticiaCargada.asStateFlow()

    private val _errorEliminandoNoticia = MutableStateFlow<String?>(null)
    val errorEliminandoNoticia: StateFlow<String?> = _errorEliminandoNoticia.asStateFlow()

    private val _noticia = MutableStateFlow<Contenido.Noticia?>(null)
    val noticia: StateFlow<Contenido.Noticia?> = _noticia.asStateFlow()

    private val _listaNoticiasFiltradas = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasFiltradas: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasFiltradas.asStateFlow()

    private val _listaNoticiasInternacionales =
        MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasInternacionales: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasInternacionales.asStateFlow()

    private val _listaNoticiasNacionales = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasNacionales: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasNacionales.asStateFlow()

    private val _listaNoticiasRegionales = MutableStateFlow<List<Contenido.Noticia>>(emptyList())
    val listaNoticiasRegionales: StateFlow<List<Contenido.Noticia>> =
        _listaNoticiasRegionales.asStateFlow()


    fun guardarNoticia(noticia: Contenido.Noticia) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                // Obtener el userId del usuario autenticado
                val userId = dataStoreManager.getUserId() ?: ""
                // Obtener el emisoraId del usuario autenticado
                val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                // Crear una nueva noticia con el ID generado
                val nuevaNoticia = noticia.copy(imagenUriNoticia = _imagenNoticiaUri.value.toString())
                // Guardar la noticia en la subcolección noticias dentro del documento de la emisora
                repository.guardarNoticia(nuevaNoticia, emisoraId.toString())
                // Solo si guardarNoticia fue exitoso, actualizamos los estados
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
                val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                val noticias = repository.obtenerNoticias(emisoraId.toString())
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
                val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                val noticias =
                    repository.obtenerNoticiasPorCategoria(emisoraId.toString(), categoria)
                _listaNoticiasFiltradas.value = noticias
                _noticiaUiState.value = NoticiaUiState.Success
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticias por categoría")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticia(noticiaId: String, userId: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val rol = dataStoreManager.getRol().first()
                if (rol == Rol.EMISORA) {
                    val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                    val noticia = repository.obtenerNoticia(noticiaId, emisoraId.toString())
                    _noticia.value = noticia
                    _noticiaCargada.value = noticia
                    _noticiaUiState.value = NoticiaUiState.Success
                } else {
                    _errorGuardandoNoticia.value = "El usuario no tiene el rol EMISORA."
                    _noticiaUiState.value =
                        NoticiaUiState.Error("El usuario no tiene el rol EMISORA.")
                }
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al obtener noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }
    fun eliminarNoticia(noticiaId: String, userId: String) {
        _noticiaUiState.value = NoticiaUiState.Loading
        viewModelScope.launch {
            try {
                val rol = dataStoreManager.getRol().first()
                if (rol == Rol.EMISORA) {
                    val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                    repository.eliminarNoticia(noticiaId, emisoraId.toString())
                    _errorEliminandoNoticia.value = null
                    _noticiaUiState.value = NoticiaUiState.Success
                } else {
                    _errorEliminandoNoticia.value = "El usuario no tiene el rol EMISORA."
                    _noticiaUiState.value =
                        NoticiaUiState.Error("El usuario no tiene el rol EMISORA.")
                }
            } catch (e: Exception) {
                _errorEliminandoNoticia.value = e.message ?: "Error al eliminar noticia"
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al eliminar noticia")
            }
        }
    }

    fun actualizarNoticia(
        noticiaId: String,
        noticia: Contenido.Noticia,
        userId: String
    ) {
        _noticiaUiState.value = NoticiaUiState.Loading
        viewModelScope.launch {
            try {
                val rol = dataStoreManager.getRol().first()
                if (rol == Rol.EMISORA) {
                    val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                    val noticiaConImagen =
                        noticia.copy(imagenUriNoticia = _imagenNoticiaUri.value.toString())
                    repository.actualizarNoticia(noticiaId, noticiaConImagen, emisoraId.toString())
                    obtenerNoticia(noticiaId, userId)
                } else {
                    _errorGuardandoNoticia.value = "El usuario no tiene el rol EMISORA."
                    _noticiaUiState.value =
                        NoticiaUiState.Error("El usuario no tiene el rol EMISORA.")
                }
            } catch (e: Exception) {
                _noticiaUiState.value =
                    NoticiaUiState.Error(e.message ?: "Error al actualizar noticia")
                _errorGuardandoNoticia.value = e.message
            }
        }
    }

    fun obtenerNoticiasInternacionales(userId: String) {
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                val noticias =
                    repository.obtenerNoticiasPorCategoria(emisoraId.toString(), "Internacional")
                // Solo si obtenerNoticiasPorCategoria fue exitoso, actualizamos los estados
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
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                val noticias =
                    repository.obtenerNoticiasPorCategoria(emisoraId.toString(), "Nacional")
                // Solo si obtenerNoticiasPorCategoria fue exitoso, actualizamos los estados
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
        viewModelScope.launch {
            _noticiaUiState.value = NoticiaUiState.Loading
            try {
                val emisoraId = dataStoreManager.getEmisoraId() ?: ""
                val noticias =
                    repository.obtenerNoticiasPorCategoria(emisoraId.toString(), "Regional")
                // Solo si obtenerNoticiasPorCategoria fue exitoso, actualizamos los estados
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
        _errorEliminandoNoticia.value = null
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