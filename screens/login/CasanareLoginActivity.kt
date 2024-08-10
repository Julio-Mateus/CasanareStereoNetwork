@file:Suppress("DEPRECATION")

package com.jcmateus.casanarestereo.screens.login

//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.width
//import androidx.compose.ui.graphics.BlendMode
//import androidx.compose.ui.graphics.ColorFilter
//import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jcmateus.casanarestereo.AnimatedSoundWave
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme

class CasanareLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = (application as HomeApplication).navController // Obtener navController de la aplicación
        setContent {
            CasanareStereoTheme {
                CasanareLoginScreen(navController = navController)
            }
        }
    }
}

@Composable
fun CasanareLoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {
    val isLoading by viewModel.loading.observeAsState(initial = false)
    val errorMessage by viewModel.errorMessage.observeAsState(initial = null)
    var CheckNotificaciones by remember { mutableStateOf(false) }
    var CheckTerminos by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() } // Crear SnackbarHostState
    val isLoggedIn by viewModel.isLoggedIn.collectAsState() // Observa el estado de inicio de sesión
    val successMessage by viewModel.successMessage.observeAsState(initial = null)
    //True = Login; False = Create
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    val token = "792234410149-82bpdkviurrvrr69g49irmemrafdam82.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        val task = getSignedInAccountFromIntent(it.data)
        try{
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                navController.navigate(PantallaFormulario.SeleccionRol.ruta)
            }
        }
        catch(ex: Exception){
            Log.d("Casanare", "LoginScreen: ${ex.message}")
        }

    }
    // Mostrar indicador de progreso si isLoading es true
    if (isLoading) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
    when{
        // Mostrar mensaje de error si errorMessage no es nulo
        errorMessage != null -> {
            ShowSnackbar(snackbarHostState, errorMessage!!){
                viewModel.clearErrorMessage() // Limpiar el mensaje de error después de mostrarlo
            }
        }
        // Mostrar mensaje de éxito si successMessage no es nulo
        successMessage != null -> {
            ShowSnackbar(snackbarHostState, successMessage!!) {
                viewModel.clearSuccessMessage() // Limpiar el mensaje de éxito después de mostrarlo
            }
        }
    }
    LaunchedEffect(key1 = isLoggedIn) {
        if (isLoggedIn) {
            if (viewModel.isCreateUser) {
                navController.navigate(PantallaFormulario.SeleccionRol.ruta)
            } else {
                navController.navigate(Destinos.HomeCasanareVista.ruta)
            }
            viewModel.clearIsLoggedIn()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.fondo), // Reemplaza con tu imagen
                contentDescription = "Imagen de fondo",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF000000).copy(alpha = 0.9f)),
                contentScale = ContentScale.Crop,
            )
            // Capa de oscurecimiento
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF000000).copy(alpha = 0.7f))
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier= Modifier.fillMaxSize()
        ) {
            Box {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedSoundWave()
                    Spacer(modifier = Modifier.padding(20.dp))
                    Text(
                        text = "CASANARE STEREO NETWORK",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground // Cambia el color del texto para que se vea sobre la imagen
                    )
                    Text(
                        text = "DONDE LATE EL CORAZÓN DEL LLANO",
                        style = MaterialTheme.typography.bodySmall,
                        //fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground  // Cambia el color del texto para que se vea sobre la imagen
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(15.dp)
                    //.absoluteOffset(x = 0.dp, y = (-403).dp)
                    .clip(RoundedCornerShape(15.dp))
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable {
                        val opciones = GoogleSignInOptions
                            .Builder(
                                GoogleSignInOptions.DEFAULT_SIGN_IN
                            )
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSignInClient = getClient(context, opciones)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo_de_google_48),
                    contentDescription = "Google",
                    modifier = Modifier
                        .size(38.dp)
                        .padding(6.dp)
                )
                Text(text = "Continuar con Google",
                    fontSize = 12.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (showLoginForm.value) {
                Text(
                    text = "Crea una cuenta",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                )
                UserForm(
                    isCreateAccount = true,
                    CheckNotificaciones = CheckNotificaciones,
                    CheckTerminos = CheckTerminos
                ) { email, password ->
                    Log.d("Casanare", "Creando cuenta con$email y $password")
                    viewModel.createUserWithEmailAndPassword(email, password, CheckNotificaciones, CheckTerminos) {
                        navController.navigate(PantallaFormulario.SeleccionRol.ruta)
                    }
                }
            } else {
                Text(text = "Inicia sesión",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary)
                UserForm(
                    isCreateAccount = false,
                    CheckNotificaciones = CheckNotificaciones,
                    CheckTerminos = CheckTerminos

                )
                { email, password ->
                    Log.d("Casanare", "Iniciando sesión con $email y $password")
                    viewModel.signInWithEmailAndPassword(email, password, CheckNotificaciones, CheckTerminos) {
                        navController.navigate(Destinos.HomeCasanareVista.ruta)
                    }
                }
            }
            Spacer(modifier = Modifier.padding(15.dp))

            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val text1 = if (
                    showLoginForm.value) "Ya tienes cuenta?" else "No tienes cuenta?"
                val text2 = if (showLoginForm.value) "Inicia sesión" else "Registrate"
                Text(
                    text = text1, color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Light,
                )
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 15.dp),
                    color = MaterialTheme.colorScheme.error

                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = CheckNotificaciones,
                    onCheckedChange = { CheckNotificaciones = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary, // <- Usar color primario del tema
                        uncheckedColor = MaterialTheme.colorScheme.onSurface, //<- Usar color de texto sobre superficie
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Acepto Notificaciones", color = MaterialTheme.colorScheme.onPrimary)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("términos y condiciones")
                    }
                }
                Checkbox(
                    checked = CheckTerminos,
                    onCheckedChange = { CheckTerminos = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary, // <- Usar color primario del tema
                        uncheckedColor = MaterialTheme.colorScheme.onSurface, //<- Usar color de texto sobre superficie
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = annotatedString,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.clickable {
                        // Abrir la página de términos y condiciones
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("URL_DE_TERMINOS_Y_CONDICIONES"))
                        context.startActivity(intent)
                    }
                )
            }


        }
    }
    // Mostrar SnackbarHost
    SnackbarHost(hostState = snackbarHostState)
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String, action: () -> Unit) {
    LaunchedEffect(key1 = message) { // Lanzar la corrutina con LaunchedEffect
        val snackbarResult = snackbarHostState.showSnackbar(
            message = message,
            withDismissAction = true,
            duration = SnackbarDuration.Short,
            actionLabel = "Aceptar"
        )
        if (snackbarResult == SnackbarResult.ActionPerformed || snackbarResult == SnackbarResult.Dismissed) {
            action()
        }
    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
    CheckNotificaciones: Boolean,
    CheckTerminos: Boolean,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty() && CheckNotificaciones && CheckTerminos
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(
            emailState = email,
            labelId = "Correo",
            )
        PasswordInput(
            passwordState = password,
            labelId = "Contraseña",
            passwordVisible = passwordVisible,
        )
        //Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos y botón
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Iniciar sesión",
            inputValido = valido,


            ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }

    }

}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
            .then(if (inputValido) Modifier.shadow(elevation = 2.dp, shape = MaterialTheme.shapes.medium) else Modifier), // Añadir sombra si está habilitado,
        shape = MaterialTheme.shapes.medium,
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (inputValido) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surfaceVariant, // Cambiar color si está deshabilitado
            contentColor = if (inputValido) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant // Cambiar color si está deshabilitado
        )

    ) {
        Text(
            text = textId,
            modifier = Modifier
                .padding(4.dp),
            color = if (inputValido) MaterialTheme.colorScheme.primary else Color.White, // Cambiar color si está deshabilitado
            // ... otros estilos

        )
    }

}

@Composable
// Campo de contraseña
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.onPrimary) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(Color.White),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        //MaterialTheme.colorScheme.inverseOnSurface,
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            } 
        },
        textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
    )

}

@Composable
//Visibilidad del icono de la contraseña
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,

    )

}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.onPrimary) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
       colors = OutlinedTextFieldDefaults.colors(Color.White),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground)
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CasanareStereoTheme {
        CasanareLoginScreen(navController = rememberNavController())
    }
}





