package com.jcmateus.casanarestereo.screens.usuarios


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.home.Destinos

class EmisoraViewModel : ViewModel() {
    private val _perfilEmisora = MutableLiveData(PerfilEmisora())
    val perfilEmisora: LiveData<PerfilEmisora> = _perfilEmisora

    fun actualizarPerfil(perfil: PerfilEmisora, navController: NavHostController) {
        _perfilEmisora.value = perfil

        // Guardar los datos en Firestore
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            db.collection("emisoras").document(user.uid)
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
