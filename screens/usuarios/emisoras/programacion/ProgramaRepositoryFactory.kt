package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import com.google.firebase.firestore.FirebaseFirestore

class ProgramaRepositoryFactory(private val firestore: FirebaseFirestore) {
    fun create(): ProgramaRepository {
        return ProgramaRepository(firestore)
    }
}