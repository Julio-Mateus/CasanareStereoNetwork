package com.jcmateus.casanarestereo.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jcmateus.casanarestereo.R

@Composable
fun InicioCasanareVista(navController: NavController){
    Box {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo), // Reemplaza con tu imagen
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            //colorFilter = ColorFilter.tint(Color(0xFF000000), blendMode = BlendMode.Darken) // Color negro con opacidad
        )
        // Capa de oscurecimiento
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000).copy(alpha = 0.6f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp), // Agrega un padding para separar el contenido de los bordes
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BIENVENIDO A:",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 44.sp,
                color = Color.White, // Cambia el color del texto para que se vea sobre la imagen
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(58.dp))
            Image(
                painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu imagen
                contentDescription = "Logo",
                modifier = Modifier
                    //.width(48.dp) // Agrega un ancho fijo al logo
                    .size(80.dp) // Establece el tamaño del logo

            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "CASANARE STEREO NETWORK",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = Color.LightGray // Cambia el color del texto para que se vea sobre la imagen
            )
            Text(
                text = "DONDE LATE EL CORAZÓN DEL LLANO",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = Color.LightGray // Cambia el color del texto para que se vea sobre la imagen
            )
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedButton(
                onClick = {navController.navigate("Login")},
                modifier = Modifier
                    .width(280.dp)// Agrega un ancho fijo al botón
                    .height(60.dp),// Agrega un alto fijo al botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                    contentColor = MaterialTheme.colorScheme.scrim

                )

            ) {
                Text(
                    "Iniciar Sesión",
                    fontSize = 20.sp,
                ) // Cambia el color del texto del botón
            }
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedButton(
                onClick = {navController.navigate("Home")},
                modifier = Modifier
                    .width(280.dp)// Agrega un ancho fijo al botón
                    .height(60.dp),// Agrega un alto fijo al botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary, // Fondo transparente
                    contentColor = MaterialTheme.colorScheme.scrim // Texto en blanco
                )
            ) {
                Text(
                    "En otro momento",
                    fontSize = 20.sp,
                ) // Cambia el color del texto del botón

            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                )
                Text(
                    "Registrate",
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Text(text = "Beneficios de tener una cuenta",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)

        )
    }
}