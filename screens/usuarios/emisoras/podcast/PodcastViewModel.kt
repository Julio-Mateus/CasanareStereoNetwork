package com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.add
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PodcastViewModel(
    private val podcastRepository: PodcastRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _podcastUiState = MutableStateFlow<PodcastUiState>(PodcastUiState.Success)
    val podcastUiState: StateFlow<PodcastUiState> = _podcastUiState.asStateFlow()

    private val _podcastGuardado = MutableStateFlow(false)
    val podcastGuardado: StateFlow<Boolean> = _podcastGuardado.asStateFlow()

    private val _errorGuardandoPodcast = MutableStateFlow<String?>(null)
    val errorGuardandoPodcast: StateFlow<String?> = _errorGuardandoPodcast.asStateFlow()

    // StateFlow for the podcast image URI
    private val _imagenPodcastUri = MutableStateFlow<Uri?>(null)
    val imagenPodcastUri: StateFlow<Uri?> = _imagenPodcastUri.asStateFlow()

    // StateFlow for the list of podcasts
    private val _listaPodcasts = MutableStateFlow<List<Contenido.Podcast>>(emptyList())
    val listaPodcasts: StateFlow<List<Contenido.Podcast>> = _listaPodcasts.asStateFlow()

    // StateFlow for errorEliminandoPodcast
    private val _errorEliminandoPodcast = MutableStateFlow<Boolean>(false)
    val errorEliminandoPodcast: StateFlow<Boolean> = _errorEliminandoPodcast.asStateFlow()

    // StateFlow for the current podcast
    private val _podcast = MutableStateFlow<Contenido.Podcast?>(null)
    val podcast: StateFlow<Contenido.Podcast?> = _podcast.asStateFlow()

    private val _navegarAInformacion = MutableStateFlow(false)
    val navegarAInformacion: StateFlow<Boolean> = _navegarAInformacion.asStateFlow()

    fun navegarAInformacionPodcast() {
        _navegarAInformacion.value = true
    }

    fun resetNavegarAInformacion() {
        _navegarAInformacion.value = false
    }


    fun guardarPodcast(podcast: Contenido.Podcast, userId: String) {
        _podcastUiState.value = PodcastUiState.Loading
        viewModelScope.launch {
            try {
                val emisoraId = userId // Assuming userId is the emisoraId
                db.collection("emisoras")
                    .document(emisoraId)
                    .collection("podcasts") // Store in "podcasts" subcollection
                    .add(podcast)
                    .addOnSuccessListener {
                        _podcastGuardado.value = true
                        _podcastUiState.value = PodcastUiState.Success
                        navegarAInformacionPodcast()
                    }
                    .addOnFailureListener { e ->
                        _podcastUiState.value = PodcastUiState.Error(e.message ?: "Error al guardar podcast")
                        _errorGuardandoPodcast.value = e.message
                    }
            } catch (e: Exception) {
                _podcastUiState.value = PodcastUiState.Error(e.message ?: "Error al guardar podcast")
                _errorGuardandoPodcast.value = e.message
            }
        }
    }

    fun restablecerPodcastGuardado() {
        _podcastGuardado.value = false
        _errorGuardandoPodcast.value = null
    }

    fun obtenerPodcasts(userId: String) {
        viewModelScope.launch {
            _podcastUiState.value = PodcastUiState.Loading
            try {
                val podcasts = podcastRepository.obtenerPodcasts(userId)
                _listaPodcasts.value = podcasts
                _podcastUiState.value = PodcastUiState.Success
            } catch (e: Exception) {
                _podcastUiState.value = PodcastUiState.Error(e.message ?: "Error al obtener podcasts")
                _errorGuardandoPodcast.value = e.message
            }
        }
    }

    fun obtenerPodcast(podcastId: String) {
        viewModelScope.launch {
            _podcastUiState.value = PodcastUiState.Loading
            try {
                val podcast = podcastRepository.obtenerPodcast(podcastId)
                _podcast.value = podcast
                _podcastUiState.value = PodcastUiState.Success
            } catch (e: Exception) {
                _podcastUiState.value = PodcastUiState.Error(e.message ?: "Error al obtener podcast")
                _errorGuardandoPodcast.value = e.message
            }
        }
    }

    fun eliminarPodcast(podcast: Contenido.Podcast, userId: String) {
        viewModelScope.launch {
            _podcastUiState.value = PodcastUiState.Loading
            try {
                podcastRepository.eliminarPodcast(podcast.id)
                _errorEliminandoPodcast.value = false
                _podcastUiState.value = PodcastUiState.Success
            } catch (e: Exception) {
                _errorEliminandoPodcast.value = true
                _podcastUiState.value = PodcastUiState.Error(e.message ?: "Error al eliminar podcast")
                _errorGuardandoPodcast.value = e.message
            }
        }
    }

    fun actualizarPodcast(podcastId: String, podcast: Contenido.Podcast) {
        viewModelScope.launch {
            try {
                podcastRepository.actualizarPodcast(podcastId, podcast)
            } catch (e: Exception) {
                _podcastUiState.value = PodcastUiState.Error(e.message ?: "Error al actualizar podcast")
                _errorGuardandoPodcast.value = e.message
            }
        }
    }

    // New function to update the podcast image URI
    fun actualizarImagenPodcast(uri: Uri) {
        _imagenPodcastUri.value = uri
    }

    fun restablecerErrorEliminandoPodcast() {
        _errorEliminandoPodcast.value = false
    }

    fun restablecerErrorGuardandoPodcast() {
        _errorGuardandoPodcast.value = null
    }
}

// Sealed class for Podcast UI states
sealed class PodcastUiState {
    object Loading : PodcastUiState()
    object Success : PodcastUiState()
    data class Error(val message: String) : PodcastUiState()
}