package com.jcmateus.casanarestereo.screens.menus

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contactenos(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Contáctenos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        var nombre by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }
        val context = LocalContext.current

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = mensaje,
            onValueChange = { mensaje = it },
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Button(
            onClick = {
                if (nombre.isBlank() || correo.isBlank() || mensaje.isBlank()) {
                    showToast(context, "Por favor, complete todos los campos.")
                } else if (!isValidEmail(correo)) {
                    showToast(context, "Por favor, ingrese un correo electrónico válido.")
                } else {
                    enviarMensaje(nombre, correo, mensaje, context)
                    nombre = ""
                    correo = ""
                    mensaje = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Mensaje")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "También puedes contactarnos a través de:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Teléfono: +57 312 345 6789",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Correo: casanarestereo@gmail.com",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

fun enviarMensaje(nombre: String, correo: String, mensaje: String, context: Context) {
    // Aquí va la lógica para enviar el mensaje
    // Puedes usar una función para manejar el envío
    // y mostrar un mensaje de éxito o error
    // Por ahora, solo mostraremos un Toast
    showToast(context, "Mensaje enviado correctamente a $correo")
    // Aquí puedes agregar la lógica para enviar el mensaje a través de un servicio de correo electrónico,
    // una API, o cualquier otro método que prefieras.
    // Recuerda que el envío de correos electrónicos directamente desde la aplicación no es recomendable
    // y se debe hacer a través de un servidor.
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}