package com.jcmateus.casanarestereo

//import com.jcmateus.casanarestereo.navigation.NavegacionCasanare
//import com.jcmateus.casanarestereo.navigation.AuthenticationNavHost
//import com.jcmateus.casanarestereo.navigation.NavegacionCasanare
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    // Constructor vacío
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController= (application as HomeApplication).navController // Obtener navController de la aplicación
        setContent {
            CasanareStereoTheme {
                Scaffold { innerPadding ->
                    NavigationHost(navController, innerPadding)
                }
            }
        }
    }
}

@Composable
fun PantallaPresentacion(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Usamos el color de fondo del tema
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = 0.7f), // Oscurecemos la imagen con un filtro de color
                blendMode = BlendMode.Darken
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), //Padding consistente
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BIENVENIDO A:",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 44.sp,
                color = MaterialTheme.colorScheme.onBackground, // Color del texto sobre el fondo
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedSoundWave()
            /*
            var angle by remember { mutableStateOf(0f) }
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val angleAnim by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = ""
            )
            angle = angleAnim
            Column(
                // ...
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu imagen
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp) // Establece el tamaño del logo
                        .graphicsLayer {
                            rotationZ = angle
                        }

                )
            }
            */
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "CASANARE STEREO NETWORK",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
            )
            Text(
                text = "DONDE LATE EL CORAZÓN DEL LLANO",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {navController.navigate(Destinos.CasanareLoginScreen.ruta)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp), // Alto del botón
                shape = MaterialTheme.shapes.medium, // Usamos las formas del tema
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground // Color del texto del botón
                )
            ) {
                Text(
                    "En otro momento",
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = MaterialTheme.colorScheme.onBackground,
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
@Composable
fun SoundWaveIcon(modifier: Modifier = Modifier, amplitude: Float, color: Color) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val centerY = height / 2

            val lineWidth = width / 7 // Ancho de cada línea
            val totalLineWidth = 20 * lineWidth // Ancho total de la onda (20 líneas)
            val offset = (width - totalLineWidth) / 2 // Offset para centrar

            // Crear múltiples líneas para simular una onda
            for (i in 0..20) {
                val x = offset + i * lineWidth
                val y = centerY + (amplitude * 1.5f * sin(x.toDouble())).toFloat()
                moveTo(x, centerY)
                lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 12f)// Trazo más grueso
        )
    }
}
@Composable
fun AnimatedSoundWave() {
    // Crear una transición infinita
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")

    // Animar la amplitud de la onda
    val amplitude by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "amplitude_animation"
    )

    // Dibujar el icono de la onda sonora con la animación
    SoundWaveIcon(
        modifier = Modifier
            .size(120.dp)
            .padding(16.dp),
        amplitude = amplitude,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Preview(showBackground = true)
@Composable
fun CasanareStereoPreview() {
    PantallaPresentacion(navController = rememberNavController())
}
