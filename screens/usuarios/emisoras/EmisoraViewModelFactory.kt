package com.jcmateus.casanarestereo.screens.usuarios.emisoras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class EmisoraViewModelFactory(
    private val emisoraRepository: EmisoraRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmisoraViewModel::class.java)) {
            return EmisoraViewModel(emisoraRepository, firebaseAuth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}