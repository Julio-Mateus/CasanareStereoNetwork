package com.jcmateus.casanarestereo.screens.menus

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.model.Emisora

@Composable
fun PerfilEmisora(emisora: Emisora, onGuardar: (Emisora) -> Unit) {
    // Campos para la información de la emisora
    var nombre by remember { mutableStateOf(emisora.nombre) }
    var descripcion by remember { mutableStateOf(emisora.descripcion) }
    // ... (otros campos)

    // Botón para guardar la información
    Button(onClick= { onGuardar(emisora.copy(nombre = nombre, descripcion = descripcion, /* ... */)) }) {
        Text("Guardar")
    }
}

// Función para guardar la información de la emisora
fun guardarEmisora(emisora: Emisora) {
    emisora.id?.let { FirebaseFirestore.getInstance().collection("emisoras").document(it).set(emisora) }
}