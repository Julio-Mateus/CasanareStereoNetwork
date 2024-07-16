package com.jcmateus.casanarestereo.screens.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jcmateus.casanarestereo.model.User
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    
   // Login with Google
    fun signInWithGoogleCredential(credential: AuthCredential, home:()->Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("CasanareStereo", "Logueado con Google Exitoso")
                        home()
                    }
                }
                .addOnFailureListener {
                    Log.d("CasanareStereo", "Error al logear con Google")
                }
        }        
        catch (ex: Exception){
            Log.d("CasanareStereo", "Excepción al logear con Google" +
                    ex.localizedMessage
            )
        }
    }

    // Login with Correo y Contraseña
    fun signInWithEmailAndPassword(email:String, password:String, home: ()-> Unit)
        = viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("CasanareStereo", "signInWithEmailAndPassword logueado!!")
                            home()
                        }
                        else {
                            Log.d("CasanareStereo", "signInWithEmailAndPassword: ${task.result}")
                        }
                    }

            }
            catch (ex: Exception){
                Log.d("CasanareStereo", "signInWithEmailAndPassword : ${ex.message}"
                )
            }
        }
    fun createUserWithEmailAndPassword(email: String,password: String, home: () -> Unit){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }
                    else {
                        Log.d("CasanareStereo", "createUserWithEmailAndPassword: ${task.result}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        //val user = mutableMapOf<String, Any>()

        //user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()

        //Usando Data Class
        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Hola que tal",
            profession = "Estudiante",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("CasanareStereo", "Creado con exito: $it.id")
            }
            .addOnFailureListener { e ->
                Log.w("CasanareStereo", "Error de creacion de usuario", e)
            }

    }
}

