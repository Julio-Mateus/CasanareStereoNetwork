package com.jcmateus.casanarestereo.screens.usuarios


import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.jcmateus.casanarestereo.screens.home.Destinos

import kotlin.text.set

class EmisoraViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val _perfilEmisora = MutableLiveData(PerfilEmisora())
    val perfilEmisora: LiveData<PerfilEmisora> = _perfilEmisora

    fun actualizarPerfil(perfil: PerfilEmisora, navController: NavHostController) {
        _perfilEmisora.value = perfil

        guardarPerfilEmisora(perfil, navController)

    }
    class EmisoraViewModelFactory(private val firebaseAuth: FirebaseAuth) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EmisoraViewModel::class.java)) {
                return EmisoraViewModel(firebaseAuth) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun actualizarImagenPerfil(imagenUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${firebaseAuth.currentUser?.uid}")
        val uploadTask = storageRef.putFile(imagenUri)

        uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> -> // Especificar el tipo
            if (task.isSuccessful) { // Acceder a isSuccessful correctamente
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val userId = firebaseAuth.currentUser?.uid ?: return@addOnSuccessListener
                    FirebaseFirestore.getInstance().collection("emisoras").document(userId)
                        .update("imagenPerfilUri", downloadUri.toString())
                        .addOnSuccessListener {
                            _perfilEmisora.value = _perfilEmisora.value?.copy(imagenPerfilUri = downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error al actualizar la Uri de la imagen", e)
                        }
                }
            } else {
                Log.w(TAG, "Error al subir la imagen", task.exception) // Acceder a exception correctamente
            }
            return@addOnCompleteListener // Agregar return
        }
    }

    private fun guardarPerfilEmisora(perfil: PerfilEmisora, navController: NavHostController) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch

            FirebaseFirestore.getInstance().collection("emisoras").document(userId)
                .set(perfil)
                .addOnSuccessListener {
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
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            db.collection("emisoras").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val perfil = document.toObject(PerfilEmisora::class.java)
                        _perfilEmisora.value = perfil
                    } else {
                        // El documento no existe, puedes crear un nuevo perfil de emisora
                        // o manejar la situación de otra manera
                    }
                }
                .addOnFailureListener { e ->
                    // Error al cargar los datos
                    // Manejar el error aquí, por ejemplo, mostrar un mensaje de error al usuario
                    Log.w(TAG, "Error al cargar el perfil de la emisora", e)
                }
        }
    }
    init {
        cargarPerfilEmisora() // Llamar a la función para cargar los datos
    }
}
