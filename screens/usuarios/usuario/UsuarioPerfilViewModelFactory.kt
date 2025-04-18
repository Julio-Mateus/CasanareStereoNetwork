package com.jcmateus.casanarestereo.screens.usuarios.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jcmateus.casanarestereo.screens.login.AuthService

class UsuarioPerfilViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val authService: AuthService,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioPerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioPerfilViewModel(usuarioRepository, authService, db, storage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}