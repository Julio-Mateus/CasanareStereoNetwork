package com.jcmateus.casanarestereo.screens.usuarios.emisoras.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PodcastViewModelFactory(
    private val podcastRepository: PodcastRepository,
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PodcastViewModel::class.java)) {
            PodcastViewModel(podcastRepository, firebaseAuth, db) as T // Pasa 'db' aquí
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}