package com.jcmateus.casanarestereo.screens.usuarios.emisoras.banner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BannerViewModelFactory(
    private val repository: BannerRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BannerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BannerViewModel(repository, firebaseAuth, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}