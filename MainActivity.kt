package com.jcmateus.casanarestereo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jcmateus.casanarestereo.screens.formulario.HomeFormularioActivity
import com.jcmateus.casanarestereo.screens.login.CasanareLoginActivity
import com.jcmateus.casanarestereo.screens.login.CasanareLoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TuAppTheme()

        }
    }
}

@Composable
fun PantallaPresentacion(navController: NavController? = null) {
    val iniciarSesion = LocalContext.current
    val homeFormulario = LocalContext.current
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
        ) // Color negro con 70% de opacidad
        // Contenido sobre la imagen
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
                fontSize = 25.sp,
                color = Color.LightGray // Cambia el color del texto para que se vea sobre la imagen
            )
            Text(
                text = "DONDE LATE EL CORAZÓN DEL LLANO",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 15.sp,
                color = Color.LightGray // Cambia el color del texto para que se vea sobre la imagen
            )
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedButton(
                onClick = {
                    iniciarSesion.startActivity(
                        Intent(
                            iniciarSesion,
                            CasanareLoginActivity::class.java
                        )
                    )
                },
                modifier = Modifier
                    .width(280.dp)// Agrega un ancho fijo al botón
                    .height(60.dp),// Agrega un alto fijo al botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary, // Fondo transparente
                    contentColor = MaterialTheme.colorScheme.secondary // Texto en blanco

                )

            ) {
                Text(
                    "Iniciar Sesión",
                    fontSize = 20.sp,
                ) // Cambia el color del texto del botón
            }
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedButton(
                onClick = {
                    homeFormulario.startActivity(
                        Intent(
                            homeFormulario,
                            HomeFormularioActivity::class.java
                        )
                    )
                },
                modifier = Modifier
                    .width(280.dp)// Agrega un ancho fijo al botón
                    .height(60.dp),// Agrega un alto fijo al botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary, // Fondo transparente
                    contentColor = MaterialTheme.colorScheme.secondary // Texto en blanco
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
                            iniciarSesion.startActivity(
                                Intent(
                                    iniciarSesion,
                                    CasanareLoginActivity::class.java
                                )
                            )
                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CasanareStereoPreview() {
    TuAppTheme()
}

@Composable
fun TuAppTheme() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PantallaPresentacion()
        //CasanareLoginScreen(navController = NavController(LocalContext.current))
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            CasanareLoginScreen(navController = NavController(LocalContext.current))
        } else {
            PantallaPresentacion(navController = NavController(LocalContext.current))
        }
    }

}
