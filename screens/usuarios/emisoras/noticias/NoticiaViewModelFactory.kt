package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoticiaViewModelFactory(
    private val repository: NoticiaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore // Agrega la instancia de Firestore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticiaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoticiaViewModel(repository, firebaseAuth, db) as T // Pasa la instancia de Firestore
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}