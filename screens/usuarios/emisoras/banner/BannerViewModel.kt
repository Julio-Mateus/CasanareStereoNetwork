package com.jcmateus.casanarestereo.screens.usuarios.emisoras.banner

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

class BannerViewModel(
    private val repository: BannerRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore // Add Firestore instance
) : ViewModel() {

    private val _bannerUiState = MutableStateFlow<BannerUiState>(BannerUiState.Loading)
    val bannerUiState: StateFlow<BannerUiState> = _bannerUiState.asStateFlow()

    private val _bannerGuardado = MutableStateFlow(false)
    val bannerGuardado: StateFlow<Boolean> = _bannerGuardado.asStateFlow()

    private val _errorGuardandoBanner = MutableStateFlow<String?>(null)
    val errorGuardandoBanner: StateFlow<String?> = _errorGuardandoBanner.asStateFlow()

    // StateFlow for the banner image URI
    private val _imagenBannerUri = MutableStateFlow<Uri?>(null)
    val imagenBannerUri: StateFlow<Uri?> = _imagenBannerUri.asStateFlow()

    // StateFlow for the list of banners
    private val _listaBanners = MutableStateFlow<List<Contenido.Banner>>(emptyList())
    val listaBanners: StateFlow<List<Contenido.Banner>> = _listaBanners.asStateFlow()

    // StateFlow for errorEliminandoBanner
    private val _errorEliminandoBanner = MutableStateFlow<Boolean>(false)
    val errorEliminandoBanner: StateFlow<Boolean> = _errorEliminandoBanner.asStateFlow()

    // StateFlow for the current banner
    private val _banner = MutableStateFlow<Contenido.Banner?>(null)
    val banner: StateFlow<Contenido.Banner?> = _banner.asStateFlow()

    fun guardarBanner(banner: Contenido.Banner, userId: String) {
        _bannerUiState.value = BannerUiState.Loading
        viewModelScope.launch {
            try {
                val emisoraId = userId // Assuming userId is the emisoraId
                db.collection("emisoras")
                    .document(emisoraId)
                    .collection("banners") // Store in "banners" subcollection
                    .add(banner)
                    .addOnSuccessListener {
                        _bannerGuardado.value = true
                        _bannerUiState.value = BannerUiState.Success
                    }
                    .addOnFailureListener { e ->
                        _bannerUiState.value = BannerUiState.Error(e.message ?: "Error al guardar banner")
                        _errorGuardandoBanner.value = e.message
                    }
            } catch (e: Exception) {
                _bannerUiState.value = BannerUiState.Error(e.message ?: "Error al guardar banner")
                _errorGuardandoBanner.value = e.message
            }
        }
    }

    fun restablecerBannerGuardado() {
        _bannerGuardado.value = false
        _errorGuardandoBanner.value = null
    }

    fun obtenerBanners(userId: String) {
        viewModelScope.launch {
            _bannerUiState.value = BannerUiState.Loading
            try {
                val banners = repository.obtenerBanners(userId)
                _listaBanners.value = banners
                _bannerUiState.value = BannerUiState.Success
            } catch (e: Exception) {
                _bannerUiState.value = BannerUiState.Error(e.message ?: "Error al obtener banners")
                _errorGuardandoBanner.value = e.message
            }
        }
    }

    fun obtenerBanner(bannerId: String) {
        viewModelScope.launch {
            _bannerUiState.value = BannerUiState.Loading
            try {
                val banner = repository.obtenerBanner(bannerId)
                _banner.value = banner
                _bannerUiState.value = BannerUiState.Success
            } catch (e: Exception) {
                _bannerUiState.value = BannerUiState.Error(e.message ?: "Error al obtener banner")
                _errorGuardandoBanner.value = e.message
            }
        }
    }

    fun eliminarBanner(banner: Contenido.Banner, userId: String) {
        viewModelScope.launch {
            _bannerUiState.value = BannerUiState.Loading
            try {
                repository.eliminarBanner(banner.id)
                _errorEliminandoBanner.value = false
                _bannerUiState.value = BannerUiState.Success
            } catch (e: Exception) {
                _errorEliminandoBanner.value = true
                _bannerUiState.value = BannerUiState.Error(e.message ?: "Error al eliminar banner")
                _errorGuardandoBanner.value = e.message
            }
        }
    }

    fun actualizarBanner(bannerId: String, banner: Contenido.Banner) {
        viewModelScope.launch {
            try {
                repository.actualizarBanner(bannerId, banner)
            } catch (e: Exception) {
                _bannerUiState.value = BannerUiState.Error(e.message ?: "Error al actualizar banner")
                _errorGuardandoBanner.value = e.message
            }
        }
    }

    // Function to update the banner image URI
    fun actualizarImagenBanner(uri: Uri) {
        _imagenBannerUri.value = uri
    }

    fun restablecerErrorEliminandoBanner() {
        _errorEliminandoBanner.value = false
    }

    fun restablecerErrorGuardandoBanner() {
        _errorGuardandoBanner.value = null
    }
}

// Sealed class for Banner UI states
sealed class BannerUiState {
    object Loading : BannerUiState()
    object Success : BannerUiState()
    data class Error(val message: String) : BannerUiState()
}