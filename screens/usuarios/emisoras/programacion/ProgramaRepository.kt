package com.jcmateus.casanarestereo.screens.usuarios.emisoras.programacion

import androidx.compose.animation.core.copy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.contenido.Contenido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ProgramaRepository(private val firestore: FirebaseFirestore) {

    suspend fun guardarPrograma(programa: Contenido.Programa, userId: String) {
        val documentReference = firestore.collection("programas").document()
        val programaConId = programa.copy(id = documentReference.id)
        firestore.collection("programas").document(programaConId.id).set(programaConId)
    }

    suspend fun obtenerPrograma(programaId: String): Contenido.Programa {
        val documentReference = firestore.collection("programas").document(programaId)
        val documentSnapshot = documentReference.get().await()
        return documentSnapshot.toObject(Contenido.Programa::class.java) ?: Contenido.Programa( "", "", "", "", "", "", "", "", "", "")
    }

    suspend fun eliminarPrograma(programaId: String) {
        firestore.collection("programas").document(programaId).delete().await()
    }

    suspend fun actualizarPrograma(programaId: String, programa: Contenido.Programa) {
        val documentReference = firestore.collection("programas").document(programaId)
        documentReference.set(programa).await()
    }

    suspend fun obtenerProgramas(userId: String): List<Contenido.Programa> {
        val querySnapshot = firestore.collection("programas")
            .whereEqualTo("userId", userId)
            .get()
            .await()
        val programas = mutableListOf<Contenido.Programa>()
        for (document in querySnapshot.documents) {
            val programa = document.toObject(Contenido.Programa::class.java)
            if (programa != null) {
                programas.add(programa)
            }
        }
        return programas
    }
    suspend fun obtenerProgramas(): List<Contenido.Programa> {
        val querySnapshot = firestore.collection("programas")
            .get()
            .await()
        val programas = mutableListOf<Contenido.Programa>()
        for (document in querySnapshot.documents) {
            val programa = document.toObject(Contenido.Programa::class.java)
            if (programa != null) {
                programas.add(programa)
            }
        }
        return programas
    }
}