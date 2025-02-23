package com.jcmateus.casanarestereo

//import com.jcmateus.casanarestereo.navigation.NavegacionCasanare
//import com.jcmateus.casanarestereo.navigation.AuthenticationNavHost
//import com.jcmateus.casanarestereo.navigation.NavegacionCasanare
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.jcmateus.casanarestereo.navigation.NavigationHost
import com.jcmateus.casanarestereo.screens.formulario.FormularioViewModel
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.createLoginViewModel
import com.jcmateus.casanarestereo.screens.login.AuthService
import com.jcmateus.casanarestereo.screens.login.EstadoAutenticacion
import com.jcmateus.casanarestereo.screens.login.LoginScreenViewModel
import com.jcmateus.casanarestereo.screens.usuarios.usuario.MyLocationManager
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioPerfilViewModel
import com.jcmateus.casanarestereo.screens.usuarios.usuario.UsuarioRepository
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.append
import kotlin.text.firstOrNull

class MainActivity : ComponentActivity() {
    private lateinit var myLocationManager: MyLocationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = (application as HomeApplication).navController
        myLocationManager = MyLocationManager(this)

        setContent {
            CasanareStereoTheme { // Aquí envolvemos el contenido con nuestro tema
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(navController)
                }
            }
        }
        lifecycleScope.launch {
            if (checkLocationPermissions()) {
                val location = myLocationManager.getLastKnownLocation()
                if (location != null) {
                    Log.d(
                        "MainActivity",
                        "Ubicación obtenida: ${location.latitude}, ${location.longitude}"
                    )
                    (application as HomeApplication).emisoraViewModel.getEmisorasCercanas(location)
                } else {
                    Log.e("MainActivity", "No se pudo obtener la ubicación")
                }
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            return false
        }
        return true
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as HomeApplication
    val authService =
        AuthService(application.firebaseAuth, application.db, application.dataStoreManager)
    val loginViewModel = createLoginViewModel(application)
    val emisoraViewModel = application.emisoraViewModel
    val podcastViewModel = application.podcastViewModel
    val usuarioViewModel = application.usuarioViewModel
    val emisoraRepository = application.emisoraRepository
    // Create UsuarioPerfilViewModel here
    val usuarioRepository = UsuarioRepository(application.db)
    val usuarioPerfilViewModel =
        UsuarioPerfilViewModel(usuarioRepository, authService, application.db, application.storage)

    NavigationHost(
        navController,
        PaddingValues(),
        loginViewModel, // Pasar loginViewModel
        formularioViewModel = FormularioViewModel(),
        authService,
        emisoraViewModel,
        podcastViewModel,
        usuarioViewModel,
        dataStoreManager = application.dataStoreManager,
        usuarioPerfilViewModel,
        emisoraRepository
    )
}

@Composable
fun PantallaPresentacion(navController: NavHostController, loginViewModel: LoginScreenViewModel) {
    var showAnimation by remember { mutableStateOf(false) }
    var animationMessage by remember { mutableStateOf("") }
    var showIntro by remember { mutableStateOf(true) } // Estado para mostrar/ocultar la introducción

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = 0.7f),
                blendMode = BlendMode.Darken
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título Principal
            Text(
                text = "BIENVENIDO A:",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 43.sp,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo
            Text(
                text = "CASANARE STEREO NETWORK",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center
            )
            Text(
                text = "DONDE LATE EL CORAZÓN DEL LLANO",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))
            // Logo
            Image(
                painter = painterResource(id = R.drawable.transmedia_lab),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(80.dp) // Tamaño reducido a 80dp
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)) // Sombra de 4dp con esquinas redondeadas
                    .clip(RoundedCornerShape(8.dp)) // Recorta la imagen a un cuadrado con esquinas redondeadas
            )
            /*
            Button(
                onClick = {
                    navController.navigate(Destinos.CasanareLoginScreen.ruta) {
                        launchSingleTop = true
                    }
                    // Mostrar mensaje o animación con la información del usuario
                    val currentUser = loginViewModel.authState.value
                    if (currentUser is EstadoAutenticacion.LoggedIn) {
                        // Accede a la información del usuario (correo o nombre)


                        // Actualiza el estado para mostrar el Snackbar
                        showAnimation = true
                        animationMessage = "Bienvenido"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
            {
                Text(
                    "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Mostrar la animación si showAnimation es true

            AnimatedVisibility(visible = showAnimation) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        animationMessage,
                        fontSize = 20.sp
                    )
                }
            }
             */

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.scrim
                )
            ) {
                Text(
                    "Casanare es científico ¿ y tú?",
                    color = MaterialTheme.colorScheme.background
                )

            }

            Spacer(modifier = Modifier.height(24.dp))
            /*
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = MaterialTheme.colorScheme.background,
                )
                Text(
                    "Registrate",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Destinos.CasanareLoginScreen.ruta)
                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            }
             */
            Spacer(modifier = Modifier.weight(1f)) // Empuja el texto hacia abajo

            // Políticas de Privacidad
            PoliticasDePrivacidad(navController = navController)
        }
        // Mensaje de Introducción (Toast-like)
        AnimatedVisibility(
            visible = showIntro,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Fondo Oscuro
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)) // Fondo oscuro
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Esta App en su primera versión está diseñada para estudiantes y profesores del departamento de Casanare, Colombia, con el propósito de desarrollar habilidades para aprender a contar historias de ciencia e identificar la percepción que tienen sobre la ciencia, tecnología e innovación en el territorio.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
    // Efecto para ocultar la introducción después de 5 segundos
    LaunchedEffect(key1 = showIntro) {
        if (showIntro) {
            delay(5000) // 5 segundos
            showIntro = false
        }
    }
}

@Composable
fun PoliticasDePrivacidad(navController: NavHostController) {
    val context = LocalContext.current
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.background)) {
            append("Al continuar, aceptas nuestras ")
        }
        pushStringAnnotation(
            tag = "politicas",
            annotation = "https://sites.google.com/cstar.com.co/casanareestereonetwork/pol%C3%ADticas-de-privacidad"
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.error,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Políticas de Privacidad")
        }
        pop()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "politicas", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            },
            style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
        )
    }
}

