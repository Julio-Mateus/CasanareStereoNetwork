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
    private val _perfilEmisora = MutableStateFlow(PerfilEmisora())
    val perfilEmisora: StateFlow<PerfilEmisora> = _perfilEmisora.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Nueva variable para guardar las emisoras cercanas
    private val _emisorasCercanas = MutableStateFlow<List<PerfilEmisora>>(emptyList())
    val emisorasCercanas: StateFlow<List<PerfilEmisora>> = _emisorasCercanas.asStateFlow()

    init {
        cargarPerfilEmisora()
    }

    fun actualizarPerfil(perfil: PerfilEmisora, navController: NavHostController) {
        _perfilEmisora.value = perfil
        guardarPerfilEmisora(perfil, navController)
    }

    fun actualizarImagenPerfil(imagenUri: Uri) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            repository.actualizarImagenPerfil(imagenUri, userId)
        }
    }

    private fun guardarPerfilEmisora(perfil: PerfilEmisora, navController: NavHostController) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            repository.guardarPerfilEmisora(perfil, userId)
            navController.navigate(Destinos.EmisoraVista.ruta)
        }
    }

    fun cargarPerfilEmisora() {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            val perfil = repository.cargarPerfilEmisora(userId)
            if (perfil != null) {
                _perfilEmisora.value = perfil
            }
            _isLoading.value = false
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
