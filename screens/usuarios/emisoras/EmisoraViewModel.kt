package com.jcmateus.casanarestereo.screens.usuarios.emisoras


import android.content.ContentValues.TAG
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

class EmisoraViewModel(private val repository: EmisoraRepository,private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val _perfilEmisora = MutableStateFlow(PerfilEmisora()) // Cambiar a MutableStateFlow
    val perfilEmisora: StateFlow<PerfilEmisora> =
        _perfilEmisora.asStateFlow() // Exponer como StateFlow


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var ultimaNoticiaGuardada: Contenido.Noticia? = null
    private val _noticiaGuardada = MutableStateFlow(false)
    var noticiaGuardada: StateFlow<Boolean> = _noticiaGuardada.asStateFlow()

    private val _noticiaActual = MutableStateFlow<Contenido.Noticia?>(null)
    val noticiaActual: StateFlow<Contenido.Noticia?> = _noticiaActual.asStateFlow()

    private val _errorGuardandoNoticia = MutableStateFlow(false)
    val errorGuardandoNoticia: StateFlow<Boolean> = _errorGuardandoNoticia.asStateFlow()



    private val _podcastGuardado = MutableStateFlow(false)
    var podcastGuardado: StateFlow<Boolean> = _podcastGuardado.asStateFlow()
    private val _errorGuardandoPodcast = MutableStateFlow(false)
    val errorGuardandoPodcast: StateFlow<Boolean> = _errorGuardandoPodcast.asStateFlow()

    init {
        viewModelScope.launch {
            cargarPerfilEmisora()
        }
    }


    fun actualizarPerfil(perfil: PerfilEmisora, navController: NavHostController) {
        _perfilEmisora.value = perfil

        guardarPerfilEmisora(perfil, navController)

    }

    fun actualizarImagenPerfil(imagenUri: Uri) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/${firebaseAuth.currentUser?.uid}")
        val uploadTask = storageRef.putFile(imagenUri)

        uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val userId = firebaseAuth.currentUser?.uid ?: return@addOnSuccessListener
                    val emisoraData = hashMapOf(
                        "imagenPerfilUri" to downloadUri.toString(),
                        "nombre" to perfilEmisora.value?.nombre, // Obtener los demás campos del perfil actual
                        "descripcion" to perfilEmisora.value?.descripcion,
                        "enlace" to perfilEmisora.value?.enlace,
                        "paginaWeb" to perfilEmisora.value?.paginaWeb,
                        "ciudad" to perfilEmisora.value?.ciudad,
                        "departamento" to perfilEmisora.value?.departamento,
                        "frecuencia" to perfilEmisora.value?.frecuencia
                    )
                    FirebaseFirestore.getInstance().collection("emisoras").document(userId)
                        .set(emisoraData) // Usar set para actualizar todos los campos
                        .addOnSuccessListener {
                            _perfilEmisora.value =
                                _perfilEmisora.value.copy(imagenPerfilUri = downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error al actualizar la Uri de la imagen", e)
                        }
                }
            } else {
                Log.w(TAG, "Error al subir la imagen", task.exception)
            }
            return@addOnCompleteListener
        }
    }

    private fun guardarPerfilEmisora(perfil: PerfilEmisora, navController: NavHostController) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            Log.d(TAG, "Guardando perfil de emisora: $perfil")

            val emisoraData = hashMapOf(
                "imagenPerfilUri" to perfil.imagenPerfilUri,
                "nombre" to perfil.nombre,
                "descripcion" to perfil.descripcion,
                "enlace" to perfil.enlace,
                "paginaWeb" to perfil.paginaWeb,
                "ciudad" to perfil.ciudad,
                "departamento" to perfil.departamento,
                "frecuencia" to perfil.frecuencia
            )

            FirebaseFirestore.getInstance().collection("emisoras").document(userId)
                .set(emisoraData) // Usar emisoraData en lugar de perfil
                .addOnSuccessListener {
                    Log.d(TAG, "Perfil de emisora guardado correctamente")
                    navController.navigate(Destinos.EmisoraVista.ruta)
                    // Los datos se guardaron correctamente
                    // Puedes mostrar un mensaje de éxito al usuario
                }
                .addOnFailureListener { e ->
                    // Error al guardar los datos
                    // Manejar el error aquí, por ejemplo, mostrar un mensaje de error al usuario
                    Log.w(TAG, "Error al guardar el perfil de la emisora", e)
                }
        }
    }

    // Función para cargar los datos del perfil de la emisora desde Firestore
    fun cargarPerfilEmisora() {
        viewModelScope.launch {
            delay(2000) // Simula un delay de 2 segundos
            val db = FirebaseFirestore.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                db.collection("emisoras").document(user.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val perfil = document.toObject(PerfilEmisora::class.java)
                            if (perfil != null) {
                                _perfilEmisora.value = perfil
                            }
                        } else {
                            // El documento no existe, puedes crear un nuevo perfil de emisora
                            // o manejar la situación de otra manera
                        }
                        _isLoading.value = false // Actualizar el estado de carga a false
                    }
                    .addOnFailureListener { e ->
                        // Error al cargar los datos
                        // Manejar el error aquí, por ejemplo, mostrar un mensaje de error al usuario
                        Log.w(TAG, "Error al cargar el perfil de la emisora", e)
                        _isLoading.value = false // Actualizar el estado de carga a false
                    }
            }
        }
    }

    fun subirPodcast(podcast: Contenido.Podcast) {
        viewModelScope.launch {
            repository.subirPodcast(podcast)
        }
    }
    fun subirNoticia(noticia: Contenido.Noticia) {
        viewModelScope.launch {
            repository.subirNoticia(noticia)
        }
    }
    fun subirPrograma(programa: Contenido.Programa) {
        viewModelScope.launch {
            repository.subirPrograma(programa)
        }
    }
    fun subirBanner(banner: Contenido.Banner) {
        viewModelScope.launch {
            repository.subirBanner(banner)
        }
    }


    fun guardarNoticia(noticia: Contenido.Noticia) {
        viewModelScope.launch {
            try {
                val userId = firebaseAuth.currentUser?.uid ?: ""
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("emisoras").document(userId).collection("noticias").document()
                val noticiaConId = noticia.copy(id = docRef.id)
                db.collection("emisoras").document(userId).collection("noticias").document(noticiaConId.id).set(noticiaConId).await()
                _noticiaGuardada.value = true
                _noticiaActual.value = noticiaConId
                ultimaNoticiaGuardada = noticiaConId
            } catch (e: Exception) {
                _errorGuardandoNoticia.value = true
            }
        }
    }

    fun obtenerNoticiaGuardada(): Contenido.Noticia? {
        return ultimaNoticiaGuardada
    }

    fun restablecerNoticiaGuardada() {
        _noticiaGuardada.value = false
    }


    fun guardarPodcasts(podcast: Contenido.Podcast) {
        viewModelScope.launch {
            try {
                val userId = firebaseAuth.currentUser?.uid ?: ""
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("emisoras").document(userId).collection("podcasts").document()
                val podcastConId = podcast.copy(id = docRef.id)
                db.collection("emisoras").document(userId).collection("podcasts").document(podcastConId.id).set(podcastConId).await()
                _podcastGuardado.value = true
            } catch (e: Exception) {
                _errorGuardandoPodcast.value = true
            }
        }
    }

    fun restablecerPodcastGuardado() {
        _podcastGuardado.value = false
    }
    init {
        cargarPerfilEmisora() // Llamar a la función para cargar los datos
    }
}
