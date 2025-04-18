package com.jcmateus.casanarestereo.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class LoginScreenViewModelFactory(
    private val dataStoreManager: DataStoreManager,
    private val authService: AuthService,
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginScreenViewModel(dataStoreManager, authService, firebaseAuth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}