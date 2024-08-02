package com.jcmateus.casanarestereo.screens.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
//import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Home
//import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
//import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.navigation.VistasCasanare
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.home.HomeActivity
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme
import kotlinx.coroutines.launch

class CasanareLoginActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            CasanareStereoTheme {
                CasanareLoginScreen(navController = rememberNavController())
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
                navController.navigate(Destinos.FormularioScreen.ruta)
            }
        }
        catch(ex: Exception){
                Log.d("Casanare", "LoginScreen: ${ex.message}")
        }

    }
    // Mostrar indicador de progreso si isLoading es true
    if (isLoading) {
        CircularProgressIndicator()
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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            verticalArrangement = Arrangement.Center
        ) {
            Box {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.surface // Cambia el color del texto para que se vea sobre la imagen
                    )
                    Text(
                        text = "DONDE LATE EL CORAZÓN DEL LLANO",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surface  // Cambia el color del texto para que se vea sobre la imagen
                    )
                }
            }
            Spacer(modifier = Modifier.padding(65.dp))
            if (showLoginForm.value) {
                Text(
                    text = "Crea una cuenta",
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                )
                UserForm(
                    isCreateAccount = true
                ) { email, password ->
                    Log.d("Casanare", "Creando cuenta con$email y $password")
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(Destinos.FormularioScreen.ruta)
                    }
                }
            } else {
                Text(text = "Inicia sesión",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    color = MaterialTheme.colorScheme.surface)
                UserForm(
                    isCreateAccount = false,

                    )
                { email, password ->
                    Log.d("Casanare", "Iniciando sesión con $email y $password")
                    viewModel.signInWithEmailAndPassword(email, password) {
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
                    text = text1, color = MaterialTheme.colorScheme.surface,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Light,
                )
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 10.dp),
                    color = MaterialTheme.colorScheme.error

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(4.dp)
                    .absoluteOffset(x = 0.dp, y = (-403).dp)
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
                        .size(50.dp)
                        .padding(8.dp)
                )
                Text(text = "Continuar con Google",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                    )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = CheckNotificaciones,
                    onCheckedChange = { CheckNotificaciones = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Acepto Notificaciones")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = CheckTerminos,
                    onCheckedChange = { CheckTerminos = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("términos y condiciones")
            }


        }


    }

    // Mostrar SnackbarHost
    SnackbarHost(hostState = snackbarHostState)
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String, action: () -> Unit) {
    val scope = rememberCoroutineScope()
    scope.launch {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
            actionLabel = "Aceptar"
        )
        if (snackbarResult == SnackbarResult.ActionPerformed || snackbarResult == SnackbarResult.Dismissed) {
            action()// Ejecutar la acción después de que se muestre el Snackbar
        }

    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
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
    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
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
            .fillMaxWidth(0.8f),
        shape = CircleShape,
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.secondary
        )

    ) {
        Text(
            text = textId,
            modifier = Modifier
                .padding(4.dp),
            //MaterialTheme.colorScheme.error

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
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.surface) },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall,
        colors = OutlinedTextFieldDefaults.colors(Color.LightGray),
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
        }
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
            tint = MaterialTheme.colorScheme.surface
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
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.surface) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(Color.LightGray),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CasanareStereoTheme {
        CasanareLoginScreen(navController = rememberNavController())
    }
}





