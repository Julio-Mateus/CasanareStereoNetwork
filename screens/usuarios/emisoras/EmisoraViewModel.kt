package com.jcmateus.casanarestereo.screens.usuarios.emisoras


import android.content.ContentValues.TAG
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.add
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido.Noticia
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido.Podcast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.text.set

class EmisoraViewModel(private val repository: EmisoraRepository, private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val _perfilEmisora = MutableStateFlow<PerfilEmisora?>(null)
    val perfilEmisora: StateFlow<PerfilEmisora?> = _perfilEmisora.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Nueva variable para guardar las emisoras cercanas
    private val _emisorasCercanas = MutableStateFlow<List<PerfilEmisora>>(emptyList())
    val emisorasCercanas: StateFlow<List<PerfilEmisora>> = _emisorasCercanas.asStateFlow()

    init {
        cargarPerfilEmisora()
    }

    fun guardarPerfil(perfil: PerfilEmisora, userId: String, navController: NavHostController) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.guardarPerfilEmisora(perfil, userId)
                _perfilEmisora.value = perfil // Actualizar el perfil en el ViewModel
                navController.navigate(Destinos.EmisoraVista.ruta)
            } catch (e: Exception) {
                Log.e("EmisoraViewModel", "Error al guardar el perfil", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarImagenPerfil(imagenUri: Uri, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.actualizarImagenPerfil(imagenUri, userId)
                cargarPerfil(userId)
            } catch (e: Exception) {
                Log.e("EmisoraViewModel", "Error al actualizar la imagen de perfil", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun cargarPerfilEmisora() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = firebaseAuth.currentUser?.uid ?: return@launch
                cargarPerfil(userId)
            } catch (e: Exception) {
                Log.e("EmisoraViewModel", "Error al cargar el perfil", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarPerfil(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val perfil = repository.cargarPerfilEmisora(userId)
                _perfilEmisora.value = perfil
            } catch (e: Exception) {
                Log.e("EmisoraViewModel", "Error al cargar el perfil", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Nuevo método para obtener las emisoras cercanas
    fun getEmisorasCercanas(location: Location) {
        viewModelScope.launch {
            // Aquí debes implementar la lógica para obtener las emisoras cercanas
            // usando la ubicación del usuario (location)
            // Por ahora, vamos a simular que obtenemos una lista de emisoras
            val emisoras = repository.getEmisorasCercanas(location)
            _emisorasCercanas.value = emisoras
        }
    }
}