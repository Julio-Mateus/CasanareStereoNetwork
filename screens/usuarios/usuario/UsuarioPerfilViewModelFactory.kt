package com.jcmateus.casanarestereo.screens.usuarios.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcmateus.casanarestereo.screens.login.AuthService

class UsuarioPerfilViewModelFactory(private val usuarioRepository: UsuarioRepository, private val authService: AuthService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioPerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioPerfilViewModel(usuarioRepository, authService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}