package com.jcmateus.casanarestereo.screens.usuarios.emisoras.noticias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.DataStoreManager

class NoticiaViewModelFactory(
    private val repository: NoticiaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val authService: AuthService,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticiaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoticiaViewModel(repository, firebaseAuth, db, authService, dataStoreManager) as T // Pasa la instancia de Firestore
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}