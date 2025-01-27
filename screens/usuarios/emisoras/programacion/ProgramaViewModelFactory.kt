package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProgramaViewModelFactory(
    private val programaRepository: ProgramaRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgramaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgramaViewModel(programaRepository, firebaseAuth, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}